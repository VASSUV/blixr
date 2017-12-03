package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.SelectDeliveryMethodView
import ru.vassuv.blixr.presentation.presenter.template.SelectDeliveryMethodPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_select_delivery_method.*
import kotlinx.android.synthetic.main.fragment_select_payment_method.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class SelectDeliveryMethodFragment : MvpAppCompatFragment(), SelectDeliveryMethodView, IFragment {
    override val type = FrmFabric.SELECT_DELIVERY_METHOD

    companion object {
        fun newInstance(): SelectDeliveryMethodFragment {
            val fragment: SelectDeliveryMethodFragment = SelectDeliveryMethodFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SelectDeliveryMethodPresenter

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_delivery_method, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickedUpOnSite.setOnClickListener(presenter.getPickedUpOnSiteClickListener())
        theSellerDeliversToTheBuyer.setOnClickListener(presenter.getTheSellerDeliversToTheBuyerClickListener())
        theSellerSends.setOnClickListener(presenter.getTheSellerSendsClickListener())
        electronically.setOnClickListener(presenter.getElectronicallyClickListener())
    }
}
