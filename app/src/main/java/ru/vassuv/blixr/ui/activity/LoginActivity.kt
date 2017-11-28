package ru.vassuv.blixr.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loader.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.R
import ru.vassuv.blixr.repository.*
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.response.Authenticate
import ru.vassuv.blixr.repository.response.CollectData
import ru.vassuv.blixr.repository.response.UserData
import ru.vassuv.blixr.repository.response.UserInfo
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.BankId
import ru.vassuv.blixr.utils.KeyboardUtils
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult

class LoginActivity : AppCompatActivity() {
    private var number: String = ""
    private var sbidClientStarted: Boolean = false

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userNumber.addTextChangedListener(getTextChangeListener())
        logIn.setOnClickListener(getOnClickListener())

//        logIn.isEnabled = false

        token = SharedData.TOKEN.getString()
        if (token.isEmpty()) {
            loadToken()
        }
    }

    private fun loadToken(restartMethod: (() -> Unit)? = null) {
        Methods.TOKEN.httpGet().authenticate("montrollBring", "UITableView_up2").responseString { request, response, result ->
            Logger.trace(request)
            Logger.trace(response)

            val verifyResult = verifyResult(result)
            if (verifyResult.isOk) {
                token = JsonValue.readFrom(verifyResult.value?:"").asObject()
                        .get(Fields.TOKEN)?.asString() ?: "";
                SharedData.TOKEN.saveString(token)

                if (restartMethod != null) {
                    restartMethod()
                }
            } else {
                showMessage(verifyResult.errorText)
            }
        }
    }

    fun getTextChangeListener(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                number = s.toString()
            }
        }
    }

    fun getOnClickListener() = View.OnClickListener {
        KeyboardUtils.hideKeyboard(this)
        if (!validatePersonalNumber())
            return@OnClickListener

        progress.visibility = View.VISIBLE
        auth(number)
    }

    private fun validatePersonalNumber() = when {
        !(number.length == 10 || number.length == 12) -> {
            showMessage(App.context.getString(R.string.field_is_empty))
            false
        }
        else -> true
    }

    private fun showMessage(text: String) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()
    }

    private fun runBankIdApp(autoStartToken: String) {
        sbidClientStarted = true
        startActivityForResult(BankId.getLoginIntent(autoStartToken), BANK_ID_AUTH_CODE)
    }

    private fun auth(number: String) {
        if (token.isEmpty()) {
            return
        }
        (Methods.AUTHENTICATE + number)
                .httpGet()
                .authenticate(token, ANY_PASSWORD)
                .responseObject(Authenticate.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    when {
                        verifyResult.isOk -> {
                            val orderRef = verifyResult.value?.orderRef ?: ""
                            SharedData.ORDER_REF.saveString(orderRef)

                            val autoStartToken = verifyResult.value?.autoStartToken ?: ""
                            SharedData.AUTO_START_TOKEN.saveString(autoStartToken)

                            runBankIdApp(autoStartToken)
                        }
                        verifyResult.status == UNAUTHORIZED -> loadToken { auth(number) }
                        else -> showMessage(verifyResult.errorText)
                    }

                    progress.visibility = View.GONE
                }
    }

    private fun collect(orderRef: String) {
        progress.visibility = View.VISIBLE
        (Methods.COLLECT + orderRef)
                .httpGet()
                .authenticate(token, ANY_PASSWORD)
                .responseObject(CollectData.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    when {
                        verifyResult.isOk -> {

                            SharedData.OCSP_RESPONSE.saveString(verifyResult.value?.ocspResponse ?: "")
                            SharedData.PROGRESS_STATUS.saveString(verifyResult.value?.progressStatus ?: "")
                            SharedData.SIGNATURE.saveString(verifyResult.value?.signature ?: "")

                            val userInfo = verifyResult.value?.userInfo

                            SharedData.GIVEN_NAME.saveString(userInfo?.givenName ?: "")
                            SharedData.NAME.saveString(userInfo?.name ?: "")
                            SharedData.SURNAME.saveString(userInfo?.surname?: "")
                            SharedData.PERSONAL_NUMBER.saveString(userInfo?.personalNumber?: "")

                            if (verifyResult.value?.progressStatus == "STARTED") {
                                showMessage("Не удалось определить клиента")
                                progress.visibility = View.GONE
                            } else {
                                loginRequest()
                            }
                        }
                        verifyResult.status == UNAUTHORIZED -> loadToken { collect(orderRef) }
                        else -> {
                            showMessage(verifyResult.errorText)
                            progress.visibility = View.GONE
                        }
                    }
                }
    }

    private fun loginRequest() {
        Fuel.post(Methods.LOGIN_REQUEST)
                .header("Content-Type" to "application/json")
                .body(JsonObject().add("idNumber", SharedData.PERSONAL_NUMBER.getString())
                        .add("firstName", SharedData.GIVEN_NAME.getString())
                        .add("lastName", SharedData.SURNAME.getString()).toString())
                .authenticate(token, ANY_PASSWORD)
                .responseObject(UserData.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    when {
                        verifyResult.value?.idNumber?.isEmpty() == true -> showMessage("Не удалось определить клиента")
                        verifyResult.isOk -> {
                            DataBase.saveUser(verifyResult.value)
                            exitLogin()
                        }
                        verifyResult.status == UNAUTHORIZED -> loadToken { loginRequest() }
                        else -> showMessage(verifyResult.errorText)
                    }
                    progress.visibility = View.GONE
                }
    }

    override fun onBackPressed() {
        KeyboardUtils.hideKeyboard(this)
        super.onBackPressed()
        backToMain()
        Thread.sleep(500)
        finish()
    }

    private fun exitLogin() {
        val user = DataBase.getUser()
        if (user?.confirmed == false) {
            startActivityForResult(Intent(this@LoginActivity, ConfirmEmailActivity::class.java), REQUEST_CODE_CONFIRM_EMAIL)
        } else {
            backToMain()
        }
//        Thread.sleep(500)
        finish()
    }

    private fun backToMain() {
        if (DataBase.getUser() == null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BANK_ID_AUTH_CODE && sbidClientStarted) {
            sbidClientStarted = false

            val orderRef = SharedData.ORDER_REF.getString()
            if ((resultCode == Activity.RESULT_CANCELED || resultCode == Activity.RESULT_OK) && orderRef.isNotEmpty()) {
                collect(orderRef)
            }
        }
    }
}
