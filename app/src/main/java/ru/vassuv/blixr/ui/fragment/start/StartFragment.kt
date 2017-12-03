package ru.vassuv.blixr.ui.fragment.start

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.tooltip.Tooltip
import kotlinx.android.synthetic.main.fragment_start.*
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.presenter.start.StartPresenter
import ru.vassuv.blixr.presentation.view.start.StartView
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.ui.activity.LoginActivity
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import ru.vassuv.blixr.utils.ATLibriry.Router

class StartFragment : MvpAppCompatFragment(), IFragment, StartView {
    override val type: FragmentFabric = FrmFabric.MAIN

    companion object {

        fun newInstance(): StartFragment {
            val fragment = StartFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: StartPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uriPath = "android.resource://" + context.packageName + "/" + R.raw.crowd
        val uri = Uri.parse(uriPath)

        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { it.isLooping = true }
        videoView.setMediaController(null)
        videoView.requestFocus(0)

        handshake.setOnClickListener(presenter.onHandshakeClick())
    }

    override fun onStart() {
        super.onStart()
        videoView.visibility = View.VISIBLE
        presenter.onStart()
        showMenuTooltip()
        if (handshakeTooltip?.isShowing != true && presenter.user != null) {
            showHandshakeToolTip()
        }
        videoView.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
        videoView.visibility = View.INVISIBLE
        hideHandshakeTooltip()
        hideMenuTooltip()
        videoView.stopPlayback()
    }

    private var menuTooltip: Tooltip? = null
    private var handshakeTooltip: Tooltip? = null

    override fun showMenuTooltip() {
        if (menuTooltip == null) return

        if (presenter.user == null && !SharedData.LOGIN_TOOLTIP_SHOWED.getBoolean()) {
            menuTooltip!!.show()
            SharedData.LOGIN_TOOLTIP_SHOWED.saveBoolean(true)
        } else if (presenter.user != null && !SharedData.SEARCH_TOOLTIP_SHOWED.getBoolean()) {
            menuTooltip!!.show()
            SharedData.SEARCH_TOOLTIP_SHOWED.saveBoolean(true)
        }
    }

    override fun hideMenuTooltip() {
        menuTooltip?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(if (presenter.user == null) R.menu.main else R.menu.main_autorized, menu)
        createTooltips {
            showMenuTooltip()
        }
    }

    private fun createTooltips(show: (()->Unit)? = null) {
        createLoginTooltip(show)
        createSearchTooltip(show)
    }

    override fun invalidateOptionMenu() {
        activity.invalidateOptionsMenu()
        createTooltips()
    }

    private fun createLoginTooltip(show: (()->Unit)? = null) {
        if (presenter.user != null) return
        Handler().post({
            val logIn = activity.findViewById<View>(R.id.logIn)
            if (logIn != null) {
                menuTooltip?.dismiss()
                menuTooltip = Tooltip.Builder(logIn, R.style.AppTheme)
                        .setCornerRadius(R.dimen.tooltipRadius)
                        .setBackgroundColor(resources.getColor(R.color.tooltipColor))
                        .setText(R.string.login_here)
                        .setTextColor(Color.WHITE)
                        .build()
                show?.invoke()
            }
        })
    }

    private fun createSearchTooltip(show: (()->Unit)? = null) {
        if (presenter.user == null) return
        Handler().post({
            val search = activity.findViewById<View>(R.id.search)
            if (search != null && !SharedData.SEARCH_TOOLTIP_SHOWED.getBoolean()) {
                menuTooltip?.dismiss()
                menuTooltip = Tooltip.Builder(search, R.style.AppTheme)
                        .setCornerRadius(R.dimen.tooltipRadius)
                        .setBackgroundColor(resources.getColor(R.color.tooltipColor))
                        .setText(R.string.search_here)
                        .setTextColor(Color.WHITE)
                        .build()
                show?.invoke()
            }
        })
    }

    override fun showHandshakeToolTip() {
        if (!SharedData.HAND_SHAKE_TOOLTIP_SHOWED.getBoolean()) {
            if (handshakeTooltip == null) {
                handshakeTooltip = Tooltip.Builder(handshake, R.style.AppTheme)
                        .setCornerRadius(R.dimen.tooltipRadius)
                        .setBackgroundColor(context.resources.getColor(R.color.tooltipColor))
                        .setText(R.string.create_document)
                        .setTextColor(Color.WHITE)
                        .show()
            } else {
                handshakeTooltip?.show()
            }
            SharedData.HAND_SHAKE_TOOLTIP_SHOWED.saveBoolean(true)
        }

    }

    override fun hideHandshakeTooltip() {
        handshakeTooltip?.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logIn -> {
            startActivity(Intent(activity, LoginActivity::class.java))
            tooltipMenuHide()
            true
        }
        R.id.search -> {
            Router.navigateTo(FrmFabric.SEARCH.name)
            tooltipMenuHide()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun tooltipMenuHide() {
        menuTooltip?.dismiss()
    }

    override fun startLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
        Thread.sleep(500)
        activity.finish()
    }
}