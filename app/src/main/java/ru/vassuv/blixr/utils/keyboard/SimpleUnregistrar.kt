package ru.vassuv.blixr.utils.keyboard

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver

import java.lang.ref.WeakReference

class SimpleUnregistrar(activity: Activity, globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) : Unregistrar {

    private val mActivityWeakReference: WeakReference<Activity> = WeakReference(activity)

    private val mOnGlobalLayoutListenerWeakReference: WeakReference<ViewTreeObserver.OnGlobalLayoutListener> = WeakReference(globalLayoutListener)

    override fun unregister() {
        val activity = mActivityWeakReference.get()
        val globalLayoutListener = mOnGlobalLayoutListenerWeakReference.get()

        if (null != activity && null != globalLayoutListener) {
            val activityRoot = KeyboardVisibilityEvent.getActivityRoot(activity)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activityRoot.viewTreeObserver
                        .removeOnGlobalLayoutListener(globalLayoutListener)
            } else {
                activityRoot.viewTreeObserver
                        .removeGlobalOnLayoutListener(globalLayoutListener)
            }
        }

        mActivityWeakReference.clear()
        mOnGlobalLayoutListenerWeakReference.clear()
    }

}
