package ru.vassuv.blixr.presentation.presenter.strat

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tooltip.Tooltip
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.strat.StartView
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.ui.activity.MainActivity
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class StartPresenter : MvpPresenter<StartView>() {
    private var handshakeTooltip: Tooltip? = null

    private var user: User? = null

    fun onHandshakeClick(activity: MainActivity?) = View.OnClickListener {

        if (user == null) {
            SharedData.LOGIN_TOOLTIP_SHOWED.saveBoolean(false)
            activity?.showLoginTooltip()
        } else {
            if (handshakeTooltip == null) {
                hideTooltip()
            }
            Router.navigateTo(FrmFabric.TEMPLATES.name)
        }
    }

    fun onStart(tooltipView: View) {
        user = DataBase.getUser()
        showToolTip(tooltipView)
    }

    fun onStop() {
        hideTooltip()
    }

    private fun showToolTip(view: View) {
        if (user != null && handshakeTooltip == null && !SharedData.SEARCH_TOOLTIP_SHOWED.getBoolean()) {
            handshakeTooltip = Tooltip.Builder(view, R.style.AppTheme)
                    .setCornerRadius(R.dimen.tooltipRadius)
                    .setBackgroundColor(view.context.resources.getColor(R.color.tooltipColor))
                    .setText(R.string.search_here)
                    .show()
            SharedData.SEARCH_TOOLTIP_SHOWED.saveBoolean(true)
        }
    }
    private fun hideTooltip() {
        handshakeTooltip?.dismiss()
    }
}
