package ru.vassuv.blixr.presentation.view.start

import com.arellomobile.mvp.MvpView

interface StartView : MvpView {

    fun startLoginActivity()
    fun showMenuTooltip()
    fun hideMenuTooltip()
    fun showHandshakeToolTip()
    fun hideHandshakeTooltip()
    fun invalidateOptionMenu()
}
