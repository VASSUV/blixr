package ru.vassuv.blixr.ui.fragment.strat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.strat.StartView
import ru.vassuv.blixr.presentation.presenter.strat.StartPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.kittinunf.fuel.httpGet
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.BankId
import ru.vassuv.blixr.utils.verifyResult

class StartFragment : MvpAppCompatFragment(), IFragment, StartView {

    override val type: FragmentFabric = FrmFabric.MAIN

    companion object {

        const val TAG = "StartFragment"

        fun newInstance(): StartFragment {
            val fragment: StartFragment = StartFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mStartPresenter: StartPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onStart() {
        super.onStart()

    }
}