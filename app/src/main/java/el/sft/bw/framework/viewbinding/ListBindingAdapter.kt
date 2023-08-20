package el.sft.bw.framework.viewbinding

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListBindingAdapter<TData : Any, TView : BindingLayoutListener>(
    private val dataSet: List<TData>,
    private val factory: () -> TView
) : RecyclerView.Adapter<BindingLayoutHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingLayoutHolder {
        val bindingView = factory()
        val view = bindingView.onCreateLayout(parent.context, parent)
        return BindingLayoutHolder(view, bindingView)
    }


    override fun getItemCount(): Int = dataSet.size;

    override fun onBindViewHolder(holder: BindingLayoutHolder, position: Int) {
        val data = dataSet[position]
        holder.rebind(data)
    }
}