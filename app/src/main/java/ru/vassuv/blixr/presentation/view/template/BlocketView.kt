package ru.vassuv.blixr.presentation.view.template

import com.arellomobile.mvp.MvpView

interface BlocketView : MvpView {
    fun showSuccessAlert()
    fun showErrorAlert()

}
