package el.sft.bw.activities

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import el.sft.bw.R
import el.sft.bw.databinding.ActivityLoginBinding
import el.sft.bw.framework.activities.SwipeBackAppCompatActivity
import el.sft.bw.network.ApiClient
import el.sft.bw.network.dto.QrCodeLoginResponse
import el.sft.bw.utils.LocalBroadcastUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : SwipeBackAppCompatActivity() {
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var binding: ActivityLoginBinding
    private var qrCodeLoginResponse: QrCodeLoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        broadcastManager = LocalBroadcastManager.getInstance(this)

        binding.finishLoginButton.setOnClickListener {
            requestFinishLogin()
        }

        requestLoginQrCode()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestFinishLogin() {
        binding.finishLoginButton.isEnabled = false
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val res = ApiClient.finishQrCodeLogin(qrCodeLoginResponse!!.authKey)

                withContext(Dispatchers.Main) {
                    if (res.data is Double) {
                        val errorString = when (res.data) {
                            -1.0 -> R.string.error_login_incorrect_authkey
                            -2.0 -> R.string.error_login_timeout
                            -4.0 -> R.string.error_login_not_be_scanned
                            -5.0 -> R.string.error_login_unconfirmed
                            else -> R.string.error_unknown
                        }
                        Toast
                            .makeText(this@LoginActivity, errorString, Toast.LENGTH_LONG)
                            .show()

                        return@withContext
                    }

                    if (res.data != null) {
                        Toast
                            .makeText(applicationContext, R.string.finish_login, Toast.LENGTH_LONG)
                            .show()

                        broadcastManager.sendBroadcast(Intent(LocalBroadcastUtils.ACCOUNT_CHANGED))
                        finish()
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(this@LoginActivity, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.finishLoginButton.isEnabled = true
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestLoginQrCode() {
        binding.finishLoginButton.isEnabled = false
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val res = ApiClient.requestQrCodeLogin()
                val data = res.data!!
                qrCodeLoginResponse = data
                withContext(Dispatchers.Main) {
                    val barcodeEncoder = BarcodeEncoder()
                    val bitmap: Bitmap =
                        barcodeEncoder.encodeBitmap(data.url, BarcodeFormat.QR_CODE, 400, 400)
                    binding.qrCode.setImageBitmap(bitmap);
                    binding.finishLoginButton.isEnabled = true
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(this@LoginActivity, R.string.error_load_failed, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}