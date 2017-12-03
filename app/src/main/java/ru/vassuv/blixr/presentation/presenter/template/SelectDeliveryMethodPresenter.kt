package ru.vassuv.blixr.presentation.presenter.template

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.SelectDeliveryMethodView
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class SelectDeliveryMethodPresenter : MvpPresenter<SelectDeliveryMethodView>() {

    fun onBackPressed() {
        Router.exit()
    }

    fun getPickedUpOnSiteClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodDelivery = DeliveryMethod.PICKED_UP_ON_SITE
                onBackPressed()
    }

    fun getTheSellerDeliversToTheBuyerClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodDelivery = DeliveryMethod.THE_SELLER_DELIVERS_TO_THE_BUYER
        onBackPressed()
    }

    fun getTheSellerSendsClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodDelivery = DeliveryMethod.THE_SELLER_SENDS
        onBackPressed()
    }

    fun getElectronicallyClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodDelivery = DeliveryMethod.ELECTRONICALLY
        onBackPressed()
    }
}
