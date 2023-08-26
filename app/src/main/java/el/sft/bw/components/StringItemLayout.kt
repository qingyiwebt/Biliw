package el.sft.bw.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import el.sft.bw.R
import el.sft.bw.framework.viewbinding.BindingLayout

class StringItemLayout : BindingLayout() {
    private lateinit var view: TextView

    override fun onCreateLayout(context: Context, parent: ViewGroup): View {
        view = LayoutInflater.from(context)
            .inflate(R.layout.layout_simple_text_1, parent, false) as TextView
        return view
    }

    override fun onRebind(obj: Any) {
        view.text = obj.toString()
    }
}