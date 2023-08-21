package el.sft.bw.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import el.sft.bw.R
import el.sft.bw.activities.VideoActivity
import el.sft.bw.components.VideoCardData
import el.sft.bw.components.VideoCardLayout
import el.sft.bw.databinding.FragmentUserVideosBinding
import el.sft.bw.framework.SpacingDecoration
import el.sft.bw.framework.components.RecyclerItemClickListener
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.framework.viewbinding.ListBindingAdapter
import el.sft.bw.network.ApiClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserVideosFragment : ScrollableFragment() {
    private lateinit var binding: FragmentUserVideosBinding


    private var currentPage: Int = 1
    private var isEnded = false

    private val videoList: ArrayList<VideoCardData> = arrayListOf()
    private val videoAdapter = ListBindingAdapter(videoList) { VideoCardLayout() }
    private var currentUserId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserVideosBinding.inflate(inflater, container, false)
        requireArguments().let {
            currentUserId = it.getLong("userId", 1)
        }

        binding.videoList.adapter = videoAdapter
        binding.videoList.addItemDecoration(SpacingDecoration(6))
        binding.videoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.videoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) requestLoadVideos(false)
            }
        })
        binding.videoList.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.videoList,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val data = videoList[position]
                        if (data.bvId.isEmpty()) return
                        onVideoCardClick(data)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        requestLoadVideos()
        return binding.root
    }

    private fun onVideoCardClick(videoCardData: VideoCardData) {
        Intent(requireContext(), VideoActivity::class.java).also {
            it.putExtra("bvId", videoCardData.bvId)
            startActivity(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadVideos(clearAll: Boolean = true) {
        if (clearAll) {
            currentPage = 1
            isEnded = false

            val beforeCount = videoList.size
            videoList.clear()
            videoAdapter.notifyItemRangeRemoved(0, beforeCount)
        } else if (isEnded) return

        binding.refreshLayout.isRefreshing = true

        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie()
                val res = ApiClient.getUserVideos(currentUserId, currentPage)
                withContext(Dispatchers.Main) {
                    val list = res.data!!.videoList.videoList

                    val beforeCount = videoList.size
                    videoList.addAll(list.map { x ->
                        VideoCardData(
                            x.title!!,
                            x.author ?: "-",
                            x.playCount ?: 0L,
                            x.pic!!,
                            x.bvid!!
                        )
                    })

                    videoAdapter.notifyItemRangeInserted(beforeCount, list.size)
                    if (list.size < 20) isEnded = true
                }
                currentPage++
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(
                            requireContext(),
                            R.string.error_load_failed,
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
    }
}