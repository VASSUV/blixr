package ru.vassuv.blixr.ui.fragment.template

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.template.ElectronicTemplateView
import ru.vassuv.blixr.presentation.presenter.template.ElectronicTemplatePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_electronic_template.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import android.app.DatePickerDialog
import ru.vassuv.blixr.utils.keyboard.hideKeyboard
import ru.vassuv.blixr.utils.keyboard.showKeyboard
import java.util.*


class ElectronicTemplateFragment : MvpAppCompatFragment(), ElectronicTemplateView, IFragment {
    override val type = FrmFabric.ELECTRONIC_TEMPLATE

    companion object {

        fun newInstance(): ElectronicTemplateFragment {
            val fragment = ElectronicTemplateFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }

    @InjectPresenter
    lateinit var presenter: ElectronicTemplatePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_electronic_template, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type_name.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.TYPE))
        mark.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.MARK))
        model.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.MODEL))
        serial_number.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.SERIAL_NUMBER))
        garantee_checkbox.setOnCheckedChangeListener(presenter.onGaranteeCheck())
        original_checkbox.setOnCheckedChangeListener(presenter.onOriginalCheck())
        photo.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.PHOTO))
        state.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.STATE))
        other_info.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.OTHER_INFO))
        method.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.METHOD_DELIVERY))
        payment_method.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.METHOD_PAYMENT))
        price.addTextChangedListener(presenter.onFieldTextWatcher(ElectronicFields.PRICE))
        preview_button.setOnClickListener(presenter.previewListener())

        state_block.setOnClickListener(presenter.getStateClickListener())
        method_block.setOnClickListener(presenter.getDeliveryMethodClickListener())
        payment_method_block.setOnClickListener(presenter.getPaymentMethodClickListener())
        date_block.setOnClickListener(presenter.getDateClickListener())
        date_name.setOnClickListener(presenter.getDateClickListener())
        photo_block.setOnClickListener(presenter.getPhotoClickListener())
        other_info.setOnClickListener(presenter.getOtherInfoClickListener())
    }

    override fun setVisibilityText(field: ElectronicFields, visibility: Boolean) {
        when (field) {
            ElectronicFields.TYPE -> setVisibilityView(type_text, visibility, false)
            ElectronicFields.MARK -> setVisibilityView(mark_text, visibility, false)
            ElectronicFields.MODEL -> setVisibilityView(model_text, visibility, false)
            ElectronicFields.SERIAL_NUMBER -> setVisibilityView(serial_number_text, visibility, false)
            ElectronicFields.PHOTO -> setVisibilityView(photo_text, visibility, false)
            ElectronicFields.STATE -> setVisibilityView(state_text, visibility, false)
            ElectronicFields.OTHER_INFO -> setVisibilityView(other_info_text, visibility, false)
            ElectronicFields.METHOD_DELIVERY -> setVisibilityView(method_text, visibility, false)
            ElectronicFields.DATE_DELIVERY -> setVisibilityView(date_text, visibility, false)
            ElectronicFields.METHOD_PAYMENT -> setVisibilityView(payment_method_text, visibility, false)
            ElectronicFields.PRICE -> setVisibilityView(price_text, visibility, false)
            else -> {
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart(activity)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    private fun setVisibilityView(text: CheckedTextView, visible: Boolean = false, checked: Boolean = visible) {
        text.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        text.isChecked = checked
    }

    private fun setErrorVisibility(text: CheckedTextView, visible: Boolean = false) {
        if (visible) text.visibility = View.VISIBLE
        text.isChecked = visible
    }

    override fun setErrorText(field: ElectronicFields, errorVisibility: Boolean) {
        when (field) {
            ElectronicFields.TYPE ->
                setErrorVisibility(type_text, errorVisibility)// && type_name.text.isNotEmpty())
            ElectronicFields.MARK ->
                setErrorVisibility(mark_text, errorVisibility)// && mark.text.isNotEmpty())
            ElectronicFields.MODEL ->
                setErrorVisibility(model_text, errorVisibility)// && model.text.isNotEmpty())
            ElectronicFields.SERIAL_NUMBER ->
                setErrorVisibility(serial_number_text, errorVisibility)// && serial_number.text.isNotEmpty())
            ElectronicFields.PHOTO ->
                setErrorVisibility(photo_text, errorVisibility)
            ElectronicFields.STATE ->
                setErrorVisibility(state_text, errorVisibility)
            ElectronicFields.OTHER_INFO ->
                setErrorVisibility(other_info_text, errorVisibility)
            ElectronicFields.METHOD_DELIVERY ->
                setErrorVisibility(method_text, errorVisibility)
            ElectronicFields.DATE_DELIVERY ->
                setErrorVisibility(date_text, errorVisibility)
            ElectronicFields.METHOD_PAYMENT ->
                setErrorVisibility(payment_method_text, errorVisibility)
            ElectronicFields.PRICE ->
                setErrorVisibility(price_text, errorVisibility)
            else -> {
            }
        }
    }

    override fun setFocus(field: ElectronicFields, showKeyboard: Boolean) {
        when (field) {
            ElectronicFields.TYPE -> {
                type_name.isFocusable = true
                scrollTo(type_block)
            }
            ElectronicFields.MARK -> {
                mark.isFocusable = true
                mark.requestFocus()
                scrollTo(type_block)
            }
            ElectronicFields.MODEL -> {
                model.isFocusable = true
                model.requestFocus()
                scrollTo(mark_block)
            }
            ElectronicFields.SERIAL_NUMBER -> {
                serial_number.isFocusable = true
                serial_number.requestFocus()
                scrollTo(model_block)
            }
            ElectronicFields.PHOTO -> {
                photo.isFocusable = true
                photo.requestFocus()
                scrollTo(photo_block)
            }
            ElectronicFields.STATE -> {
                state.isFocusable = true
                state.requestFocus()
                scrollTo(state_block)
            }
            ElectronicFields.OTHER_INFO -> {
                other_info.isFocusable = true
                other_info.requestFocus()
                scrollTo(other_info_block)
            }
            ElectronicFields.METHOD_DELIVERY -> {
                method.isFocusable = true
                method.requestFocus()
                scrollTo(method_block)
            }
            ElectronicFields.DATE_DELIVERY -> {
                date_name.isFocusable = true
                date_name.requestFocus()
                scrollTo(date_block)
            }
            ElectronicFields.METHOD_PAYMENT -> {
                payment_method.isFocusable = true
                payment_method.requestFocus()
                scrollTo(payment_method_block)
            }
            ElectronicFields.PRICE -> {
                price.isFocusable = true
                price.requestFocus()
                scrollTo(price_block)
            }
            else -> {
            }
        }

        if (showKeyboard) {
//            changeKeyboardVisibility(true)
        }
    }

    private fun scrollTo(element: View?) {
        scrollView.scrollTo(0, element!!.top)
    }

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

    override fun setSerialNumber(value: String) {
        serial_number.setText(value)
        setVisibilityText(ElectronicFields.SERIAL_NUMBER, !value.isEmpty())
    }

    override fun setState(value: String) {
        state.text = value
        setVisibilityText(ElectronicFields.STATE, !value.isEmpty())
    }

    override fun setDeliveryMethod(value: String) {
        method.text = value
        setVisibilityText(ElectronicFields.METHOD_DELIVERY, !value.isEmpty())
    }

    override fun setPaymentMethod(value: String) {
        payment_method.text = value
        setVisibilityText(ElectronicFields.METHOD_PAYMENT, !value.isEmpty())
    }

    override fun setDeliveryDate(value: String) {
        date_name.text = value
        setVisibilityText(ElectronicFields.DATE_DELIVERY, !value.isEmpty())
    }

    override fun setCountPhoto(size: Int) {
        photo.text = if (size > 0) context.getString(R.string.count_photos, size) else ""
        setVisibilityText(ElectronicFields.PHOTO, size > 0)
    }

    override fun setOtherInfo(value: String) {
        other_info.text = value
        setVisibilityText(ElectronicFields.OTHER_INFO, !value.isEmpty())
    }

    override fun showPreview() {
        Handler().postDelayed({
            preview_block?.visibility = View.VISIBLE
        }, 200)
    }

    override fun hidePreview() {
        Handler().postDelayed({
            preview_block?.visibility = View.GONE
        }, 0)
    }

    private var datePickerDialog: DatePickerDialog? = null

    override fun showDatePickerDialog(result: (Int, Int, Int) -> Unit) {
        if (datePickerDialog == null) {
            val calendar = GregorianCalendar.getInstance()
            datePickerDialog = DatePickerDialog(activity,
                    { _, y, m, d -> result(y, m, d) },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
        }
        datePickerDialog?.show()
    }

    enum class ElectronicFields {
        TYPE,
        MARK,
        MODEL,
        SERIAL_NUMBER,
        GARANTEE,
        ORIGINAL,
        PHOTO,
        STATE,
        OTHER_INFO,
        METHOD_DELIVERY,
        DATE_DELIVERY,
        METHOD_PAYMENT,
        PRICE
    }
}
