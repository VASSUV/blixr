package ru.vassuv.blixr.utils.keyboard

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun convertDpToPx(context: Context, dp: Float): Float {
    val res = context.resources

    return dp * (res.displayMetrics.densityDpi / 160f)
}

fun showKeyboard(context: Context?, target: EditText?) {
    if (context == null || target == null) {
        return
    }

    val imm = getInputMethodManager(context)

    imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
}

fun showKeyboardInDialog(dialog: Dialog?, target: EditText?) {
    if (dialog == null || target == null) {
        return
    }

    dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    target.requestFocus()
}

fun hideKeyboard(context: Context?, target: View?) {
    if (context == null || target == null) {
        return
    }

    val imm = getInputMethodManager(context)
    imm.hideSoftInputFromWindow(target.windowToken, 0)
}

fun hideKeyboard(activity: Activity) {
    val view = activity.window.decorView

    if (view != null) {
        hideKeyboard(activity, view)
    }
}

private fun getInputMethodManager(context: Context): InputMethodManager {
    return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}