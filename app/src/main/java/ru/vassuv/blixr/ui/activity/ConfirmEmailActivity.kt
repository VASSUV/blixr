package ru.vassuv.blixr.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_confirm_email.*
import org.jetbrains.anko.alert
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.ANY_PASSWORD
import ru.vassuv.blixr.repository.OK
import ru.vassuv.blixr.repository.SessionConfig
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.repository.*
import ru.vassuv.blixr.repository.db.EMAIL
import ru.vassuv.blixr.repository.db.USER_ID
import ru.vassuv.blixr.utils.verifyResult

class ConfirmEmailActivity : AppCompatActivity() {

    private var textEmail: String = ""

    private var token: String = ""

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_email)

        email.addTextChangedListener(getTextChangeListener())
        confirm.setOnClickListener(getConfirmClickListener())
        cancel.setOnClickListener(getCancelClickListener())

        token = SharedData.TOKEN.getString()
        if (token.isEmpty()) {
            loadToken()
        }

        user = DataBase.getUser()
    }

    private fun showMessage(text: String) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()
    }

    private fun loadToken(restartMethod: (() -> Unit)? = null) {
        Methods.TOKEN.httpGet()
                .authenticate(SessionConfig.USER_NAME, SessionConfig.USER_PASSWORD)
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        token = JsonValue.readFrom(verifyResult.value).asObject()
                                .get(Fields.TOKEN)?.asString() ?: "";
                        SharedData.TOKEN.saveString(token)

                        if (restartMethod != null) {
                            restartMethod()
                        }
                    } else {
                        showMessage(verifyResult.value)
                    }
                }
    }

    private fun confirmPost() {
        if (user == null) {
            return
        }
        (Methods.PASS_EMAIL).httpPost()
                .header(JSON_HEADER)
                .authenticate(token, ANY_PASSWORD)
                .body(JsonObject().add(USER_ID, user!!.id.toString())
                        .add(EMAIL, textEmail).toString())
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        if (OK == JsonObject.readFrom(verifyResult.value).string(Fields.STATUS)) {
                            alert(R.string.registration_success_send_email, R.string.registration) {
                                positiveButton(R.string.ok) {
                                    restartMainActivity()
                                }
                                onCancelled { }
                            }.show()
                        } else {
                            alert(R.string.email_already_use, R.string.registration_error) {
                                positiveButton(R.string.replay) { }
                                negativeButton(R.string.cancel) {
                                    restartMainActivity()
                                }
                            }.show()
                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { confirmPost() }
                    } else {
                        showMessage(verifyResult.value)
                    }
                }
    }

    private fun getConfirmClickListener() = View.OnClickListener {
        if (validateEmail()) {
            confirmPost()
        }
    }

    private fun validateEmail(): Boolean = true

    private fun getCancelClickListener() = View.OnClickListener {
        restartMainActivity()
    }

    override fun onBackPressed() {
        restartMainActivity()
    }

    private fun restartMainActivity() {
        val intent = Intent(this@ConfirmEmailActivity, MainActivity::class.java)
        intent.putExtra(THEME, R.style.AppTheme_MainActionBar_Autorized)
        startActivity(intent)
        Thread.sleep(500)
        finish()
    }

    private fun getTextChangeListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textEmail = s.toString()
            }
        }
    }
}
