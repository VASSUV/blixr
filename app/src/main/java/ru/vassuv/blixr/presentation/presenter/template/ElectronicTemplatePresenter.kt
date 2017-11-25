package ru.vassuv.blixr.presentation.presenter.template

import android.text.Editable
import android.text.TextWatcher
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.ElectronicTemplateView
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment.*

@InjectViewState
class ElectronicTemplatePresenter : MvpPresenter<ElectronicTemplateView>() {
    private var model: String = ""
    private var mark: String = ""
    private var serialNumber: String = ""
    private var type: String = ""

    val onTypeTextWatcher: TextWatcher
        get() = object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    viewState.setErrorText(ElectronicFields.TYPE, false)
                    if (type.isEmpty()) {
                        viewState.setVisibilityText(ElectronicFields.TYPE, true)
                    }
                }
                if (!type.isEmpty() && s.isEmpty()) {
                    viewState.setVisibilityText(ElectronicFields.TYPE, false)
                }
                type = s.toString().trim { it <= ' ' }
            }
        }

    val onMarkTextWatcher: TextWatcher
        get() = object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    viewState.setErrorText(ElectronicFields.MARK, false)
                    if (mark.isEmpty()) {
                        viewState.setVisibilityText(ElectronicFields.MARK, true)
                    }
                }
                if (!mark.isEmpty() && s.isEmpty()) {
                    viewState.setVisibilityText(ElectronicFields.MARK, false)
                }
                mark = s.toString().trim { it <= ' ' }
            }
        }

    val onModelTextWatcher: TextWatcher
        get() = object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    viewState.setErrorText(ElectronicFields.MODEL, false)
                    if (model.isEmpty()) {
                        viewState.setVisibilityText(ElectronicFields.MODEL, true)
                    }
                }
                if (!model.isEmpty() && s.isEmpty()) {
                    viewState.setVisibilityText(ElectronicFields.MODEL, false)
                }
                model = s.toString().trim { it <= ' ' }
            }
        }

    val onSerialNumberTextWatcher: TextWatcher
        get() = object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    viewState.setErrorText(ElectronicFields.SERIAL_NUMBER, false)
                    if (serialNumber.isEmpty()) {
                        viewState.setVisibilityText(ElectronicFields.SERIAL_NUMBER, true)
                    }
                }
                if (!serialNumber.isEmpty() && s.isEmpty()) {
                    viewState.setVisibilityText(ElectronicFields.SERIAL_NUMBER, false)
                }
                serialNumber = s.toString().trim { it <= ' ' }
            }
        }

   abstract class CustomTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

    }
}
