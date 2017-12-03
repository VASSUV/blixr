package ru.vassuv.blixr.presentation.presenter.share

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import ru.vassuv.blixr.presentation.view.share.ShareView
import ru.vassuv.blixr.repository.*
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.EMAIL
import ru.vassuv.blixr.repository.db.UID
import ru.vassuv.blixr.repository.db.User
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router.showMessage
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult

@InjectViewState
class SharePresenter : MvpPresenter<ShareView>() {

    private var textEmail: String = ""
    private var user: User? = null
    private var token: String = ""

    fun onStart() {
        token = SharedData.TOKEN.getString()
        if (token.isEmpty()) {
            loadToken()
        }
        user = DataBase.getUser()
    }

    fun getOnClickListener() = View.OnClickListener {
        if (user != null) {
            referalFriend()
        } else {

        }
    }

    fun getEmailTextWatcher(): TextWatcher? = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textEmail = s.toString()
        }

    }

    private fun loadToken(restartMethod: (() -> Unit)? = null) {
        Methods.TOKEN.httpGet()
                .authenticate(SessionConfig.USER_NAME, SessionConfig.USER_PASSWORD)
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        token = JsonValue.readFrom(verifyResult.value ?: "").asObject()
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

    private fun referalFriend() {
        (Methods.REFERRAL).httpPost()
                .header(JSON_HEADER)
                .authenticate(token, ANY_PASSWORD)
                .body(JsonObject().add(UID, user!!.id.toString())
                        .add(EMAIL, textEmail).toString())
                .responseString { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        if (OK == JsonObject.readFrom(verifyResult.value ?: "").string(Fields.STATUS)) {
                            viewState.showSuccessAlert()
                        } else {
                            viewState.showErrorAlert()
                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { referalFriend() }
                    } else {
                        showMessage(verifyResult.errorText)
                    }
                }
    }

}
