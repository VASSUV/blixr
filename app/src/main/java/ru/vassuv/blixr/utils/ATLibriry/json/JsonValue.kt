package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.*
import java.lang.Double.isInfinite
import java.lang.Double.isNaN
import java.lang.Float.isInfinite
import java.lang.Float.isNaN

abstract class JsonValue : Serializable {
    open val isObject: Boolean
        get() = false

    open val isArray: Boolean
        get() = false

    open val isNumber: Boolean
        get() = false

    open val isString: Boolean
        get() = false

    open val isBoolean: Boolean
        get() = false

    open val isTrue: Boolean
        get() = false

    open val isFalse: Boolean
        get() = false

    open val isNull: Boolean
        get() = false

    open fun int(field: String) = if (this.isObject) asObject()[field]?.asInt() else null
    open fun long(field: String) = if (this.isObject) asObject()[field]?.asLong() else null
    open fun float(field: String) = if (this.isObject) asObject()[field]?.asFloat() else null
    open fun double(field: String) = if (this.isObject) asObject()[field]?.asDouble() else null
    open fun string(field: String) = if (this.isObject) asObject()[field]?.asString() else null
    open fun array(field: String) = if (this.isObject) asObject()[field]?.asArray() else null
    open fun boolean(field: String) = if (this.isObject) asObject()[field]?.asBoolean() else null
    open fun obj(field: String) = if (this.isObject) asObject()[field]?.asObject() else null

    open fun int() = if (this.isNumber) asInt() else null
    open fun long() = if (this.isNumber) asLong() else null
    open fun float() = if (this.isNumber) asFloat() else null
    open fun double() = if (this.isNumber) asDouble() else null
    open fun string() = if (this.isString) asString() else null
    open fun array() = if (this.isArray) asArray() else null
    open fun boolean() = if (this.isBoolean) asBoolean() else null


    @Throws(IOException::class)
    fun writeTo(writer: Writer) {
        write(JsonWriter(writer))
    }

    @Throws(IOException::class)
    override fun toString(): String {
        val stringWriter = StringWriter()
        val jsonWriter = JsonWriter(stringWriter)
        try {
            write(jsonWriter)
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }

        return stringWriter.toString()
    }

    override fun equals(other: Any?) = super.equals(other)

    override fun hashCode() = super.hashCode()

    @Throws(IOException::class)
    abstract fun write(writer: JsonWriter)

    open fun asObject(): JsonObject {
        throw UnsupportedOperationException("Not an object: " + toString())
    }

    open fun asArray(): JsonArray {
        throw UnsupportedOperationException("Not an array: " + toString())
    }

    open fun asInt(): Int {
        throw UnsupportedOperationException("Not a number: " + toString())
    }

    open fun asLong(): Long {
        throw UnsupportedOperationException("Not a number: " + toString())
    }

    open fun asFloat(): Float {
        throw UnsupportedOperationException("Not a number: " + toString())
    }

    open fun asDouble(): Double {
        throw UnsupportedOperationException("Not a number: " + toString())
    }

    open fun asString(): String {
        throw UnsupportedOperationException("Not a string: " + toString())
    }

    open fun asBoolean(): Boolean {
        throw UnsupportedOperationException("Not a boolean: " + toString())
    }

    companion object {

        val TRUE: JsonValue = JsonLiteral("true")

        val FALSE: JsonValue = JsonLiteral("false")

        val NULL: JsonValue = JsonLiteral("null")

        @Throws(IOException::class)
        fun readFrom(reader: Reader): JsonValue {
            return JsonParser(reader).parse()
        }

        @Throws(IOException::class)
        fun readFrom(text: String): JsonValue {
            try {
                return JsonParser(text).parse()
            } catch (exception: IOException) {
                throw RuntimeException(exception)
            }
        }

        fun valueOf(value: Int): JsonValue = JsonNumber(value.toString())

        fun valueOf(value: Long): JsonValue = JsonNumber(value.toString())

        fun valueOf(value: Float): JsonValue {
            if (isInfinite(value) || isNaN(value)) {
                throw IllegalArgumentException("Infinite and NaN values not permitted in JSON")
            }
            return JsonNumber(value.toString().cutOffPointZero())
        }

        fun valueOf(value: Double): JsonValue {
            if (isInfinite(value) || isNaN(value)) {
                throw IllegalArgumentException("Infinite and NaN values not permitted in JSON")
            }
            return JsonNumber(value.toString().cutOffPointZero())
        }

        fun valueOf(string: String?): JsonValue = if (string == null) NULL else JsonString(string)

        fun valueOf(value: Boolean) = if (value) TRUE else FALSE

        private fun String.cutOffPointZero() = if (endsWith(".0")) substring(0, length - 2) else this

    }

}