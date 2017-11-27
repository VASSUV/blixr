package ru.vassuv.blixr.presentation.view.template

import com.arellomobile.mvp.MvpView
import ru.vassuv.blixr.ui.fragment.template.ElectronicTemplateFragment

interface ElectronicTemplateView : MvpView {

    fun setErrorText(field: ElectronicTemplateFragment.ElectronicFields, errorVisibility: Boolean)
    fun setVisibilityText(field: ElectronicTemplateFragment.ElectronicFields, visibility: Boolean)
    fun setFocus(field: ElectronicTemplateFragment.ElectronicFields, showKeyboard: Boolean)
    fun setType(value: String)
    fun setMark(value: String)
    fun setModel(value: String)
    fun setOther(value: String)
    fun changeKeyboardVisibility(visibility: Boolean)
    fun hidePreview()
    fun showPreview()
}
