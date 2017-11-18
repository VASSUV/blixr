package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.TemplateView
import ru.vassuv.blixr.presentation.presenter.template.TemplatePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class TemplateFragment : MvpAppCompatFragment(), TemplateView, IFragment {
    override val type = FrmFabric.TEMPLATES

    companion object {
        fun newInstance(): TemplateFragment {
            val fragment = TemplateFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: TemplatePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_template, container, false)
        rootView.findViewById<View>(R.id.electronics).setOnClickListener(presenter.getElectronicsOnClickListener())
        rootView.findViewById<View>(R.id.instruments).setOnClickListener(presenter.getInstrumentsOnClickListener())
        rootView.findViewById<View>(R.id.event_tickets).setOnClickListener(presenter.getEventTicketsOnClickListener())
        rootView.findViewById<View>(R.id.other).setOnClickListener(presenter.getOtherOnClickListener())
        rootView.findViewById<View>(R.id.blocket_link).setOnClickListener(presenter.getBlocketLinkOnClickListener())
        rootView.findViewById<View>(R.id.more_templates_on_the_way).setOnClickListener(presenter.getMoreTemplatesOnClickListener())
        return rootView
    }
}
