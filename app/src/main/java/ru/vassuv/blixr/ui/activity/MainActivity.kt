package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout.*
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.FrmFabric
import ru.vassuv.blixr.R
import ru.vassuv.blixr.utils.ATLibriry.*
import android.support.design.widget.Snackbar



class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener (getNavigationListener())
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
        Router.newRootScreen(FrmFabric.MAIN.name)
    }

    fun shouldDisplayHomeUp() {
        val showHomeButton = supportFragmentManager.backStackEntryCount <= 1
        toggle.isDrawerIndicatorEnabled = showHomeButton

        drawerLayout.setDrawerLockMode(if(showHomeButton) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED)

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
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
    }

    private fun getNavigator(): Navigator {
        return object : Navigator(supportFragmentManager, R.id.container, onChangeFragment = {

        }) {
            override fun createFragment(screenKey: String, data: Bundle) =
                    FrmFabric.valueOf(screenKey).create()

            override fun showSystemMessage(message: String) {
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
            }

            override fun exit() {
            }

            override fun openFragment(name: String) {
            }
        }
    }

    private fun getNavigationListener(): (MenuItem) -> Boolean {
        return { item ->
            when (item.itemId) {
                R.id.documents -> {
                    Router.navigateTo(FrmFabric.AUTH.name)
                }
                R.id.film -> {
                    Router.navigateTo(FrmFabric.MAIN.name)
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
                R.id.exit -> finish()
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

}
