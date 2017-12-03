package ru.vassuv.blixr.presentation.view.template

import com.arellomobile.mvp.MvpView

interface SelectPhotosView : MvpView {
    fun launchGallery(READ_REQUEST_CODE: Int)

}
