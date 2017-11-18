package ru.vassuv.blixr.presentation.presenter.template

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.TemplateView

@InjectViewState
class TemplatePresenter : MvpPresenter<TemplateView>() {
    fun getElectronicsOnClickListener() = View.OnClickListener {

    }

    fun getInstrumentsOnClickListener() = View.OnClickListener {

    }

    fun getEventTicketsOnClickListener() = View.OnClickListener {

    }

    fun getOtherOnClickListener() = View.OnClickListener {

    }

    fun getBlocketLinkOnClickListener() = View.OnClickListener {

    }

    fun getMoreTemplatesOnClickListener() = View.OnClickListener {

    }

}
