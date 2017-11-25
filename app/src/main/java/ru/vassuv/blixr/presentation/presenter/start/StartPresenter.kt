package ru.vassuv.blixr.presentation.presenter.start

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.presentation.view.start.StartView
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.ui.activity.MainActivity
import ru.vassuv.blixr.ui.components.SystemState
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class StartPresenter : MvpPresenter<StartView>() {

    var user: User? = DataBase.getUser()

    fun onHandshakeClick() = View.OnClickListener {
        if (user == null) {
            SharedData.LOGIN_TOOLTIP_SHOWED.saveBoolean(false)
            viewState.showMenuTooltip()
        } else {
            viewState.hideHandshakeTooltip()
            Router.navigateTo(FrmFabric.TEMPLATES.name)
        }
    }

    fun onStart() {
        val userUpdate = user ?: DataBase.getUser()
        if (user == null && userUpdate != null) {
            user = userUpdate
//            viewState.hideMenuTooltip()
            viewState.invalidateOptionMenu()
        }

        SystemState.onNavigatorHide = onNavigatorHide
        SystemState.onNavigatorShow = onNavigatorShow
    }

    private val onNavigatorHide = {
        viewState.hideHandshakeTooltip()
        viewState.hideMenuTooltip()
    }

    private val onNavigatorShow = {

    }

    fun onStop() {
        if (SystemState.onNavigatorHide == onNavigatorHide)
            SystemState.onNavigatorHide = null
        if (SystemState.onNavigatorShow == onNavigatorShow)
            SystemState.onNavigatorShow = null
    }
}
