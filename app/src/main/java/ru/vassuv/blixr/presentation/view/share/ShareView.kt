package ru.vassuv.blixr.presentation.view.share

import com.arellomobile.mvp.MvpView

interface ShareView : MvpView {
    fun showSuccessAlert()
    fun showErrorAlert()
}
