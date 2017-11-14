package ru.vassuv.blixr.presentation.presenter.strat

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.strat.StartView
import ru.vassuv.blixr.repository.db.DataBase

@InjectViewState
class StartPresenter : MvpPresenter <StartView>() {
    fun onHandshakeClick() {
        val user = DataBase.getUser()
        if (user == null) {
            viewState.startLoginActivity()
        } else {
            viewState.startLoginActivity()
        }
    }
}
