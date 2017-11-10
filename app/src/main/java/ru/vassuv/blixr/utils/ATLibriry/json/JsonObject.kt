package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException
import java.io.ObjectInputStream
import java.io.Reader
import java.util.*

class JsonObject (other: JsonObject? = null, unmodifiable: Boolean = false) : JsonValue(), Iterable<JsonObject.Member> {

    private val names: MutableList<String>

    private val values: MutableList<JsonValue>

    @Transient private var table: HashIndexTable


    fun add(name: String, value: Int): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String, value: Long): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String, value: Float): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String, value: Double): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String, value: Boolean): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String, value: String): JsonObject {
        add(name, valueOf(value))
        return this
    }

    fun add(name: String?, value: JsonValue?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        if (value == null) {
            throw NullPointerException("value is null")
        }
        table.add(name, names.size)
        names.add(name)
        values.add(value)
        return this
    }

    operator fun set(name: String, value: Int): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String, value: Long): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String, value: Float): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String, value: Double): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String, value: Boolean): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String, value: String): JsonObject {
        set(name, valueOf(value))
        return this
    }

    operator fun set(name: String?, value: JsonValue?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        if (value == null) {
            throw NullPointerException("value is null")
        }
        val index = indexOf(name)
        if (index != -1) {
            values[index] = value
        } else {
            table.add(name, names.size)
            names.add(name)
            values.add(value)
        }
        return this
    }

    fun remove(name: String?): JsonObject {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        val index = indexOf(name)
        if (index != -1) {
            table.remove(index)
            names.removeAt(index)
            values.removeAt(index)
        }
        return this
    }

    operator fun get(name: String?): JsonValue? {
        if (name == null) {
            throw NullPointerException("name is null")
        }
        val index = indexOf(name)
        return if (index != -1) values[index] else null
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter) {
        writer.writeObject(this)
    }

    fun size() = names.size

    val isEmpty: Boolean
        get() = names.isEmpty()

    override val isObject: Boolean
        get() = true

    fun names() = Collections.unmodifiableList(names)

    override fun iterator(): Iterator<Member> {
        val namesIterator = names.iterator()
        val valuesIterator = values.iterator()

        return object : Iterator<Member> {
            override fun hasNext() = namesIterator.hasNext()
            override fun next() = Member(namesIterator.next(), valuesIterator.next())
        }
    }

    override fun asObject() = this

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + names.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }

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
        val jsonObject = other as JsonObject?
        return names == jsonObject!!.names && values == jsonObject.values
    }

    internal fun indexOf(name: String): Int {
        val index = table[name]
        if (index != -1 && name == names[index]) {
            return index
        }
        return names.lastIndexOf(name)
    }

    @Synchronized @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        inputStream.defaultReadObject()
        table = HashIndexTable()
        updateHashIndex()
    }

    private fun updateHashIndex() {
        val size = names.size
        for (i in 0..size - 1) {
            table.add(names[i], i)
        }
    }

    class Member internal constructor(

            val name: String,

            val value: JsonValue) {

        override fun hashCode(): Int {
            var result = 1
            result = 31 * result + name.hashCode()
            result = 31 * result + value.hashCode()
            return result
        }

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
            val member = other as Member?
            return name == member!!.name && value == member.value
        }

    }

    internal class HashIndexTable {
        private val hashTable = ByteArray(32)

        constructor()

        constructor(original: HashIndexTable) {
            System.arraycopy(original.hashTable, 0, hashTable, 0, hashTable.size)
        }

        fun add(name: String, index: Int) {
            hashTable[hashSlotFor(name)] = if (index < 0xff) (index + 1).toByte() else 0
        }

        fun remove(index: Int) {
            for (i in hashTable.indices) {
                if (hashTable[i].toInt() == index + 1) {
                    hashTable[i] = 0
                } else if (hashTable[i] > index + 1) {
                    hashTable[i]--
                }
            }
        }

        operator fun get(name: Any) = (hashTable[hashSlotFor(name)].toInt() and 0xff) - 1

        private fun hashSlotFor(element: Any) = element.hashCode() and hashTable.size - 1

    }

    companion object {

        @Throws(IOException::class)
        fun readFrom(reader: Reader) = JsonValue.readFrom(reader).asObject()

        fun readFrom(string: String) = JsonValue.readFrom(string).asObject()

        fun unmodifiableObject(other: JsonObject) = JsonObject(other, true)
    }

    init {
        if (other == null) {
            names = ArrayList<String>()
            values = ArrayList<JsonValue>()
        } else {
            if (unmodifiable) {
                names = Collections.unmodifiableList(other.names)
                values = Collections.unmodifiableList(other.values)
            } else {
                names = ArrayList(other.names)
                values = ArrayList(other.values)
            }
        }
        table = HashIndexTable()
        updateHashIndex()
    }

}
