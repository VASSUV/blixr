package ru.vassuv.blixr.presentation.presenter.template

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittiunf.fuel.jackson.responseObject
import ru.vassuv.blixr.presentation.view.template.BlocketView
import ru.vassuv.blixr.repository.*
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.*
import ru.vassuv.blixr.repository.response.FetchBlocketAd
import ru.vassuv.blixr.repository.response.Token
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult

@InjectViewState
class BlocketPresenter : MvpPresenter<BlocketView>() {

    private var textUrl: String = ""
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
            fetchBlocketAd()
        } else {

        }
    }

    fun getUrlTextWatcher(): TextWatcher? = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textUrl = s.toString()
        }

    }

    private fun loadToken(restartMethod: (() -> Unit)? = null) {
        Methods.TOKEN.httpGet()
                .authenticate(SessionConfig.USER_NAME, SessionConfig.USER_PASSWORD)
                .responseObject(Token.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        token = verifyResult.value?.token ?: ""
                        SharedData.TOKEN.saveString(token)

                        if (restartMethod != null) {
                            restartMethod()
                        }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

    private fun fetchBlocketAd() {
        (Methods.FETCH_BLOCKET_AD).httpPost()
                .header(JSON_HEADER)
                .authenticate(token, ANY_PASSWORD)
                .body(JsonObject().add(FETCH_URL, textUrl).toString())
                .responseObject(FetchBlocketAd.Deserializer()){ request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
//                        if (OK == verifyResult.status) {
//                            viewState.showSuccessAlert()
//                        } else {
//                            viewState.showErrorAlert()
//                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { fetchBlocketAd() }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

}
