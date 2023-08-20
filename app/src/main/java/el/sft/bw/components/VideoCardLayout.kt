package el.sft.bw.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import el.sft.bw.databinding.LayoutVideoCardBinding
import el.sft.bw.framework.viewbinding.BindingLayout
import el.sft.bw.utils.toHumanReadable

class VideoCardLayout : BindingLayout() {
    private lateinit var binding: LayoutVideoCardBinding

    override fun onCreateLayout(context: Context, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        binding = LayoutVideoCardBinding.inflate(layoutInflater, parent, false)

        return binding.root
    }

    override fun onRebind(obj: Any) {
        val typedObj = obj as VideoCardData
        binding.title.text = typedObj.title
        binding.author.text = typedObj.author
        binding.viewCount.text = typedObj.playCount.toHumanReadable()
        binding.cover.load(typedObj.cover)
    }
}