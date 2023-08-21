package el.sft.bw.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

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

fun setClipboardPlainText(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    clipboardManager.setPrimaryClip(ClipData.newPlainText("Label", text))
}