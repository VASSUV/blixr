package ru.vassuv.blixr.presentation.presenter.auth

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.kittinunf.fuel.httpGet
import ru.vassuv.blixr.App
import ru.vassuv.blixr.R
import ru.vassuv.blixr.presentation.view.auth.AuthView
import ru.vassuv.blixr.repository.SharedData
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.BankId
import ru.vassuv.blixr.utils.verifyResult

@InjectViewState
class AuthPresenter : MvpPresenter<AuthView>() {
    val BANK_ID_AUTH_CODE = 199
    private var number: String = ""
    private var sbidClientStarted: Boolean = false

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
        if (!validatePersonalNumber()) return@OnClickListener

        val autoStartToken = SharedData.AUTO_START_TOKEN.getString()
        if (autoStartToken.isNotEmpty()) {
            runBankIdApp(autoStartToken)
        } else {
            auth(number)
        }
    }

    private fun validatePersonalNumber() = when {
        !(number.length == 10 || number.length == 12) -> {
            Router.showMessage(App.context.getString(R.string.field_is_empty))
            false
        }
        else -> true
    }

    private fun runBankIdApp(autoStartToken: String) {
        sbidClientStarted = true
        viewState.startActivityForResult(BankId.getLoginIntent(autoStartToken), BANK_ID_AUTH_CODE)
    }

    private fun auth(number: String = SharedData.USER_NUMBER.getString()) {
        (Methods.AUTHENTICATE + number)
                .httpGet()
                .authenticate("montrollBring", "UITableView_up2")
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {

                        val orderRef = JsonValue.readFrom(verifyResult.value).string(Fields.ORDER_REF) ?: ""
                        SharedData.ORDER_REF.saveString(orderRef)

                        val autoStartToken = JsonValue.readFrom(verifyResult.value).string(Fields.AUTO_START_TOKEN) ?: ""
                        SharedData.AUTO_START_TOKEN.saveString(autoStartToken)

                        runBankIdApp(autoStartToken)

                        Router.showMessage(orderRef)
                    } else {
                        Router.showMessage(verifyResult.value)
                    }
                }
    }

    private fun orderRef(orderRef: String) {
        (Methods.ORDER_REF)
                .httpGet()//listOf(Fields.ORDER_REF to orderRef)
                .authenticate("montrollBring", "UITableView_up2")
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        Router.showMessage(verifyResult.value)
                    } else {
                        Router.showMessage(verifyResult.value)
                    }
                }
    }

    fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BANK_ID_AUTH_CODE && sbidClientStarted) {
            sbidClientStarted = false

            val orderRef = SharedData.ORDER_REF.getString()
            if ((resultCode == RESULT_CANCELED || resultCode == RESULT_OK) && orderRef.isNotEmpty()) {
                orderRef(orderRef)
            }
        }
    }

//    private fun token() {
//        Methods.TOKEN.httpGet().authenticate("montrollBring", "UITableView_up2").responseString { request, response, result ->
//            Logger.trace(request)
//            Logger.trace(response)
//
//            val verifyResult = verifyResult(result)
//            if(verifyResult.isOk) {
//                val token = JsonValue.readFrom(verifyResult.value).asObject().get(Fields.TOKEN)?.asString()
//                        ?: "sdfsdf"
//                auth(token)
//            } else {
//                Toast.makeText(this, verifyResult.value, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}
