package el.sft.bw.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import coil.load
import el.sft.bw.R
import el.sft.bw.databinding.ActivityMainBinding
import el.sft.bw.fragments.FavListFragment
import el.sft.bw.fragments.HomePageFragment
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.network.ApiClient
import el.sft.bw.network.dto.NavResponse
import el.sft.bw.utils.LocalBroadcastUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : SwipeBackAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var broadcastManager: LocalBroadcastManager
    private val broadcastReceiver: LocalBroadcastReceiver = LocalBroadcastReceiver()
    private var isLoggedIn: Boolean? = null
    private var userData: NavResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.root.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
                setSwipeBackEnable(false)
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                setSwipeBackEnable(true)
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        binding.userCard.setOnClickListener {
            if (isLoggedIn == true) {
                Intent(this, UserCenterActivity::class.java).also {
                    it.putExtra("userId", userData!!.mid!!)
                    startActivity(it)
                }
            } else {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.pager.isUserInputEnabled = false
        binding.pager.adapter = viewPagerAdapter

        broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager.registerReceiver(
            broadcastReceiver,
            IntentFilter(LocalBroadcastUtils.ACTION_OPEN_DRAWER)
        )

        broadcastManager.registerReceiver(
            broadcastReceiver,
            IntentFilter(LocalBroadcastUtils.ACTION_ACCOUNT_CHANGED)
        )

        binding.gotoHomePage.setOnClickListener { gotoFragment(HOME_PAGE) }
        binding.gotoFavorite.setOnClickListener { gotoFragment(FAV_LIST) }

        requestReloadAccount()
    }

    private fun gotoFragment(index: Int) {
        binding.pager.setCurrentItem(index, false)
        binding.root.closeDrawers()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestReloadAccount() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                val res = ApiClient.getUserInfoFromNav()

                withContext(Dispatchers.Main) {
                    userData = res.data!!
                    isLoggedIn = userData!!.isLogin

                    reloadAccount()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(
                            this@MainActivity,
                            getText(R.string.error_load_failed),
                            Toast.LENGTH_LONG
                        )
                        .show()
                    reloadAccount()
                }
            }
        }
    }

    private fun reloadAccount() {
        when (isLoggedIn) {
            true -> {
                binding.uploaderName.text = userData!!.uname
                binding.coinCount.text = userData!!.money.toString()
                binding.avatarImage.load(userData!!.face)
            }

            false -> {
                val str = getText(R.string.non_login)
                binding.uploaderName.text = str
                binding.coinCount.text = str
            }

            else -> {
                val str = getText(R.string.error_load_failed)
                binding.uploaderName.text = str
                binding.coinCount.text = str
            }
        }
    }


    inner class LocalBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                LocalBroadcastUtils.ACTION_OPEN_DRAWER -> {
                    binding.root.openDrawer(binding.drawer)
                }

                LocalBroadcastUtils.ACTION_ACCOUNT_CHANGED -> {
                    requestReloadAccount()
                }
            }
        }
    }

    inner class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                HOME_PAGE -> HomePageFragment()
                FAV_LIST -> FavListFragment()
                else -> null!!
            }
        }
    }

    companion object {
        private const val HOME_PAGE = 0
        private const val FAV_LIST = 1
        private const val DYNAMIC = 2
    }
}