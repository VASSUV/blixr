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
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue

class SplashActivity : AppCompatActivity() {

    private val DEFAULT_VERSION = "0.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkVersion()
    }

    private fun checkVersion() {
//        progress.visibility = View.VISIBLE
        Methods.SERVER_VERSION.httpGet().authenticate("montrollBring", "UITableView_up2").responseString { request, response, result ->
            println(request)
            println(response)
            when (result) {
                is Result.Success -> {
                    if(result.value.isNotEmpty()) {
                        val version = JsonValue.readFrom(result.value).asObject().get(VERSION)?.asString()
                                ?: DEFAULT_VERSION
                        Toast.makeText(this, "Версия $version", Toast.LENGTH_LONG).show()
                        SharedData.VERSION.saveString(version)
                    } else {
                        Toast.makeText(this, "Ответ пустой", Toast.LENGTH_LONG).show()
                    }
                }
                is Result.Failure -> Logger.traceException(Methods.SERVER_VERSION, result.getException())
            }
            exitSplashActivity()
//                    progress.visibility = View.GONE
        }
    }

    private fun exitSplashActivity() {
        startActivity(Intent(this, if (SharedData.IS_NOT_FIRST_START.getBoolean()) {
            MainActivity::class.java
        } else {
            IntroActivity::class.java
        }))
        finish()
    }
}
