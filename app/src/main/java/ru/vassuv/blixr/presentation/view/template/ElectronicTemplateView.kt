package ru.vassuv.blixr.presentation.view.template

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment

@StateStrategyType(SingleStateStrategy::class)
interface ElectronicTemplateView : MvpView {

    fun setErrorText(field: ElectronicTemplateFragment.ElectronicFields, errorVisibility: Boolean)
    fun setVisibilityText(field: ElectronicTemplateFragment.ElectronicFields, visibility: Boolean)
    fun setFocus(field: ElectronicTemplateFragment.ElectronicFields, showKeyboard: Boolean)
    fun setType(value: String)
    fun setMark(value: String)
    fun setModel(value: String)
    fun setSerialNumber(value: String)
    fun changeKeyboardVisibility(visibility: Boolean)
    fun hidePreview()
    fun showPreview()
    fun setState(value: String)
    fun setDeliveryMethod(value: String)
    fun setPaymentMethod(value: String)
    fun showDatePickerDialog(result: (Int, Int, Int) -> Unit)
    fun setDeliveryDate(value: String)
    fun setCountPhoto(size: Int)
    fun setOtherInfo(otherInfo: String)
}
