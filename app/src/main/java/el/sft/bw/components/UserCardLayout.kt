package el.sft.bw.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import el.sft.bw.databinding.LayoutUserCardBinding
import el.sft.bw.framework.viewbinding.BindingLayout
import el.sft.bw.utils.toHumanReadable

class UserCardLayout : BindingLayout() {
    private lateinit var binding: LayoutUserCardBinding
    var attachToParentRequired = false

    override fun onCreateLayout(context: Context, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        binding = LayoutUserCardBinding.inflate(inflater, parent, attachToParentRequired)

        return binding.root
    }

    override fun onRebind(obj: Any) {
        val typedObj = obj as UserCardData
        binding.uploaderName.text = typedObj.name
        binding.avatarImage.load(typedObj.avatar)
        binding.followerCount.text = typedObj.followerCount.toHumanReadable()
    }
}