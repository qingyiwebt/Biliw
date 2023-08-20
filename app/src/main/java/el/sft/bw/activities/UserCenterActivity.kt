package el.sft.bw.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import el.sft.bw.R
import el.sft.bw.databinding.ActivityUserCenterBinding
import el.sft.bw.fragments.UserInfoFragment
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity

class UserCenterActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivityUserCenterBinding
    private val viewPagerAdapter = ViewPagerAdapter(this)

    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.let {
            userId = intent.getLongExtra("userId", -1)
            if (userId == -1L) finish()
        }

        binding.pager.adapter = viewPagerAdapter
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.titleBar.text = getString(viewPagerAdapter.getTitle(position))
            }
        })

        binding.titleBar.setOnClickListener {
            if (binding.pager.currentItem != USER_INFO)
                binding.pager.setCurrentItem(USER_INFO, true)
            else finish()
        }
    }

    inner class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 1
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                USER_INFO -> {
                    val fragment = UserInfoFragment()
                    val arguments = Bundle()
                    arguments.putLong("userId", userId)
                    fragment.arguments = arguments
                    fragment
                }

                else -> null!!
            }
        }

        fun getTitle(position: Int): Int {
            return when (position) {
                USER_INFO -> R.string.title_user_info
                else -> R.string.title_unknown
            }
        }
    }

    companion object {
        private const val USER_INFO = 0
        private const val POSTS = 1
        private const val DYNAMIC = 2
    }
}