package ru.vassuv.blixr.ui.components

import android.view.View

object SystemState {
    var onNavigatorHide: (() -> Unit)? = null
    var onNavigatorShow: (() -> Unit)? = null

    var loader: Loader? = null
}

class Loader(val loader: View) {
    var visibility: Boolean = false
        set(value) {
        if(value != field) {
            field = value
            loader.visibility = if (value) View.VISIBLE else View.GONE
        }
    }
}