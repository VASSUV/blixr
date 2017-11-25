package ru.vassuv.blixr.utils

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.result.Result
import com.yandex.metrica.YandexMetrica
import ru.vassuv.blixr.utils.ATLibriry.Logger
import java.net.UnknownHostException

@JvmField
val STATUS_OK = 0
@JvmField
val INTERNET_ERROR = 1
@JvmField
val BAD_REQUEST = 400
@JvmField
val UNAUTHORIZED = 401
@JvmField
val CONFLICT = 409

class Response<out T>(val value: T? = null, val errorText: String = "", val isOk: Boolean = false, val status: Int = STATUS_OK)

@Suppress("UNCHECKED_CAST")
fun <T : Any>verifyResult(result: Result<T, FuelError>): Response<T> = when (result) {
    is Result.Failure<T, FuelError> -> checkError(result)
    is Result.Success<T, FuelError> -> {
        if (result.value is String) {
            val resultString = result.value as String
            if (resultString.isEmpty()) {
                Response(errorText = "Произошла ошибка")
            } else if (resultString.startsWith('{') || resultString.startsWith('[')) {
                Logger.trace(resultString)
                Response(resultString as T, isOk = true)
            } else when (result.value) {
                "Unauthorized Access" -> Response(errorText = "Unauthorized Access", status = UNAUTHORIZED)
                else -> Response(errorText = "Произошла ошибка")
            }
        } else {
            Response(result.value, isOk = true)
        }
    }
    else -> Response(errorText = "Произошла ошибка")
}

private fun <T : Any> checkError(result: Result.Failure<T, FuelError>): Response<T> {
    Logger.traceException("verifyResult", result.getException())
    val statusCode = result.error.response.statusCode
    val response: Response<T> = when (statusCode) {
        BAD_REQUEST -> Response(errorText = "Произошла ошибка (BAD_REQUEST)", status = BAD_REQUEST)
        UNAUTHORIZED -> Response(errorText = "Произошла ошибка", status = UNAUTHORIZED)
        CONFLICT -> Response(errorText = "Произошла ошибка (CONFLICT)", status = CONFLICT)
        else -> when (result.error.exception) {
            is UnknownHostException -> Response(errorText = "Отсутствует интернет соединение", status = INTERNET_ERROR)
            is HttpException -> Response(errorText = "Произошла ошибка", status = statusCode)
            else -> Response(errorText = "Произошла ошибка")
        }
    }
    YandexMetrica.reportError("${result.error.response}\n${response.errorText}", result.error.exception)
    return response
}