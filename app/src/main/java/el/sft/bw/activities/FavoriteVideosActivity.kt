package el.sft.bw.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import el.sft.bw.R
import el.sft.bw.components.VideoCardData
import el.sft.bw.components.VideoCardLayout
import el.sft.bw.databinding.ActivityFavoriteVideosBinding
import el.sft.bw.framework.SpacingDecoration
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.framework.components.RecyclerItemClickListener
import el.sft.bw.framework.viewbinding.ListBindingAdapter
import el.sft.bw.network.ApiClient
import el.sft.bw.utils.runWithActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteVideosActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivityFavoriteVideosBinding
    private val videoList: ArrayList<VideoCardData> = ArrayList()
    private val videoAdapter = ListBindingAdapter(videoList) { VideoCardLayout() }

    private var currentMlid: Long = -1
    private var currentPage: Int = 1
    private var isEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            currentMlid = intent.getLongExtra("mlid", -1)
            if (currentMlid == -1L) finish()
        }

        binding.titleBar.setOnClickListener { finish() }
        binding.refreshLayout.setOnRefreshListener { requestLoadList() }
        binding.favVideoList.adapter = videoAdapter
        binding.favVideoList.addItemDecoration(SpacingDecoration(6))
        binding.favVideoList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.favVideoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) requestLoadList(false)
            }
        })
        binding.favVideoList.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                binding.favVideoList,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val data = videoList[position]
                        if (data.bvId.isEmpty()) return
                        onVideoCardClick(data)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        requestLoadList()
    }

    private fun onVideoCardClick(videoCardData: VideoCardData) {
        Intent(this, VideoActivity::class.java).also {
            it.putExtra("bvId", videoCardData.bvId)
            startActivity(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadList(clearAll: Boolean = true) {
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
                val res = ApiClient.getFavVideos(currentMlid, currentPage)
                runWithActivity(this@FavoriteVideosActivity) {
                    val beforeCount = videoList.size
                    val list = res.data!!.items

                    videoList.addAll(list.map { x ->
                        VideoCardData(
                            x.title!!,
                            x.upper!!.name!!,
                            x.stat?.view ?: 0L,
                            x.cover!!,
                            x.bvid!!
                        )
                    })

                    videoAdapter.notifyItemRangeInserted(beforeCount, list.size)

                    if (list.size < 20) isEnded = true
                }
                currentPage++
            } catch (ex: Exception) {
                ex.printStackTrace()
                runWithActivity(this@FavoriteVideosActivity) {
                    Toast
                        .makeText(
                            this@FavoriteVideosActivity,
                            R.string.error_load_failed,
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            } finally {
                runWithActivity(this@FavoriteVideosActivity) {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
    }
}