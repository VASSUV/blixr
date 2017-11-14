package ru.vassuv.blixr.utils

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.result.Result
import ru.vassuv.blixr.utils.ATLibriry.Logger
import java.net.UnknownHostException

@JvmField
val STATUS_OK = 0
@JvmField
val INTERNET_ERROR = 1
@JvmField
val UNAUTHORIZED = 401

class Response(val value: String = "", val isOk: Boolean = false, val status: Int = STATUS_OK)

fun verifyResult(result: Result<String, FuelError>): Response = when (result) {
    is Result.Failure<String, FuelError> -> {
        Logger.traceException("verifyResult", result.getException())

        when (result.error.exception) {
            is UnknownHostException -> Response("Отсутствует интернет соединение", status = INTERNET_ERROR)
            is HttpException -> Response("Произошла ошибка", status = result.error.response.statusCode)
            else -> Response("Произошла ошибка")
        }
    }
    is Result.Success<String, FuelError> -> {
        if (result.value.isEmpty()) {
            Response("Произошла ошибка")
        } else if ((result.value.startsWith('{') || result.value.startsWith('['))) {
            Logger.trace(result.value)
            Response(result.value, true)
        } else when (result.value) {
            "Unauthorized Access" -> Response("Unauthorized Access")
            else -> Response("Произошла ошибка")
        }
    }
    else -> Response("Произошла ошибка")
}
