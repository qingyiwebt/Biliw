package el.sft.bw.framework.viewbinding

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BindingLayoutHolder(
    private val view: View,
    private val bindingViewListener: BindingLayoutListener
) :
    RecyclerView.ViewHolder(view) {
    fun rebind(obj: Any) {
        bindingViewListener.onRebind(obj)
    }
}
