package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.SelectPaymentMethodView
import ru.vassuv.blixr.presentation.presenter.template.SelectPaymentMethodPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_select_delivery_method.*
import kotlinx.android.synthetic.main.fragment_select_payment_method.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class SelectPaymentMethodFragment : MvpAppCompatFragment(), SelectPaymentMethodView, IFragment {
    override val type = FrmFabric.SELECT_PAYMENT_METHOD

    companion object {
        fun newInstance(): SelectPaymentMethodFragment {
            val fragment: SelectPaymentMethodFragment = SelectPaymentMethodFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SelectPaymentMethodPresenter

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_payment_method, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cashPay.setOnClickListener(presenter.getCashPayClickListener())
        swishPay.setOnClickListener(presenter.getSwishPayClickListener())
        otherPay.setOnClickListener(presenter.getOtherPayClickListener())
    }
}
