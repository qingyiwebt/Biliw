package el.sft.bw.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView.ControllerVisibilityListener
import el.sft.bw.R
import el.sft.bw.databinding.ActivityPlayerBinding
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.network.ApiClient
import el.sft.bw.network.dto.VideoStreamResponse
import el.sft.bw.utils.GlobalHttpClientUtils
import el.sft.bw.utils.finishWithEmptyString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private lateinit var player: ExoPlayer

    private var currentSource: Int = SOURCE_UNKNOWN
    private var currentUrl: String? = null
    private var currentBvId: String? = null
    private var currentCId: Int? = null
    private var orientationLandscape = false

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES;

        player = ExoPlayer.Builder(this@PlayerActivity)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
            .also {
                binding.player.player = it
                binding.controller.player = it
            }

        binding.controller.rotateAction = Runnable {
            orientationLandscape = !orientationLandscape
            requestedOrientation =
                if (orientationLandscape)
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        intent.let {
            currentSource = it.getIntExtra("source", SOURCE_UNKNOWN)

            when (currentSource) {
                SOURCE_URL -> {
                    currentUrl = it.getStringExtra("url") ?: finishWithEmptyString()
                }

                SOURCE_BILI -> {
                    currentBvId = it.getStringExtra("bvId") ?: finishWithEmptyString()
                    currentCId = it.getIntExtra("cId", -1)
                    if (currentCId == -1) finish()
                }
            }
        }

        requestLoadPlayer()
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    private fun requestLoadPlayer() {
        when (currentSource) {
            SOURCE_URL -> loadUrlVideo()
            SOURCE_BILI -> requestLoadBiliVideo()
        }
    }

    private fun loadUrlVideo() {
        binding.controller.title = currentUrl!!
        try {
            MediaItem.fromUri(currentUrl!!).also {
                player.setMediaItem(it)
                player.prepare()
                player.playWhenReady = true
            }
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_unknown_with_msg, ex.message),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
            finish()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoadBiliVideo() {
        binding.controller.title = currentBvId!!
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                val res = ApiClient.getVideoStream(currentBvId!!, currentCId!!)
                val data = res.data!!
                withContext(Dispatchers.Main) { loadBiliVideo(data) }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_unknown_with_msg, ex.message),
                        Toast.LENGTH_LONG
                    ).show()
                    ex.printStackTrace()
                    finish()
                }
            }
        }
    }


    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun loadBiliVideo(data: VideoStreamResponse) {
        currentUrl = data.durl[0].url

        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(GlobalHttpClientUtils.generateHeaders().also {
                it["Referer"] = "https://www.bilibili.com/video/$currentBvId"
            })

        val dataSourceFactory = DefaultDataSource.Factory(this, httpDataSourceFactory)

        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(currentUrl!!))

        player.addMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    companion object {
        const val SOURCE_UNKNOWN = -1
        const val SOURCE_URL = 0
        const val SOURCE_BILI = 1
    }
}