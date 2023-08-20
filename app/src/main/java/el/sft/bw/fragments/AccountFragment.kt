package el.sft.bw.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import el.sft.bw.R
import el.sft.bw.activities.LoginActivity
import el.sft.bw.databinding.FragmentAccountBinding
import el.sft.bw.framework.components.ScrollableFragment
import el.sft.bw.network.ApiClient
import el.sft.bw.utils.LocalBroadcastUtils
import el.sft.bw.utils.PrefsUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : ScrollableFragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var broadcastManager: LocalBroadcastManager
    private val broadcastReceiver: LocalBroadcastReceiver = LocalBroadcastReceiver()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val intent =
                Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            PrefsUtils.currentCookieJar.clearCookies()
            broadcastManager.sendBroadcast(Intent(LocalBroadcastUtils.ACCOUNT_CHANGED))
        }

        broadcastManager = LocalBroadcastManager.getInstance(this.requireContext())
        broadcastManager.registerReceiver(
            broadcastReceiver,
            IntentFilter(LocalBroadcastUtils.ACCOUNT_CHANGED)
        )

        requestReloadAccount()

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestReloadAccount() {
        binding.loadingPanel.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            try {
                ApiClient.reloadCookie();
                val res = ApiClient.getUserInfoFromNav()

                withContext(Dispatchers.Main) {
                    val data = res.data!!
                    val loggedIn = data.isLogin!!
                    binding.nonLoginPanel.visibility = if (loggedIn) View.GONE else View.VISIBLE
                    binding.loginPanel.visibility = if (!loggedIn) View.GONE else View.VISIBLE

                    if (!loggedIn) return@withContext
                    binding.accountText.text = data.uname!!
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    val ctx = requireContext()
                    Toast
                        .makeText(ctx, ctx.getText(R.string.error_load_failed), Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.loadingPanel.visibility = View.GONE
                }
            }
        }
    }

    inner class LocalBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocalBroadcastUtils.ACCOUNT_CHANGED) {
                requestReloadAccount()
            }
        }
    }
}