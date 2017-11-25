package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.EventTemplateView
import ru.vassuv.blixr.presentation.presenter.template.EventTemplatePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class EventTemplateFragment : MvpAppCompatFragment(), EventTemplateView, IFragment{
    override val type = FrmFabric.EVENT_TEMPLATE

    companion object {
        fun newInstance(): EventTemplateFragment {
            val fragment: EventTemplateFragment = EventTemplateFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: EventTemplatePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_template, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
