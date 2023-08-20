package el.sft.bw.framework.viewbinding

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface BindingLayoutListener {
    fun onCreateLayout(context: Context, parent: ViewGroup): View
    fun onRebind(obj: Any)
}