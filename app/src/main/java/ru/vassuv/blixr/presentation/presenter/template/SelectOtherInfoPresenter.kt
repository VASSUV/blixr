package ru.vassuv.blixr.presentation.presenter.template

import android.text.Editable
import android.text.TextWatcher
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.SelectOtherInfoView
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class SelectOtherInfoPresenter : MvpPresenter<SelectOtherInfoView>() {
    private var text: String = ""

    fun getTextChangeListener() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            text = s.toString().trim()
        }

    }

    fun onApplyClick() {
        ElectronicTemplatePresenter.otherInfo = text
        Router.exit()
    }

}
