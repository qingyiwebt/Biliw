package el.sft.bw.framework.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import el.sft.bw.framework.components.swipebacklayout.SwipeBackActivityHelper
import el.sft.bw.framework.components.swipebacklayout.SwipeBackLayout
import el.sft.bw.framework.components.swipebacklayout.Utils

/**
 * from https://github.com/ikew0ng/SwipeBackLayout/blob/master/library/src/main/java/me/imid/swipebacklayout/lib/app/SwipeBackActivity.java
 * modified
 */
open class SwipeBackAppCompatActivity() : AppCompatActivity() {
    lateinit var viewHelper: SwipeBackActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHelper = SwipeBackActivityHelper(this)
        viewHelper.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewHelper.onPostCreate()
    }

    open fun getSwipeBackLayout(): SwipeBackLayout {
        return viewHelper.getSwipeBackLayout()
    }

    open fun setSwipeBackEnable(enable: Boolean) {
        getSwipeBackLayout().setEnableGesture(enable)
    }

    open fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        getSwipeBackLayout().scrollToFinishActivity()
    }
}