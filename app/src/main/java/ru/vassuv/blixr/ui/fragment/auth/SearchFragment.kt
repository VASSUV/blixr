package ru.vassuv.blixr.ui.fragment.auth

import android.os.Bundle
import android.provider.BlockedNumberContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.auth.SearchView
import ru.vassuv.blixr.presentation.presenter.search.SearchPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.alert
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.ATLibriry.Router

class SearchFragment : MvpAppCompatFragment(), SearchView, IFragment {
    companion object {

        fun newInstance(): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override val type = FrmFabric.SEARCH

    @InjectPresenter
    lateinit var mAuthPresenter: SearchPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search.setOnClickListener(mAuthPresenter.getOnClickListener())
        code.addTextChangedListener(mAuthPresenter.getTextChangeListener())
    }

    override fun showErrorAlert(codeString: String) {
        activity.alert("Какие дальнейшие действия?", "Не удалось найти контракт \"$codeString\"") {
            positiveButton(R.string.replay) { }
            negativeButton(R.string.cancel) {
                Router.newRootScreen(FrmFabric.MAIN.name)
            }
        }.show()
    }

    override fun showSuccessAlert() {
        activity.alert ("Контракт успешно получен", "Новый контракт отправлен в черновики ваших документов") {
            positiveButton(R.string.ok) {
                Router.newRootScreen(FrmFabric.MAIN.name)
            }
            onCancelled { }
        }.show()
    }
}