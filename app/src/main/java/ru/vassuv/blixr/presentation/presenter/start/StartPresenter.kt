package ru.vassuv.blixr.presentation.presenter.start

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.presentation.view.start.StartView
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
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

        SystemState.onNavigatorDragging = onNavigatorDragging
        SystemState.onNavigatorEdle = onNavigatorEdle
    }

    private val onNavigatorDragging = {
        viewState.hideHandshakeTooltip()
        viewState.hideMenuTooltip()
    }

    private val onNavigatorEdle = {

    }

    fun onStop() {
        if (SystemState.onNavigatorDragging == onNavigatorDragging)
            SystemState.onNavigatorDragging = null
        if (SystemState.onNavigatorEdle == onNavigatorEdle)
            SystemState.onNavigatorEdle = null
    }
}
