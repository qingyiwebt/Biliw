package el.sft.bw.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout

class UserCardComponent :
    ConstraintLayout {

    private val layout: UserCardLayout

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        this.layout = UserCardLayout().also {
            it.attachToParentRequired = true
            it.onCreateLayout(getContext(), this)
        }
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(
        context,
        attributeSet
    ) {
        this.layout = UserCardLayout().also {
            it.attachToParentRequired = true
            it.onCreateLayout(getContext(), this)
        }
    }


    fun rebind(obj: Any) = layout.onRebind(obj)
}