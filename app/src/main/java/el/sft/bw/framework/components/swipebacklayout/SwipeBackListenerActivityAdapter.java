//
// from https://github.com/ikew0ng/SwipeBackLayout/blob/master/library/src/main/java/me/imid/swipebacklayout/lib/app/SwipeBackListenerActivityAdapter.java
//

package el.sft.bw.framework.components.swipebacklayout;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import el.sft.bw.framework.components.swipebacklayout.SwipeBackLayout;
import el.sft.bw.framework.components.swipebacklayout.Utils;

/**
 * Created by laysionqet on 2018/4/24.
 */
public class SwipeBackListenerActivityAdapter implements SwipeBackLayout.SwipeListenerEx {
    private final WeakReference<Activity> mActivity;

    public SwipeBackListenerActivityAdapter(@NonNull Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {

    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        Activity activity = mActivity.get();
        if (null != activity) {
            Utils.convertActivityToTranslucent(activity);
        }
    }

    @Override
    public void onScrollOverThreshold() {

    }

    @Override
    public void onContentViewSwipedBack() {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            activity.finish();
            activity.overridePendingTransition(0, 0);
        }
    }
}