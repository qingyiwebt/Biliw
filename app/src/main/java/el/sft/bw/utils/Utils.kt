package el.sft.bw.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun AppCompatActivity.finishWithEmptyString(): String {
    this.finish()
    return ""
}

fun Long.toHumanReadable(): String {
    if (this > 10000L) {
        return "${String.format("%.2f", this / 10000f)}万"
    }
    return toString()
}

suspend fun runWithFragment(fragment: Fragment, action: CoroutineScope.() -> Unit) {
    if (!fragment.isAdded) return
    withContext(Dispatchers.Main, action)
}


suspend fun runWithActivity(activity: Activity, action: CoroutineScope.() -> Unit) {
    if (activity.isDestroyed) return
    withContext(Dispatchers.Main, action)
}

fun setClipboardPlainText(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboardManager.setPrimaryClip(ClipData.newPlainText("Label", text))
}