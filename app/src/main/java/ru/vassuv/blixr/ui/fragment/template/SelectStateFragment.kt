package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.SelectStateView
import ru.vassuv.blixr.presentation.presenter.template.SelectStatePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_select_state.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class SelectStateFragment : MvpAppCompatFragment(), SelectStateView, IFragment {
    override val type = FrmFabric.SELECT_STATE

    companion object {
        fun newInstance(): SelectStateFragment {
            val fragment = SelectStateFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SelectStatePresenter

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_state, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asNew.setOnClickListener(presenter.getAsNewClickListener())
        good.setOnClickListener(presenter.getGoodClickListener())
        used.setOnClickListener(presenter.getUsedClickListener())
    }
}
