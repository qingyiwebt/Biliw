package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import el.sft.bw.R
import el.sft.bw.activities.AddFavoriteActivity
import el.sft.bw.activities.PlayerActivity
import el.sft.bw.activities.UserCenterActivity
import el.sft.bw.components.UserCardData
import el.sft.bw.databinding.FragmentVideoInfoBinding
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.network.ApiClient
import el.sft.bw.network.dto.VideoDetailResponse
import el.sft.bw.network.model.UserCardModel
import el.sft.bw.network.simplestruct.VideoPage
import el.sft.bw.utils.getAccentColor
import el.sft.bw.utils.getDefaultColor
import el.sft.bw.utils.runWithFragment
import el.sft.bw.utils.setClipboardPlainText
import el.sft.bw.utils.toHumanReadable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VideoInfoFragment : ScrollableFragment() {
    private lateinit var episodesAdapter: VideoPageArrayAdapter
    private lateinit var binding: FragmentVideoInfoBinding

    private lateinit var primaryColor: ColorStateList
    private lateinit var defaultColor: ColorStateList

    private lateinit var currentBvId: String

    private var videoDetail: VideoDetailResponse? = null
    private var videoUploader: UserCardModel? = null
    private var videoLiked: Boolean? = null

    private var episodesList: MutableList<VideoPage> = ArrayList()
    private var descriptionExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoInfoBinding.inflate(inflater, container, false)
        requireArguments().let {
            currentBvId = it.getString("bvId")!!
        }

        primaryColor = ColorStateList.valueOf(getAccentColor(requireContext()))
        defaultColor = ColorStateList.valueOf(
            getDefaultColor(requireContext())
        )

        episodesAdapter = VideoPageArrayAdapter()
        binding.episodesList.adapter = episodesAdapter
        binding.episodesList.setOnItemClickListener { _, _, position, _ ->
            val item = episodesAdapter.getItem(position)

            requestReportHistory(item!!.cid!!)

            Intent(requireContext(), PlayerActivity::class.java).also {
                it.putExtra("source", PlayerActivity.SOURCE_BILI)
                it.putExtra("bvId", currentBvId)
                it.putExtra("cId", item.cid!!)
                startActivity(it)
            }
        }

        binding.userCard.setOnClickListener {
            if (videoUploader == null) return@setOnClickListener

            Intent(requireContext(), UserCenterActivity::class.java).also {
                it.putExtra("userId", videoUploader!!.mid!!)
                startActivity(it)
            }
        }

        binding.description.setOnClickListener {
            descriptionExpanded = !descriptionExpanded
            binding.description.maxLines = if (descriptionExpanded) 3 else 256
        }


        binding.videoId.setOnClickListener {
            setClipboardPlainText(requireContext(), currentBvId)
            Toast.makeText(requireContext(), R.string.copied, Toast.LENGTH_LONG).show()
        }

        binding.likeButton.setOnClickListener {
            if (videoLiked == null) return@setOnClickListener
            requestLikeVideo()
        }

        binding.favButton.setOnClickListener {
            if (videoDetail == null) return@setOnClickListener
            Intent(requireContext(), AddFavoriteActivity::class.java).let {
                it.putExtra("bvId", currentBvId)
                startActivity(it)
            }
        }

        requestLoadVideoInfo()

        return binding.root
    }

    private fun updateVideoInfo() {
        val videoModel = videoDetail!!.videoModel

        binding.title.text = videoModel.title ?: "-"
        binding.viewCount.text = videoModel.stat.view.toHumanReadable()
        binding.description.text = if (videoModel.desc.isNullOrEmpty()) "-" else videoModel.desc
        binding.videoId.text = currentBvId

        episodesList.addAll(videoModel.pages)
        episodesAdapter.notifyDataSetChanged()

        binding.userCard.rebind(
            UserCardData(
                videoUploader?.name ?: "-",
                videoUploader?.face ?: "",
                videoUploader?.followerCount ?: 0L
            )
        )
    }

    private fun updateLikedStat() {
        when (videoLiked) {
            true -> {
                binding.likeIcon.backgroundTintList = primaryColor
            }

            else -> {
                binding.likeIcon.backgroundTintList = defaultColor
            }
        }
    }

    inner class VideoPageArrayAdapter : ArrayAdapter<VideoPage>(requireContext(), 0, episodesList) {
        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val item = getItem(position)
            val currentView = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.layout_episode_item, parent, false)

            val title = currentView.findViewById<TextView>(R.id.episodeName)
            title.text = "P${position + 1}: ${item?.part ?: "?..."}"

            return currentView
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestReportHistory(cId: Int) = GlobalScope.launch(Dispatchers.IO) {
        try {
            ApiClient.reloadCookie()
            ApiClient.reportHistory(currentBvId, cId)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadVideoInfo() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                videoDetail = ApiClient.getVideoDetail(currentBvId).data
                videoUploader = videoDetail!!.card.videoOwner
                videoLiked = when (ApiClient.getLikedStat(currentBvId).data) {
                    0 -> false
                    1 -> true
                    else -> null
                }

                runWithFragment(this@VideoInfoFragment) {
                    updateVideoInfo()
                    updateLikedStat()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithFragment(this@VideoInfoFragment) {
                    val ctx = requireContext()
                    Toast.makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLikeVideo() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();

                val newState = !videoLiked!!
                val response = ApiClient.setLikeState(currentBvId, newState)
                if (response.code == 0) videoLiked = newState

                runWithFragment(this@VideoInfoFragment) {
                    updateLikedStat()

                    val text = when (response.code) {
                        0 -> R.string.error_success
                        -101 -> R.string.error_not_loggedin
                        -111 -> R.string.error_csrf
                        else -> R.string.error_unknown
                    }
                    val ctx = requireContext()
                    Toast.makeText(ctx, text, Toast.LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithFragment(this@VideoInfoFragment) {
                    val ctx = requireContext()
                    Toast.makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}