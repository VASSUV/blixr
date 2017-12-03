package ru.vassuv.blixr.presentation.presenter.template

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.SelectPaymentMethodView
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class SelectPaymentMethodPresenter : MvpPresenter<SelectPaymentMethodView>() {

    fun onBackPressed() {
        Router.exit()
    }

    fun getCashPayClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodPayment = PaymentMethod.CASH
        onBackPressed()
    }

    fun getSwishPayClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodPayment = PaymentMethod.SWISH
        onBackPressed()
    }

    fun getOtherPayClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.methodPayment = PaymentMethod.OTHER
        onBackPressed()
    }

}
