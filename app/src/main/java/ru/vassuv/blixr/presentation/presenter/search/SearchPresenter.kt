package ru.vassuv.blixr.presentation.presenter.search

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import ru.vassuv.blixr.presentation.view.auth.SearchView
import ru.vassuv.blixr.repository.*
import ru.vassuv.blixr.repository.api.Fields
import ru.vassuv.blixr.repository.api.Methods
import ru.vassuv.blixr.repository.db.CODE_STRING
import ru.vassuv.blixr.repository.db.DataBase
import ru.vassuv.blixr.repository.db.EMAIL
import ru.vassuv.blixr.repository.db.UID
import ru.vassuv.blixr.repository.response.DocumentList
import ru.vassuv.blixr.utils.ATLibriry.Logger
import ru.vassuv.blixr.utils.ATLibriry.Router
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject
import ru.vassuv.blixr.utils.ATLibriry.json.JsonValue
import ru.vassuv.blixr.utils.STATUS_OK
import ru.vassuv.blixr.utils.UNAUTHORIZED
import ru.vassuv.blixr.utils.verifyResult

@InjectViewState
class SearchPresenter : MvpPresenter<SearchView>() {

    private var token: String = ""

    private var code = ""

    fun getOnClickListener() = View.OnClickListener {
        searchDocument()
    }

    fun getTextChangeListener() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            code = s.toString()
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
                        token = JsonValue.readFrom(verifyResult.value?:"").asObject()
                                .get(Fields.TOKEN)?.asString() ?: "";
                        SharedData.TOKEN.saveString(token)

                        if (restartMethod != null) {
                            restartMethod()
                        }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

    private fun searchDocument() {
        Methods.GET_CONTRACT_FROM_CODE.httpPost()
                .header(JSON_HEADER)
                .authenticate(token, ANY_PASSWORD)
                .body(JsonObject().add(CODE_STRING, code).toString())
                .responseObject(DocumentList.Deserializer()) { request, response, result ->
                    Logger.trace(request)
                    Logger.trace(response)
                    Logger.trace(request.cUrlString())

                    val verifyResult = verifyResult(result)
                    if (verifyResult.isOk) {
                        if (STATUS_OK == verifyResult.status) {
                            viewState.showSuccessAlert()
                            DataBase.saveDraftDocument(verifyResult.value)
                        } else {
                            viewState.showErrorAlert(code)
                        }
                    } else if (verifyResult.status == UNAUTHORIZED) {
                        loadToken { searchDocument() }
                    } else {
                        Router.showMessage(verifyResult.errorText)
                    }
                }
    }

}
