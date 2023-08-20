package el.sft.bw.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import el.sft.bw.databinding.ActivityMainBinding
import el.sft.bw.fragments.HomePageFragment
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity


class MainActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.pager.isUserInputEnabled = false
        binding.pager.adapter = viewPagerAdapter
    }

    inner class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 1

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                HOME_PAGE -> HomePageFragment()
                else -> null!!
            }
        }
    }

    companion object {
        private const val HOME_PAGE = 0;
        private const val DYNAMIC = 1;
    }
}