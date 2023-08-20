package el.sft.bw.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import el.sft.bw.R
import el.sft.bw.databinding.LayoutPlayerControllerBinding

class PlayerControllerComponent : FrameLayout {
    private lateinit var binding: LayoutPlayerControllerBinding
    private val eventListener = PlayerListener()

    private var isControllerDisplay = false
    private var isProgressBarDragging = false
    private var isRootDoubleClick = false
    private var isLongPressSpeedUp = false

    private val progressWatcherRunnable = Runnable { progressWatch() }
    private var durationString: String = "0:0:0"

    private lateinit var pauseIconDrawable: Drawable
    private lateinit var resumeIconDrawable: Drawable

    var player: ExoPlayer? = null
        set(value) {
            removeListener()
            value?.addListener(eventListener)
            field = value
        }

    var rotateAction: Runnable? = null

    var title: String
        set(value) {
            binding.title.text = value
        }
        get() {
            return binding.title.text.toString()
        }


    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context, attributeSet, defStyle
    ) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(
        context, attributeSet
    ) {
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        val inflater = LayoutInflater.from(context)
        binding = LayoutPlayerControllerBinding.inflate(inflater, this, true)

        updateDisplayState()
        binding.root.setOnClickListener {
            if (isRootDoubleClick) {
                togglePause()
                isRootDoubleClick = false
                return@setOnClickListener
            }

            isRootDoubleClick = true
            handler.postDelayed({ isRootDoubleClick = false }, 750)

            isControllerDisplay = !isControllerDisplay
            updateDisplayState()
        }

        binding.root.setOnLongClickListener { beginLongPress(); true }
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                endLongPress()
            }
            false
        }

        pauseIconDrawable =
            AppCompatResources.getDrawable(context, R.drawable.ic_baseline_pause_24)!!

        resumeIconDrawable =
            AppCompatResources.getDrawable(context, R.drawable.ic_baseline_play_arrow_24)!!

        binding.progressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                val longProgress = progress.toLong()
                player?.seekTo(longProgress)
                updateElapsedTime(longProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                player?.playWhenReady = false
                isProgressBarDragging = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isProgressBarDragging = false
                player?.playWhenReady = true
            }
        })

        binding.playButton.setOnClickListener { togglePause() }
        binding.rotateButton.setOnClickListener { rotateAction?.run() }

        binding.forwardButton.setOnClickListener { player?.seekForward() }
        binding.replayButton.setOnClickListener { player?.seekBack() }
    }

    private fun togglePause() {
        player?.playWhenReady = !(player?.playWhenReady ?: false)
    }

    private fun updateDisplayState() {
        val displayFlag = if (isControllerDisplay) View.VISIBLE else View.GONE
        binding.topBar.visibility = displayFlag
        binding.bottomBar.visibility = displayFlag
    }

    private fun removeListener() {
        player?.removeListener(eventListener)
        endProgressWatch()
    }

    override fun onDetachedFromWindow() {
        removeListener()
        super.onDetachedFromWindow()
    }

    private fun beginLongPress() {
        isLongPressSpeedUp = true
        player?.setPlaybackSpeed(3f)
    }

    private fun endLongPress() {
        if (!isLongPressSpeedUp) return
        player?.setPlaybackSpeed(1f)
    }

    private fun beginProgressWatch() {
        val duration = player?.duration ?: 0L
        durationString = DateUtils.formatElapsedTime(duration / 1000)
        binding.progressBar.max = duration.toInt()
        binding.playButton.setCompoundDrawablesWithIntrinsicBounds(
            pauseIconDrawable, null, null, null
        )

        handler?.postDelayed(progressWatcherRunnable, 500)
    }

    private fun endProgressWatch() {
        handler?.removeCallbacks(progressWatcherRunnable)
        binding.playButton.setCompoundDrawablesWithIntrinsicBounds(
            resumeIconDrawable, null, null, null
        )
    }

    private fun progressWatch() {
        if (!isProgressBarDragging) {
            val currentPosition = player?.currentPosition ?: 0L
            binding.progressBar.progress = currentPosition.toInt()
            updateElapsedTime(currentPosition)
        }
        handler.postDelayed(progressWatcherRunnable, 1000)
    }

    private fun updateElapsedTime(ms: Long) {
        binding.timeText.text = "${DateUtils.formatElapsedTime(ms / 1000)} / $durationString"
    }

    inner class PlayerListener : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                this@PlayerControllerComponent.beginProgressWatch()
            } else {
                this@PlayerControllerComponent.endProgressWatch()
            }
            super.onIsPlayingChanged(isPlaying)
        }
    }
}