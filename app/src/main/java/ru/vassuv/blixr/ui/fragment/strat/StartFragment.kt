package ru.vassuv.blixr.ui.fragment.strat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.strat.StartView
import ru.vassuv.blixr.presentation.presenter.strat.StartPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
