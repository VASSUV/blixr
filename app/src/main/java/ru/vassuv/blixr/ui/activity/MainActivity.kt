package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.FrmFabric.*
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.utils.ATLibriry.*
import ru.vassuv.blixr.utils.INTERNET_ERROR
import ru.vassuv.blixr.utils.verifyResult

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(intent.getIntExtra("theme", R.style.AppTheme_MainActionBar))
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(getNavigationListener())
        App.setNavigationHolder(getNavigator())

        supportFragmentManager.addOnBackStackChangedListener {
            shouldDisplayHomeUp()
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
        user = DataBase.getUser()
    }

    fun shouldDisplayHomeUp() {
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
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(if (user == null) R.menu.main else R.menu.main_autorized, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logIn -> {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            true
        }
        R.id.search -> {
            Router.navigateTo(FrmFabric.SEARCH.name)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private var currentType: FragmentFabric? = null

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
                    showActionBar()
                }
                SEARCH -> {
                    showActionBar()
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
//                    Router.navigateTo(SEARCH.name)
                }
                R.id.film -> {
//                    Router.navigateTo(MAIN.name)
                }
                R.id.share -> {
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
                    finish()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun getBackScreenListener(): OnBackScreenListener {
        return object : OnBackScreenListener {
            override fun onBackScreen() {
            }
        }
    }

    private fun getNewRootScreenListener(): OnNewRootScreenListener {
        return object : OnNewRootScreenListener {
            override fun onChangeRootScreen(screenKey: String) {
            }
        }
    }

    private fun checkVersion() {
        progress.visibility = View.VISIBLE
        Methods.SERVER_VERSION.httpGet().responseString { _, _, result ->
            val verifyResult = verifyResult(result)
            if (!verifyResult.isOk && verifyResult.status == INTERNET_ERROR) {
                showMessage(verifyResult.value)
            }
            progress.visibility = View.GONE
        }
    }

    /* Методы состояний */

    fun hideActionBar() {
        toolbar.visibility = View.GONE
    }

    fun showActionBar() {
        toolbar.visibility = View.VISIBLE
    }
}
