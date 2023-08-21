package el.sft.bw.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import el.sft.bw.framework.viewbinding.BindingLayout
import el.sft.bw.network.model.FavListModel

class FavListItemLayout : BindingLayout() {
    private lateinit var view: TextView

    override fun onCreateLayout(context: Context, parent: ViewGroup): View {
        view = LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return view
    }

    override fun onRebind(obj: Any) {
        val typedObj = obj as FavListModel
        view.text = typedObj.title
    }
}