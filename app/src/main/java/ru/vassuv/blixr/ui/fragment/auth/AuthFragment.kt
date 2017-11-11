package ru.vassuv.blixr.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.auth.AuthView
import ru.vassuv.blixr.presentation.presenter.auth.AuthPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import org.jetbrains.anko.find
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment

class AuthFragment : MvpAppCompatFragment(), AuthView, IFragment {
    companion object {
        const val TAG = "AuthFragment"

        fun newInstance(): AuthFragment {
            val fragment = AuthFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override val type = FrmFabric.AUTH

    @InjectPresenter
    lateinit var mAuthPresenter: AuthPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_auth, container, false)
        val userNumber = rootView.find<EditText>(R.id.userNumber)
        val button = rootView.find<Button>(R.id.logIn)

        userNumber.addTextChangedListener(mAuthPresenter.getTextChangeListener())
        button.setOnClickListener(mAuthPresenter.getOnClickListener())
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mAuthPresenter.onResult(requestCode, resultCode, data)
    }
}
