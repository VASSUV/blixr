package ru.vassuv.blixr.ui.components

object SystemState {
    var onNavigatorHide: (() -> Unit)? = null
    var onNavigatorShow: (() -> Unit)? = null
    var onKeyBoardHide: (() -> Unit)? = null
    var onKeyBoardShow: (() -> Unit)? = null
}