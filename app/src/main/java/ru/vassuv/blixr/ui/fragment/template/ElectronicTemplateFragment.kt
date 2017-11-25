package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.ElectronicTemplateView
import ru.vassuv.blixr.presentation.presenter.template.ElectronicTemplatePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_electronic_template.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.KeyboardUtils.hideKeyboard
import ru.vassuv.blixr.utils.KeyboardUtils.showKeyboard

class ElectronicTemplateFragment : MvpAppCompatFragment(), ElectronicTemplateView, IFragment {
    override val type = FrmFabric.ELECTRONIC_TEMPLATE

    companion object {
        fun newInstance(): ElectronicTemplateFragment {
            val fragment: ElectronicTemplateFragment = ElectronicTemplateFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ElectronicTemplatePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_electronic_template, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type_name.addTextChangedListener(presenter.onTypeTextWatcher)
        mark.addTextChangedListener(presenter.onMarkTextWatcher)
        model.addTextChangedListener(presenter.onModelTextWatcher)
        serial_number.addTextChangedListener(presenter.onSerialNumberTextWatcher)
    }


    override fun setVisibilityText(field: ElectronicFields, visibility: Boolean) {
        val visible = if (visibility) View.VISIBLE else View.INVISIBLE
        when (field) {
            ElectronicFields.TYPE -> {
                type_text.visibility = visible
                type_text.isChecked = false
            }
            ElectronicFields.MARK -> {
                mark_text.visibility = visible
                mark_text.isChecked = false
            }
            ElectronicFields.MODEL -> model_text.visibility = visible
            ElectronicFields.SERIAL_NUMBER -> serial_number_text.visibility = visible
            else -> {
            }
        }
    }

    override fun setErrorText(field: ElectronicFields, errorVisibility: Boolean) {
        when (field) {
            ElectronicFields.TYPE -> {
                type_text.visibility = if (!errorVisibility && type_text.text.isEmpty()) View.INVISIBLE else View.VISIBLE
                type_text.isChecked = errorVisibility
            }
            ElectronicFields.MARK -> {
                mark_text.visibility = if (!errorVisibility && mark.text.isEmpty()) View.INVISIBLE else View.VISIBLE
                mark_text.isChecked = errorVisibility
            }
            ElectronicFields.MODEL -> {
                model_text.visibility = if (!errorVisibility && model.text.isEmpty()) View.INVISIBLE else View.VISIBLE
                model_text.isChecked = errorVisibility
            }
            ElectronicFields.SERIAL_NUMBER -> {
                serial_number_text.visibility = if (!errorVisibility && serial_number.text.isEmpty()) View.INVISIBLE else View.VISIBLE
                serial_number_text.isChecked = errorVisibility
            }
            else -> {
            }
        }
    }

    override fun setFocus(field: ElectronicFields, showKeyboard: Boolean) {
        when (field) {
            ElectronicFields.TYPE -> {
                type_name.isFocusable = true
//                scrollTo(type_block)
            }
            ElectronicFields.MARK -> {
                mark.isFocusable = true
                mark.requestFocus()
//                scrollTo(type_block)
            }
            ElectronicFields.MODEL -> {
                model.isFocusable = true
                model.requestFocus()
//                scrollTo(model_block)
            }
            ElectronicFields.SERIAL_NUMBER -> {
                serial_number.isFocusable = true
                serial_number.requestFocus()
//                scrollTo(serial_number_block)
            }
        }

        if (showKeyboard) {
//            changeKeyboardVisibility(true)
        }
    }
//
//    private fun scrollTo(element: View?) {
//        scroll_view.scrollTo(0, element!!.top)
//    }

    override fun changeKeyboardVisibility(visibility: Boolean) {
        if (visibility) {
            showKeyboard(activity)
        } else {
            hideKeyboard(activity)
        }
    }

    override fun setType(value: String) {
        type_name.setText(value)
        setVisibilityText(ElectronicFields.TYPE, !value.isEmpty())
    }

    override fun setMark(value: String) {
        mark.setText(value)
        setVisibilityText(ElectronicFields.MARK, !value.isEmpty())
    }

    override fun setModel(value: String) {
        model.setText(value)
        setVisibilityText(ElectronicFields.MODEL, !value.isEmpty())
    }

    override fun setOther(value: String) {
        serial_number.setText(value)
        setVisibilityText(ElectronicFields.SERIAL_NUMBER, !value.isEmpty())
    }

    enum class ElectronicFields {
        TYPE,
        MARK,
        MODEL,
        SERIAL_NUMBER
    }
}
