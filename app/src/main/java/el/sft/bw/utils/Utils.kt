package el.sft.bw.utils

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.finishWithEmptyString(): String {
    this.finish()
    return ""
}

fun Long.toHumanReadable(): String {
    if(this > 10000L) {
        return "${String.format("%.2f", this / 10000f)}万"
    }
    return toString()
}