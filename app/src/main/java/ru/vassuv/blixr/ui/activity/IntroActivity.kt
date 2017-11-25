package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_intro.*
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.SharedData

class IntroActivity : AppCompatActivity() {

    private val layoutArray = intArrayOf(
            R.layout.intro_1,
            R.layout.intro_2,
            R.layout.intro_3,
            R.layout.intro_4,
            R.layout.intro_5)

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        introIndicator.setupWithViewPager(container, true)

        closeButton.setOnClickListener {
            goToMain()
        }
    }

    private fun goToMain() {
        if(!intent.getBooleanExtra("for_result", false)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    override fun onStart() {
        super.onStart()
        SharedData.IS_NOT_FIRST_START.saveBoolean(true)
        SharedData.HAND_SHAKE_TOOLTIP_SHOWED.remove()
        SharedData.LOGIN_TOOLTIP_SHOWED.remove()
        SharedData.SEARCH_TOOLTIP_SHOWED.remove()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(layoutArray[position])
        }

        override fun getCount(): Int {
            return layoutArray.size
        }
    }

    class PlaceholderFragment : Fragment() {
        var layoutId: Int = 0

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(layoutId, container, false)
        }

        companion object {

            fun newInstance(gradientId: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                fragment.layoutId = gradientId
                fragment.arguments = Bundle()
                return fragment
            }
        }
    }
}
