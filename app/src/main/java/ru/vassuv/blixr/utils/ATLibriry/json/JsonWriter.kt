package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException
import java.io.Writer


class JsonWriter(private val writer: Writer) {

    @Throws(IOException::class)
    fun write(string: String) {
        writer.write(string)
    }

    @Throws(IOException::class)
    fun writeString(string: String) {
        writer.write('"'.toInt())
        val length = string.length
        var start = 0
        val chars = CharArray(length)
        string.toCharArray(chars, 0, 0, length)
        for (index in 0..length - 1) {
            val replacement = getReplacementChars(chars[index])
            if (replacement != null) {
                writer.write(chars, start, index - start)
                writer.write(replacement)
                start = index + 1
            }
        }
        writer.write(chars, start, length - start)
        writer.write('"'.toInt())
    }

    @Throws(IOException::class)
    fun writeObject(`object`: JsonObject) {
        writeBeginObject()
        var first = true
        for (member in `object`) {
            if (!first) {
                writeObjectValueSeparator()
            }
            writeString(member.name)
            writeNameValueSeparator()
            member.value.write(this)
            first = false
        }
        writeEndObject()
    }

    @Throws(IOException::class)
    private fun writeBeginObject() {
        writer.write('{'.toInt())
    }

    @Throws(IOException::class)
    private fun writeEndObject() {
        writer.write('}'.toInt())
    }

    @Throws(IOException::class)
    private fun writeNameValueSeparator() {
        writer.write(':'.toInt())
    }

    @Throws(IOException::class)
    private fun writeObjectValueSeparator() {
        writer.write(','.toInt())
    }

    @Throws(IOException::class)
    fun writeArray(array: JsonArray) {
        writeBeginArray()
        var first = true
        for (value in array) {
            if (!first) {
                writeArrayValueSeparator()
            }
            value.write(this)
            first = false
        }
        writeEndArray()
    }

    @Throws(IOException::class)
    private fun writeBeginArray() {
        writer.write('['.toInt())
    }

    @Throws(IOException::class)
    private fun writeEndArray() {
        writer.write(']'.toInt())
    }

    @Throws(IOException::class)
    private fun writeArrayValueSeparator() {
        writer.write(','.toInt())
    }

    companion object {
        private val CONTROL_CHARACTERS_END = 0x001f

        private val QUOT_CHARS = charArrayOf('\\', '"')
        private val BS_CHARS = charArrayOf('\\', '\\')
        private val LF_CHARS = charArrayOf('\\', 'n')
        private val CR_CHARS = charArrayOf('\\', 'r')
        private val TAB_CHARS = charArrayOf('\\', 't')
        // In JavaScript, U+2028 and U+2029 characters count as line endings and must be encoded.
        // http://stackoverflow.com/questions/2965293/javascript-parse-error-on-u2028-unicode-character
        private val UNICODE_2028_CHARS = charArrayOf('\\', 'u', '2', '0', '2', '8')
        private val UNICODE_2029_CHARS = charArrayOf('\\', 'u', '2', '0', '2', '9')
        private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        private fun getReplacementChars(ch: Char): CharArray? {
            var replacement: CharArray? = null
            when {
                ch == '"' -> replacement = QUOT_CHARS
                ch == '\\' -> replacement = BS_CHARS
                ch == '\n' -> replacement = LF_CHARS
                ch == '\r' -> replacement = CR_CHARS
                ch == '\t' -> replacement = TAB_CHARS
                ch == '\u2028' -> replacement = UNICODE_2028_CHARS
                ch == '\u2029' -> replacement = UNICODE_2029_CHARS
                ch.toInt() <= CONTROL_CHARACTERS_END -> {
                    replacement = charArrayOf('\\', 'u', '0', '0', '0', '0')
                    replacement[4] = HEX_DIGITS[ch.toInt() shr 4 and 0x000f]
                    replacement[5] = HEX_DIGITS[ch.toInt() and 0x000f]
                }
            }
            return replacement
        }
    }

}
