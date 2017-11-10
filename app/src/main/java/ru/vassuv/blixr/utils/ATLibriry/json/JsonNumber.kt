package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException


internal class JsonNumber(private val string: String) : JsonValue() {

    override fun toString() = string

    @Throws(IOException::class)
    override fun write(writer: JsonWriter) {
        writer.write(string)
    }

    override val isNumber: Boolean
        get() = true

    override fun asInt() = Integer.parseInt(string, 10)

    override fun asLong() = java.lang.Long.parseLong(string, 10)

    override fun asFloat() = java.lang.Float.parseFloat(string)

    override fun asDouble() = java.lang.Double.parseDouble(string)

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        return string == (other as JsonNumber?)!!.string
    }

}

