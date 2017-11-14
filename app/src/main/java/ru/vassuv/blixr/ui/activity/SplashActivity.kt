package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Fields.VERSION
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        exitSplashActivity()
    }

    private fun exitSplashActivity() {
        if (SharedData.IS_NOT_FIRST_START.getBoolean()) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            if (DataBase.getUser() != null) {
                intent.putExtra("theme", R.style.AppTheme_MainActionBar_Autorized)
            }
            startActivity(intent)
        } else {
            startActivity(Intent(this, IntroActivity::class.java))
        }
        Thread.sleep(500)
        finish()
    }
}
