package ru.vassuv.blixr.ui.components

import android.view.ViewGroup
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.view.ViewTreeObserver
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window


open class KeyboardListenerActivity : AppCompatActivity() {
    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        if (rootLayout == null) return@OnGlobalLayoutListener

        val heightDiff = rootLayout!!.rootView.height - rootLayout!!.height
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top

        val broadcastManager = LocalBroadcastManager.getInstance(this@KeyboardListenerActivity)

        if (heightDiff <= contentViewTop) {
            SystemState.onKeyBoardHide?.invoke()
            val intent = Intent("KeyboardWillHide")
            broadcastManager.sendBroadcast(intent)
        } else {
            val keyboardHeight = heightDiff - contentViewTop
            SystemState.onKeyBoardShow?.invoke()

            val intent = Intent("KeyboardWillShow")
            intent.putExtra("KeyboardHeight", keyboardHeight)
            broadcastManager.sendBroadcast(intent)
        }
    }

    private var keyboardListenersAttached = false
    private var rootLayout : ViewGroup? = null

    protected fun attachKeyboardListeners(rootView: ViewGroup) {
        if (keyboardListenersAttached) {
            return
        }

        rootLayout = rootView

        rootLayout!!.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)

        keyboardListenersAttached = true
    }

    override fun onDestroy() {
        super.onDestroy()

        if (keyboardListenersAttached) {
            rootLayout!!.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }
}