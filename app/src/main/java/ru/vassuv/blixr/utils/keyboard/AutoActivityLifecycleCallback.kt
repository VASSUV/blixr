package ru.vassuv.blixr.utils.keyboard

import android.app.Activity
import android.app.Application
import android.os.Bundle

abstract class AutoActivityLifecycleCallback(private val mTargetActivity: Activity) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle) {    }

    override fun onActivityStarted(activity: Activity) {    }

    override fun onActivityResumed(activity: Activity) {    }

    override fun onActivityPaused(activity: Activity) {    }

    override fun onActivityStopped(activity: Activity) {    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity === mTargetActivity) {
            mTargetActivity.application.unregisterActivityLifecycleCallbacks(this)
            onTargetActivityDestroyed()
        }
    }

    protected abstract fun onTargetActivityDestroyed()
}