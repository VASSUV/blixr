package ru.vassuv.blixr.utils.ATLibriry

import android.util.Log
import ru.vassuv.blixr.BuildConfig

object Logger{
    var debugMode: Boolean = BuildConfig.DEBUG
    private var tag: String = "ATLibrary"

    fun trace(vararg args: Any) {
        if (debugMode) Log.d(tag, varargToString(args))
    }

    fun traceException(text: String, exception: Throwable) {
        if (debugMode) {
            Log.d(tag, "$text: $exception")
            exception.stackTrace.forEach { Log.d(tag, ("|   " + it.toString())) }
        }
    }

    private fun varargToString(args: Array<*>): String {
        return args.joinToString(separator = " ") {
            when (it) {
                is Array<*> -> varargToString(it)
                else -> it.toString()
            }
        }
    }
}