package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import el.sft.bw.R
import el.sft.bw.activities.PlayerActivity
import el.sft.bw.activities.UserCenterActivity
import el.sft.bw.components.UserCardData
import el.sft.bw.databinding.FragmentVideoInfoBinding
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.network.ApiClient
import el.sft.bw.network.model.UserCardModel
import el.sft.bw.network.simplestruct.VideoPage
import el.sft.bw.utils.runWithFragment
import el.sft.bw.utils.setClipboardPlainText
import el.sft.bw.utils.toHumanReadable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VideoInfoFragment : ScrollableFragment() {
    private lateinit var episodesAdapter: VideoPageArrayAdapter
    private lateinit var binding: FragmentVideoInfoBinding
    private lateinit var currentBvId: String

    private var uploaderModel: UserCardModel? = null

    private var episodesList: MutableList<VideoPage> = ArrayList()
    private var descriptionExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoInfoBinding.inflate(inflater, container, false)
        requireArguments().let {
            currentBvId = it.getString("bvId")!!
        }

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
            if (uploaderModel == null) return@setOnClickListener

            Intent(requireContext(), UserCenterActivity::class.java).also {
                it.putExtra("userId", uploaderModel!!.mid!!)
                startActivity(it)
            }
        }

        binding.description.setOnClickListener {
            descriptionExpanded = !descriptionExpanded
            binding.description.maxLines = if (descriptionExpanded) 3 else 256
        }


        binding.videoId.setOnClickListener {
            setClipboardPlainText(requireContext(), currentBvId)

            Toast
                .makeText(requireContext(), R.string.copied, Toast.LENGTH_LONG)
                .show()
        }

        requestLoadVideoInfo()

        return binding.root
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
    private fun requestReportHistory(cId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                ApiClient.reportHistory(currentBvId, cId)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadVideoInfo() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                val res = ApiClient.getVideoDetail(currentBvId)
                val videoModel = res.data!!.videoModel
                uploaderModel = res.data.card!!.videoOwner

                runWithFragment(this@VideoInfoFragment) {
                    binding.title.text = videoModel?.title ?: "-"
                    binding.viewCount.text = videoModel?.stat?.view?.toHumanReadable() ?: "-"
                    binding.description.text =
                        if (videoModel?.desc == null || videoModel.desc?.isEmpty() != false) "-" else videoModel.desc
                    binding.videoId.text = currentBvId

                    if (videoModel?.pages != null) {
                        episodesList.addAll(videoModel.pages)
                        episodesAdapter.notifyDataSetChanged()
                    }

                    binding.userCard.rebind(
                        UserCardData(
                            uploaderModel?.name ?: "-",
                            uploaderModel?.face ?: "",
                            uploaderModel?.followerCount ?: 0L
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithFragment(this@VideoInfoFragment) {
                    val ctx = requireContext()
                    Toast
                        .makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}