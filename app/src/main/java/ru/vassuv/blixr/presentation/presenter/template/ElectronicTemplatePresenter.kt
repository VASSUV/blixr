package ru.vassuv.blixr.presentation.presenter.template

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.ElectronicTemplateView
import ru.vassuv.blixr.ui.components.SystemState
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment.*
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment.ElectronicFields.*
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEventListener
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEvent
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEvent.registerEventListener
import ru.vassuv.blixr.utils.keyboard.Unregistrar





@InjectViewState
class ElectronicTemplatePresenter : MvpPresenter<ElectronicTemplateView>() {
    private var model: String = ""
    private var mark: String = ""
    private var serialNumber: String = ""
    private var type: String = ""
    private var photo: String = ""
    private var state: String = ""
    private var otherInfo: String = ""
    private var methodDelivery: String = ""
    private var dateDelivery: String = ""
    private var methodPayment: String = ""
    private var price: String = ""

    fun onFieldTextWatcher(fieldType: ElectronicFields) = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val trimText = s.toString().trim { it <= ' ' }
            fieldChangeState(s, fieldType, valueIsEmpty(fieldType))
            saveFieldText(fieldType, trimText)
        }
    }

    private fun saveFieldText(fieldType: ElectronicFields, trimText: String) {
        when (fieldType) {
            TYPE -> type = trimText
            MARK -> mark = trimText
            MODEL -> model = trimText
            SERIAL_NUMBER -> serialNumber = trimText
            PHOTO -> photo = trimText
            STATE -> state = trimText
            OTHER_INFO -> otherInfo = trimText
            METHOD_DELIVERY -> methodDelivery = trimText
            DATE_DELIVERY -> dateDelivery = trimText
            METHOD_PAYMENT -> methodPayment = trimText
            PRICE -> price = trimText
            else -> {
            }
        }
    }

    private fun valueIsEmpty(fieldType: ElectronicFields) = when (fieldType) {
        TYPE -> type.isEmpty()
        MARK -> mark.isEmpty()
        MODEL -> model.isEmpty()
        SERIAL_NUMBER -> serialNumber.isEmpty()
        PHOTO -> photo.isEmpty()
        STATE -> state.isEmpty()
        OTHER_INFO -> otherInfo.isEmpty()
        METHOD_DELIVERY -> methodDelivery.isEmpty()
        DATE_DELIVERY -> dateDelivery.isEmpty()
        METHOD_PAYMENT -> methodPayment.isEmpty()
        PRICE -> price.isEmpty()
        else -> false
    }

    private fun fieldChangeState(s: CharSequence, fieldType: ElectronicFields, isEmpty: Boolean) {
        if (s.isNotEmpty()) {
            viewState.setErrorText(fieldType, false)
            if (isEmpty) {
                viewState.setVisibilityText(fieldType, true)
            }
        }
        if (!isEmpty && s.isEmpty()) {
            viewState.setVisibilityText(fieldType, false)
        }
    }

    fun onGaranteeCheck() = CompoundButton.OnCheckedChangeListener { view, isChecked ->

    }

    fun onOriginalCheck() = CompoundButton.OnCheckedChangeListener { view, isChecked ->

    }


    private var unregistrar: Unregistrar? = null

    fun onStart(activity: Activity) {
        unregistrar = registerEventListener(activity, object: KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if(isOpen) {
                    viewState.hidePreview()
                } else {
                    viewState.showPreview()
                }
            }
        })
    }

    fun onStop() {
        unregistrar?.unregister()
    }
}
