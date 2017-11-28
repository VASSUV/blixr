package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View.OnClickListener
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.FrmFabric.*
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.utils.ATLibriry.*
import ru.vassuv.blixr.utils.INTERNET_ERROR
import ru.vassuv.blixr.utils.verifyResult
import kotlinx.android.synthetic.main.loader.*
import ru.vassuv.blixr.repository.SessionConfig
import android.os.Build
import android.widget.FrameLayout
import android.app.Activity
import android.app.LoaderManager
import android.support.annotation.ColorRes
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import ru.vassuv.blixr.ui.components.Loader
import ru.vassuv.blixr.ui.components.SystemState
import ru.vassuv.blixr.utils.KeyboardUtils


class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = object : ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    SystemState.onNavigatorHide?.invoke()
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(getNavigationListener())
        App.setNavigationHolder(getNavigator())

        supportFragmentManager.addOnBackStackChangedListener {
            shouldDisplayHomeUp()
            KeyboardUtils.hideKeyboard(this)
        }
        toggle.toolbarNavigationClickListener = OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (supportFragmentManager.backStackEntryCount == 0) {
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        }

        shouldDisplayHomeUp()
        Router.onNewRootScreenListener = getNewRootScreenListener()
        Router.onBackScreenListener = getBackScreenListener()
        Router.newRootScreen(MAIN.name)

        checkVersion()
    }

    override fun onStart() {
        super.onStart()
        val user = DataBase.getUser()
        val isAuthorizedMode = user != null
        if (this.user == null && isAuthorizedMode)  {
            this.user = user
            invalidateOptionsMenu()
        }

        SystemState.loader =  Loader(progress)
        setStatusBarColored(this, if (isAuthorizedMode) R.color.colorPrimaryDark else R.color.colorPrimaryDarkNotAuth)
        toolbar.setBackgroundColor(resources.getColor(if (isAuthorizedMode) R.color.colorPrimary else R.color.colorPrimaryNotAuth))
    }

    override fun onStop() {
        super.onStop()
        SystemState.loader = null
    }

    private fun setStatusBarColored(context: Activity, @ColorRes colorStatusBar: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = context.window
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val statusBarHeight = getStatusBarHeight(context)

            val view = View(context)
            view.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.layoutParams.height = statusBarHeight
            (w.decorView as ViewGroup).addView(view)
            view.setBackgroundColor(context.resources.getColor(colorStatusBar))
        }
    }

    private fun getStatusBarHeight(context: Activity): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    private fun shouldDisplayHomeUp() {
        val showHomeButton = supportFragmentManager.backStackEntryCount <= 1
        toggle.isDrawerIndicatorEnabled = showHomeButton

        drawerLayout.setDrawerLockMode(if (showHomeButton) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED)

        if (supportFragmentManager.backStackEntryCount > 1) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            toggle.syncState()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        KeyboardUtils.hideKeyboard(this)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            KeyboardUtils.hideKeyboard(this)
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment != null && fragment is IFragment) {
                (fragment as IFragment).onBackPressed()
            } else {
                super.onBackPressed()
            }

            if (supportFragmentManager.backStackEntryCount == 0) {
                super.onBackPressed()
            }
        }
    }

    private
    var currentType: FragmentFabric? = null

    private fun getNavigator(): Navigator {
        return object : Navigator(supportFragmentManager, R.id.container, getChangeFragmentListener()) {
            override fun createFragment(screenKey: String, data: Bundle) =
                    valueOf(screenKey).create()

            override fun showSystemMessage(message: String) {
                showMessage(message)
            }

            override fun exit() {
            }

            override fun openFragment(name: String) {
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun getChangeFragmentListener(): () -> Unit = {
        val fragment = getCurrentFragment()
        if (fragment != null && currentType != fragment.type) {
            currentType = fragment.type
            when (currentType) {
                MAIN -> {
                    setTitle(R.string.title_main_screen)
                    baseState()
                }
                SEARCH -> {
                    setTitle(R.string.title_search_screen)
                    baseState()
                }
                DOCUMENTS -> {
                    setTitle(R.string.title_main_screen)
                    baseState()
                }
                SHARE -> {
                    setTitle(R.string.title_share_screen)
                    baseState()
                }
                TEMPLATES -> {
                    setTitle(R.string.title_create_contract)
                    baseState()
                }
                ELECTRONIC_TEMPLATE -> {
                    setTitle(R.string.title_electronic)
                    baseState()
                }
            }
        }
    }

    private fun getCurrentFragment(): IFragment? {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        return if (fragment is IFragment) fragment else null
    }

    private fun getNavigationListener(): (MenuItem) -> Boolean {
        return { item ->
            when (item.itemId) {
                R.id.documents -> {
                    if (user == null) {
                        SharedData.LOGIN_TOOLTIP_SHOWED.saveBoolean(false)
//                        showMenuTooltip()
                    } else {
                        Router.navigateTo(DOCUMENTS.name)
                    }
                }
                R.id.film -> {
//                    Router.navigateTo(MAIN.name)
                }
                R.id.share -> {
                    if (user == null) {
                        SharedData.LOGIN_TOOLTIP_SHOWED.saveBoolean(false)
//                        showMenuTooltip()
                    } else {
                        Router.navigateTo(SHARE.name)
                    }
                }
                R.id.terms -> {
                }
                R.id.privacy -> {
                }
                R.id.email -> {
                }
                R.id.intro -> {
                    val intent = Intent(this@MainActivity, IntroActivity::class.java)
                    intent.putExtra("for_result", true)
                    startActivityForResult(intent, 100)
                }
                R.id.exit -> {
                    SharedData.values().forEach { it.remove() }
                    DataBase.clearUser()
                    finish()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun getBackScreenListener() = object : OnBackScreenListener {
        override fun onBackScreen() {
            KeyboardUtils.hideKeyboard(this@MainActivity)
        }
    }

    private fun getNewRootScreenListener(): OnNewRootScreenListener {
        return object : OnNewRootScreenListener {
            override fun onChangeRootScreen(screenKey: String) {
            }
        }
    }

    private fun checkVersion() {
        if (!SessionConfig.versionIsLoaded) {
            progress.visibility = View.VISIBLE
            Methods.SERVER_VERSION.httpGet().responseString { _, _, result ->
                val verifyResult = verifyResult(result)
                if (!verifyResult.isOk && verifyResult.status == INTERNET_ERROR) {
                    showMessage(verifyResult.errorText)
                }
                progress.visibility = View.GONE
            }
        }
    }

    /* Методы состояний */

    fun hideActionBar() {
        toolbar.visibility = View.GONE
    }

    fun baseState() {
        showActionBar()
        KeyboardUtils.showKeyboard(this)
    }

    fun showActionBar() {
        toolbar.visibility = View.VISIBLE
    }
}
