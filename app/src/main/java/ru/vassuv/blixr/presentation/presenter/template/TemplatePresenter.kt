package ru.vassuv.blixr.presentation.presenter.template

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.presentation.view.template.TemplateView
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class TemplatePresenter : MvpPresenter<TemplateView>() {
    fun getElectronicsOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.ELECTRONIC_TEMPLATE.name)
    }

    fun getInstrumentsOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.ELECTRONIC_TEMPLATE.name)
    }

    fun getEventTicketsOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.ELECTRONIC_TEMPLATE.name)
    }

    fun getOtherOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.ELECTRONIC_TEMPLATE.name)
    }

    fun getBlocketLinkOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.BLOCKET.name)
    }

    fun getMoreTemplatesOnClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.ELECTRONIC_TEMPLATE.name)
    }
}
