package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import el.sft.bw.R
import el.sft.bw.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.pager.adapter = viewPagerAdapter
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.titleBar.text = getString(viewPagerAdapter.getTitle(position))
            }
        })

        binding.titleBar.setOnClickListener {
            if (binding.pager.currentItem != RECOMMENDED_VIDEO)
                binding.pager.setCurrentItem(RECOMMENDED_VIDEO, true)
        }

        return binding.root
    }


    inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                RECOMMENDED_VIDEO -> RecommendedVideosFragment()
                CUSTOM_SOURCE -> CustomSourceFragment()
                ACCOUNT -> AccountFragment()
                else -> null!!
            }
        }

        fun getTitle(position: Int): Int {
            return when (position) {
                RECOMMENDED_VIDEO -> R.string.title_recommended_videos
                CUSTOM_SOURCE -> R.string.title_custom_source
                ACCOUNT -> R.string.title_account
                else -> R.string.title_unknown
            }
        }
    }

    companion object {
        private const val RECOMMENDED_VIDEO = 0
        private const val CUSTOM_SOURCE = 1
        private const val ACCOUNT = 2
    }
}