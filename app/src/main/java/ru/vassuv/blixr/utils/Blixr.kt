package ru.vassuv.blixr.utils

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import ru.vassuv.blixr.utils.ATLibriry.Logger

class Error(var value: String = "", var isOk: Boolean = false)

fun verifyResult(result: Result<String, FuelError>): Error = when (result) {
    is Result.Failure<String, FuelError> -> {
        Logger.traceException("verifyResult", result.getException())
        Error("Ошибка")
    }
    is Result.Success<String, FuelError> -> {
        if (result.value.isEmpty()) {
            Error("Ошибка")
        } else if (!result.value.startsWith('{')) {
            when (result.value) {
                "Unauthorized Access" -> Error("Ошибка")
                else -> Error("Ошибка")
            }
        } else {
            Logger.trace(result.value)
            Error(result.value, true)
        }
    }
    else -> Error("Произошла ошибка")
}
