package el.sft.bw.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun AppCompatActivity.finishWithEmptyString(): String {
    this.finish()
    return ""
}

fun Long?.toHumanReadable(): String {
    if (this == null) return "-"
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

fun getAccentColor(context: Context): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(androidx.appcompat.R.attr.colorAccent, typedValue, true);
    return typedValue.data
}

fun getDefaultColor(context: Context): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.colorForeground, typedValue, true);
    return typedValue.data
}

fun setClipboardPlainText(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboardManager.setPrimaryClip(ClipData.newPlainText("Label", text))
}