package ru.vassuv.blixr.presentation.presenter.template

import android.app.Activity
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.App
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.ElectronicTemplateView
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment.ElectronicFields
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment.ElectronicFields.*
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEvent.registerEventListener
import ru.vassuv.blixr.utils.keyboard.KeyboardVisibilityEventListener
import ru.vassuv.blixr.utils.keyboard.Unregistrar

enum class ElectronicState(val textForUser: String, val textForTemplate: String) {
    AS_NEW(App.context.getString(R.string.as_new), "Nyskick"),
    GOOD(App.context.getString(R.string.good), "Gott skick"),
    USED(App.context.getString(R.string.used), "Använd"),
    NOTHING("", "");
}

enum class DeliveryMethod(val textForUser: String, val textForTemplate: String) {
    PICKED_UP_ON_SITE(App.context.getString(R.string.picked_up_on_site), "Hämtas på plats"),
    THE_SELLER_DELIVERS_TO_THE_BUYER(App.context.getString(R.string.the_seller_delivers_to_the_buyer), "Säljaren levererar till köparenskick"),
    THE_SELLER_SENDS(App.context.getString(R.string.the_seller_sends), "Säljaren skickar"),
    ELECTRONICALLY(App.context.getString(R.string.electronically), "Elektroniskt"),
    NOTHING("", "");
}

enum class PaymentMethod(val textForUser: String, val textForTemplate: String) {
    CASH(App.context.getString(R.string.cash_pay), "Kontant"),
    SWISH(App.context.getString(R.string.swish_pay), "Swish"),
    OTHER(App.context.getString(R.string.other_pay), "Annat"),
    NOTHING("", "");
}

@InjectViewState
class ElectronicTemplatePresenter : MvpPresenter<ElectronicTemplateView>() {

    companion object {
        var model: String = ""
        var mark: String = ""
        var serialNumber: String = ""
        var type: String = ""
        var photo: ArrayList<Uri> = arrayListOf()
        var state: ElectronicState = ElectronicState.NOTHING
        var otherInfo: String = ""
        var methodDelivery = DeliveryMethod.NOTHING
        var dateDelivery: String = ""
        var methodPayment = PaymentMethod.NOTHING
        var price: String = ""
        var garanteeChecked: Boolean = false
        var originalChecked: Boolean = false
    }

    init {
        model = ""
        mark = ""
        serialNumber = ""
        type = ""
        photo.clear()
        state = ElectronicState.NOTHING
        otherInfo = ""
        methodDelivery = DeliveryMethod.NOTHING
        dateDelivery = ""
        methodPayment = PaymentMethod.NOTHING
        price = ""
        garanteeChecked = false
        originalChecked = false
    }

    fun onCreate() {
    }

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
            OTHER_INFO -> otherInfo = trimText
            DATE_DELIVERY -> dateDelivery = trimText
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
        STATE -> state == ElectronicState.NOTHING
        OTHER_INFO -> otherInfo.isEmpty()
        METHOD_DELIVERY -> methodDelivery == DeliveryMethod.NOTHING
        DATE_DELIVERY -> dateDelivery.isEmpty()
        METHOD_PAYMENT -> methodPayment == PaymentMethod.NOTHING
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
        if (s.isEmpty()) {
            viewState.setVisibilityText(fieldType, false)
        }
    }

    fun onGaranteeCheck() = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        garanteeChecked = isChecked
    }

    fun onOriginalCheck() = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        originalChecked = isChecked
    }

    private var unregistrar: Unregistrar? = null

    fun onStart(activity: Activity) {
        unregistrar = registerEventListener(activity, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen) {
                    viewState.hidePreview()
                } else {
                    viewState.showPreview()
                }
            }
        })
        viewState.setState(state.textForUser)
        viewState.setDeliveryMethod(methodDelivery.textForUser)
        viewState.setPaymentMethod(methodPayment.textForUser)
        viewState.setDeliveryDate(dateDelivery)
        viewState.setCountPhoto(photo.size)
        viewState.setOtherInfo(otherInfo)
    }

    fun onStop() {
        unregistrar?.unregister()
    }

    fun previewListener() = View.OnClickListener {
        if (fieldIsCorrect()) {
            navigateToPreview()
        }
    }

    private fun navigateToPreview() {

    }

    private fun fieldIsCorrect(): Boolean {
        var correctFields = true
        var field: ElectronicFields? = null
        if (isPriceNotCorrect()) {
            field = ElectronicFields.PRICE
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isMethodPaymentNotCorrect()) {
            field = ElectronicFields.METHOD_PAYMENT
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isDateDeliveryNotCorrect()) {
            field = ElectronicFields.DATE_DELIVERY
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isMethodDeliveryNotCorrect()) {
            field = ElectronicFields.METHOD_DELIVERY
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isOtherInfoNotCorrect()) {
            field = ElectronicFields.OTHER_INFO
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isStateNotCorrect()) {
            field = ElectronicFields.STATE
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isPhotoNotCorrect()) {
            field = ElectronicFields.PHOTO
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isSerialNumberNotCorrect()) {
            field = ElectronicFields.SERIAL_NUMBER
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isModelNotCorrect()) {
            field = ElectronicFields.MODEL
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isMarkNotCorrect()) {
            field = ElectronicFields.MARK
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (isTypeNotCorrect()) {
            field = ElectronicFields.TYPE
            viewState.setErrorText(field, true)
            correctFields = false
        }
        if (field != null)
            viewState.setFocus(field, false)
        return correctFields
    }

    private fun isTypeNotCorrect() = type.isEmpty()
    private fun isMarkNotCorrect() = mark.isEmpty()
    private fun isModelNotCorrect() = model.isEmpty()
    private fun isSerialNumberNotCorrect() = serialNumber.isEmpty()
    private fun isPriceNotCorrect() = price.isEmpty()
    private fun isMethodPaymentNotCorrect() = methodPayment == PaymentMethod.NOTHING
    private fun isDateDeliveryNotCorrect() = dateDelivery.isEmpty()
    private fun isMethodDeliveryNotCorrect() = methodDelivery == DeliveryMethod.NOTHING
    private fun isOtherInfoNotCorrect() = otherInfo.isEmpty()
    private fun isStateNotCorrect() = state == ElectronicState.NOTHING
    private fun isPhotoNotCorrect() = photo.isEmpty()

    fun getStateClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.SELECT_STATE.name)
    }

    fun getDeliveryMethodClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.SELECT_DELIVERY_METHOD.name)
    }

    fun getPaymentMethodClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.SELECT_PAYMENT_METHOD.name)
    }

    fun getDateClickListener() = View.OnClickListener {
        viewState.showDatePickerDialog { y, m, d ->
            dateDelivery = "$y-${m + 1}-$d"
            viewState.setDeliveryDate(dateDelivery)
        }
    }

    fun getPhotoClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.SELECT_PHOTOS.name)
    }

    fun getOtherInfoClickListener() = View.OnClickListener {
        Router.navigateTo(FrmFabric.SELECT_OTHER_INFO.name)
    }
}
