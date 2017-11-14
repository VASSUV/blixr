package ru.vassuv.blixr.presentation.view.auth

import android.content.Intent
import com.arellomobile.mvp.MvpView

interface SearchView : MvpView {
    fun startActivityForResult(intent: Intent, requestNumber: Int)
}
