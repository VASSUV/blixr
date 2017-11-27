package ru.vassuv.blixr.utils.keyboard

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

import android.view.WindowManager

import ru.vassuv.blixr.utils.keyboard.convertDpToPx

object KeyboardVisibilityEvent {

    private val KEYBOARD_VISIBLE_THRESHOLD_DP = 100

//    fun setEventListener(activity: Activity,
//                         listener: KeyboardVisibilityEventListener) {
//
//        val unregistrar = registerEventListener(activity, listener)
//        activity.application
//                .registerActivityLifecycleCallbacks(object : AutoActivityLifecycleCallback(activity) {
//                    override fun onTargetActivityDestroyed() {
//                        unregistrar.unregister()
//                    }
//                })
//    }

    fun registerEventListener(activity: Activity?,
                              listener: KeyboardVisibilityEventListener?): Unregistrar {

        if (activity == null) {
            throw NullPointerException("Parameter:activity must not be null")
        }

        val softInputMethod = activity.window.attributes.softInputMode
        if (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE != softInputMethod && WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED != softInputMethod) {
            throw IllegalArgumentException("Parameter:activity window SoftInputMethod is not ADJUST_RESIZE")
        }

        if (listener == null) {
            throw NullPointerException("Parameter:listener must not be null")
        }

        val activityRoot = getActivityRoot(activity)

        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

            private val r = Rect()

            private val visibleThreshold = Math.round(
                    convertDpToPx(activity, KEYBOARD_VISIBLE_THRESHOLD_DP.toFloat()))

            private var wasOpened = false

            override fun onGlobalLayout() {
                activityRoot.getWindowVisibleDisplayFrame(r)

                val heightDiff = activityRoot.rootView.height - r.height()

                val isOpen = heightDiff > visibleThreshold

                if (isOpen == wasOpened) {
                    // keyboard state has not changed
                    return
                }

                wasOpened = isOpen

                listener.onVisibilityChanged(isOpen)
            }
        }
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        return SimpleUnregistrar(activity, layoutListener)
    }

    fun isKeyboardVisible(activity: Activity): Boolean {
        val r = Rect()

        val activityRoot = getActivityRoot(activity)
        val visibleThreshold = Math.round(convertDpToPx(activity, KEYBOARD_VISIBLE_THRESHOLD_DP.toFloat()))

        activityRoot.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold
    }

    fun getActivityRoot(activity: Activity)
            = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
}
