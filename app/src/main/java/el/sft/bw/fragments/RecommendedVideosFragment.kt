package el.sft.bw.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import el.sft.bw.R
import el.sft.bw.activities.VideoActivity
import el.sft.bw.components.VideoCardData
import el.sft.bw.components.VideoCardLayout
import el.sft.bw.databinding.FragmentRecommendedVideosBinding
import el.sft.bw.framework.SpacingDecoration
import el.sft.bw.framework.components.RecyclerItemClickListener
import el.sft.bw.framework.viewbinding.ListBindingAdapter
import el.sft.bw.network.ApiClient
import el.sft.bw.network.model.VideoModel
import el.sft.bw.utils.runWithActivity
import el.sft.bw.utils.runWithFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendedVideosFragment : Fragment() {
    private lateinit var binding: FragmentRecommendedVideosBinding
    private val videoList: ArrayList<VideoCardData> = ArrayList()
    private val videoAdapter = ListBindingAdapter(videoList) { VideoCardLayout() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedVideosBinding.inflate(inflater, container, false)

        binding.videoList.adapter = videoAdapter
        binding.videoList.addItemDecoration(SpacingDecoration(6))
        binding.videoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.videoList.addOnItemTouchListener(
            RecyclerItemClickListener(
                this.requireContext(),
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
        binding.videoList.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) requestVideoLoad(false)
            }
        })

        binding.refreshLayout.setOnRefreshListener { requestVideoLoad(true) }

        requestVideoLoad(true)

        return binding.root
    }

    private fun onVideoCardClick(videoCardData: VideoCardData) {
        Intent(this.requireContext(), VideoActivity::class.java).also {
            it.putExtra("bvId", videoCardData.bvId)
            startActivity(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestVideoLoad(clearAll: Boolean) {
        binding.refreshLayout.isRefreshing = true
        if (clearAll) {
            val beforeCount = videoList.size
            videoList.clear()
            videoAdapter.notifyItemRangeRemoved(0, beforeCount)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                val res = ApiClient.getRecommendedVideos()

                runWithFragment(this@RecommendedVideosFragment) {
                    val items: ArrayList<VideoModel> = res.data?.item ?: return@runWithFragment

                    val beforeCount = videoList.size
                    videoList.addAll(items.map { x ->
                        VideoCardData(
                            x.title!!,
                            x.owner!!.name!!,
                            x.stat?.view ?: 0L,
                            x.pic!!,
                            x.bvid!!
                        )
                    })

                    videoAdapter.notifyItemRangeInserted(
                        beforeCount,
                        items.size
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithFragment(this@RecommendedVideosFragment) {
                    val ctx = requireContext()
                    Toast
                        .makeText(ctx, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                runWithFragment(this@RecommendedVideosFragment) {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
    }
}