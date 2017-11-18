package ru.vassuv.blixr.ui.fragment.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.share.ShareView
import ru.vassuv.blixr.presentation.presenter.share.SharePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import org.jetbrains.anko.alert
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.ATLibriry.Router

class ShareFragment() : MvpAppCompatFragment(), ShareView, IFragment {

    override val type: FragmentFabric = FrmFabric.SHARE

    companion object {
        const val TAG = "ShareFragment"

        fun newInstance(): ShareFragment {
            val fragment = ShareFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mSharePresenter: SharePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_share, container, false)
        rootView.findViewById<EditText>(R.id.email).addTextChangedListener(mSharePresenter.getEmailTextWatcher())
        rootView.findViewById<View>(R.id.send).setOnClickListener(mSharePresenter.getOnClickListener())
        return rootView
    }

    override fun onStart() {
        super.onStart()
        mSharePresenter.onStart()
    }

    override fun showErrorAlert() {
        activity.alert("Не удалось пригласить Вашего друга. Приносим свои извинения", "Не удалось пригласить друга") {
            positiveButton(R.string.replay) { }
            negativeButton(R.string.cancel) {
                Router.newRootScreen(FrmFabric.MAIN.name)
            }
        }.show()
    }

    override fun showSuccessAlert() {
        activity.alert ("Ваш друг приглашен, он получит сообщение на электронную почту", "Ваш друг приглашен") {
            positiveButton(R.string.ok) {
                Router.newRootScreen(FrmFabric.MAIN.name)
            }
            onCancelled { }
        }.show()
    }
}
