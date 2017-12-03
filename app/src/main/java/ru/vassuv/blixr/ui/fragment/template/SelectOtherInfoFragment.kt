package ru.vassuv.blixr.ui.fragment.template

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.os.Handler
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_select_other_info.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.presenter.template.SelectOtherInfoPresenter
import ru.vassuv.blixr.presentation.view.template.SelectOtherInfoView
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEvent
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEventListener
import ru.vassuv.blixr.utils.keyboard.Unregistrar
import ru.vassuv.blixr.utils.keyboard.showKeyboard

class SelectOtherInfoFragment : MvpAppCompatFragment(), SelectOtherInfoView, IFragment {
    override val type = FrmFabric.SELECT_OTHER_INFO

    companion object {
        fun newInstance(): SelectOtherInfoFragment {
            val fragment: SelectOtherInfoFragment = SelectOtherInfoFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SelectOtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_other_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText.addTextChangedListener(presenter.getTextChangeListener())
    }

    private var unregistrar: Unregistrar? = null

    override fun onStart() {
        super.onStart()
        editText.requestFocus()
        Handler().postDelayed({
            showKeyboard(activity, editText)
        }, 500)
        unregistrar = KeyboardVisibilityEvent.registerEventListener(activity, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (!isOpen) {
                    onBackPressed()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.apply, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onApplyClick()
        return true
    }

    override fun onStop() {
        super.onStop()
        unregistrar?.unregister()
    }
}
