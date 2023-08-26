package el.sft.bw.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import el.sft.bw.R
import el.sft.bw.databinding.ActivityVideoBinding
import el.sft.bw.fragments.VideoInfoFragment
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.utils.finishWithEmptyString

class VideoActivity : SwipeBackAppCompatActivity() {
    private val viewPagerAdapter = ViewPagerAdapter(this)
    private lateinit var binding: ActivityVideoBinding
    private lateinit var currentBvId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            currentBvId = it.getStringExtra("bvId") ?: finishWithEmptyString()
        }

        binding.pager.adapter = viewPagerAdapter
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.titleBar.text = getString(viewPagerAdapter.getTitle(position))
            }
        })

        binding.titleBar.setOnClickListener {
            if (binding.pager.currentItem != VIDEO)
                binding.pager.setCurrentItem(VIDEO, true)
            else finish()
        }
    }

    inner class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 1

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                VIDEO -> {
                    val videoInfoFragment = VideoInfoFragment()
                    val arguments = Bundle()
                    arguments.putString("bvId", currentBvId)
                    videoInfoFragment.arguments = arguments
                    videoInfoFragment
                }

                else -> null!!
            }
        }

        fun getTitle(position: Int): Int {
            return when (position) {
                VIDEO -> R.string.title_video
                else -> R.string.title_unknown
            }
        }
    }

    companion object {
        private const val VIDEO = 0
    }
}