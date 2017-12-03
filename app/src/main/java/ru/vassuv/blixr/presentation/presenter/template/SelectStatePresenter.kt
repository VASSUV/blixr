package ru.vassuv.blixr.presentation.presenter.template

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.blixr.presentation.view.template.SelectStateView
import ru.vassuv.blixr.utils.ATLibriry.Router

@InjectViewState
class SelectStatePresenter : MvpPresenter<SelectStateView>() {
    fun onBackPressed() {
        Router.exit()
    }

    fun getAsNewClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.state = ElectronicState.AS_NEW
        onBackPressed()
    }

    fun getGoodClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.state = ElectronicState.GOOD
        onBackPressed()
    }

    fun getUsedClickListener() = View.OnClickListener {
        ElectronicTemplatePresenter.state = ElectronicState.USED
        onBackPressed()
    }

}
