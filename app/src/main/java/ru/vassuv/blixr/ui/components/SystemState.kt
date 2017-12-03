package ru.vassuv.blixr.ui.components

import android.os.Handler
import android.view.View

object SystemState {
    var onNavigatorDragging: (() -> Unit)? = null
    var onNavigatorEdle: (() -> Unit)? = null
    var isNavigatorVisible: Boolean = false

    var loader: Loader? = null
}

class Loader(val loader: View) {
    var visibility: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                if (value) {
                    loader.visibility = View.VISIBLE
                } else {
                    Handler().postDelayed({ loader.visibility = View.GONE }, 500)
                }
            }
        }
}