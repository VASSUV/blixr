package ru.vassuv.blixr.ui.fragment.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.presenter.strat.StartPresenter
import ru.vassuv.blixr.presentation.view.strat.StartView
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric
import ru.vassuv.blixr.utils.ATLibriry.IFragment
import android.net.Uri
import kotlinx.android.synthetic.main.fragment_start.*
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.ui.activity.LoginActivity

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)
        val videoView = rootView.findViewById<VideoView>(R.id.videoView)
        val handShake = rootView.findViewById<View>(R.id.handshake)

        handShake.setOnClickListener {
            presenter.onHandshakeClick()
        }

        val uriPath = "android.resource://"+context.packageName+"/"+R.raw.crowd

        val uri = Uri.parse(uriPath)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { it.isLooping = true }
        videoView.setMediaController(null)
        videoView.requestFocus(0)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        videoView.visibility = View.VISIBLE
        videoView.start()
        val user = DataBase.getUser()

        if (user != null) {

        } else {

        }
    }

    override fun onStop() {
        super.onStop()
        videoView.stopPlayback()
        videoView.visibility = View.INVISIBLE
    }

    override fun startLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
        Thread.sleep(500)
        activity.finish()
    }
}