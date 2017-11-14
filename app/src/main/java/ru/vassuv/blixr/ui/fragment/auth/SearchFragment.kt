package ru.vassuv.blixr.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.auth.SearchView
import ru.vassuv.blixr.presentation.presenter.auth.SearchPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import org.jetbrains.anko.find
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class SearchFragment : MvpAppCompatFragment(), SearchView, IFragment {
    companion object {
        const val TAG = "SearchFragment"

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
        val rootView = inflater.inflate(R.layout.fragment_auth, container, false)
        val userNumber = rootView.find<EditText>(R.id.userNumber)
        val button = rootView.find<View>(R.id.logIn)

        userNumber.addTextChangedListener(mAuthPresenter.getTextChangeListener())
        button.setOnClickListener(mAuthPresenter.getOnClickListener())
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mAuthPresenter.onResult(requestCode, resultCode)
    }
}
