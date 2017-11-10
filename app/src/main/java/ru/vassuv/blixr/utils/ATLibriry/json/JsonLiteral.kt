package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException

internal class JsonLiteral(private val value: String) : JsonValue() {

    @Throws(IOException::class)
    override fun write(writer: JsonWriter) {
        writer.write(value)
    }

    override fun toString() = value

    override fun asString() = value

    override fun asBoolean() = if (isBoolean) isTrue else super.asBoolean()

    override val isNull: Boolean
        get() = this === NULL

    override val isBoolean: Boolean
        get() = this === TRUE || this === FALSE

    override val isTrue: Boolean
        get() = this === TRUE

    override val isFalse: Boolean
        get() = this === FALSE

    override fun hashCode() = value.hashCode()

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
        return value == (other as JsonLiteral?)!!.value
    }

}
