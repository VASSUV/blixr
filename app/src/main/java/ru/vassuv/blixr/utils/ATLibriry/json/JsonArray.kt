package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException
import java.io.Reader
import java.util.*

class JsonArray : JsonValue, Iterable<JsonValue> {

    private val values: MutableList<JsonValue>

    constructor() {
        values = ArrayList<JsonValue>()
    }

    constructor(array: JsonArray, unmodifiable: Boolean = false) {
        if (unmodifiable) {
            values = Collections.unmodifiableList(array.values)
        } else {
            values = ArrayList(array.values)
        }
    }

    fun add(value: Int): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: Long): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: Float): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: Double): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: Boolean): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: String): JsonArray {
        values.add(valueOf(value))
        return this
    }

    fun add(value: JsonValue): JsonArray {
        values.add(value)
        return this
    }

    operator fun set(index: Int, value: Int): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: Long): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: Float): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: Double): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: Boolean): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: String): JsonArray {
        values[index] = valueOf(value)
        return this
    }

    operator fun set(index: Int, value: JsonValue): JsonArray {
        values[index] = value
        return this
    }

    fun remove(index: Int): JsonArray {
        values.removeAt(index)
        return this
    }

    fun size(): Int = values.size

    val isEmpty: Boolean
        get() = values.isEmpty()

    operator fun get(index: Int) = values[index]

    fun values() = Collections.unmodifiableList(values)

    override fun iterator(): Iterator<JsonValue> {
        val iterator = values.iterator()
        return object : Iterator<JsonValue> {

            override fun hasNext() = iterator.hasNext()

            override fun next() = iterator.next()
        }
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter) {
        writer.writeArray(this)
    }

    override val isArray: Boolean
        get() = true

    override fun asArray() = this

    override fun hashCode() = values.hashCode()

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
        return values == (other as JsonArray?)!!.values
    }

    companion object {

        @Throws(IOException::class)
        fun readFrom(reader: Reader) = JsonValue.readFrom(reader).asArray()

        fun readFrom(string: String) = JsonValue.readFrom(string).asArray()

        fun unmodifiableArray(array: JsonArray) = JsonArray(array, true)
    }

}