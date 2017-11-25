package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.SharedData

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        exitSplashActivity()
    }

    private fun exitSplashActivity() {
        if (SharedData.IS_NOT_FIRST_START.getBoolean()) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
        }
        Thread.sleep(500)
        finish()
    }
}
