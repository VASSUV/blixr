package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.BlocketView
import ru.vassuv.blixr.presentation.presenter.template.BlocketPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class BlocketFragment : MvpAppCompatFragment(), BlocketView, IFragment {
    override val type = FrmFabric.BLOCKET

    companion object {
        fun newInstance(): BlocketFragment {
            val fragment = BlocketFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: BlocketPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_blocket, container, false)
        rootView.findViewById<EditText>(R.id.url).addTextChangedListener(presenter.getUrlTextWatcher())
        rootView.findViewById<View>(R.id.button).setOnClickListener(presenter.getOnClickListener())
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun showSuccessAlert() {
    }

    override fun showErrorAlert() {
    }

}
