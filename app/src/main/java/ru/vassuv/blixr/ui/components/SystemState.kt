package ru.vassuv.blixr.ui.components

object SystemState {
    var onNavigatorHide: (() -> Unit)? = null
    var onNavigatorShow: (() -> Unit)? = null
}