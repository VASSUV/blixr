package ru.vassuv.blixr.utils.ATLibriry.json

import java.io.IOException
import java.io.Reader
import java.io.StringReader


internal class JsonParser @JvmOverloads constructor(private val reader: Reader, buffersize: Int = DEFAULT_BUFFER_SIZE) {
    private val buffer: CharArray = CharArray(buffersize)
    private var bufferOffset: Int = 0
    private var index: Int = 0
    private var fill: Int = 0
    private var line: Int = 1
    private var lineOffset: Int = 0
    private var current: Int = 0
    private var captureBuffer: StringBuilder = StringBuilder()
    private var captureStart: Int = -1

    /*
   * |                      bufferOffset
   *                        v
   * [a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t]        < input
   *                       [l|m|n|o|p|q|r|s|t|?|?]    < buffer
   *                          ^               ^
   *                       |  index           fill
   */

    constructor(string: String) : this(StringReader(string),
            Math.max(MIN_BUFFER_SIZE, Math.min(DEFAULT_BUFFER_SIZE, string.length)))

    @Throws(IOException::class)
    fun parse(): JsonValue {
        read()
        skipWhiteSpace()
        val result = readValue()
        skipWhiteSpace()
        if (!isEndOfText) {
            throw error("Unexpected character")
        }
        return result
    }

    @Throws(IOException::class)
    private fun readValue() = when (current.toChar()) {
        'n' -> readNull()
        't' -> readTrue()
        'f' -> readFalse()
        '"' -> readString()
        '[' -> readArray()
        '{' -> readObject()
        '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber()
        else -> throw expected("value")
    }

    @Throws(IOException::class)
    private fun readArray(): JsonArray {
        read()
        val array = JsonArray()
        skipWhiteSpace()
        if (readChar(']')) {
            return array
        }
        do {
            skipWhiteSpace()
            array.add(readValue())
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar(']')) {
            throw expected("',' or ']'")
        }
        return array
    }

    @Throws(IOException::class)
    private fun readObject(): JsonObject {
        read()
        val `object` = JsonObject()
        skipWhiteSpace()
        if (readChar('}')) {
            return `object`
        }
        do {
            skipWhiteSpace()
            val name = readName()
            skipWhiteSpace()
            if (!readChar(':')) {
                throw expected("':'")
            }
            skipWhiteSpace()
            `object`.add(name, readValue())
            skipWhiteSpace()
        } while (readChar(','))
        if (!readChar('}')) {
            throw expected("',' or '}'")
        }
        return `object`
    }

    @Throws(IOException::class)
    private fun readName(): String {
        if (current != '"'.toInt()) {
            throw expected("name")
        }
        return readStringInternal()
    }

    @Throws(IOException::class)
    private fun readNull(): JsonValue {
        read()
        readRequiredChar('u')
        readRequiredChar('l')
        readRequiredChar('l')
        return JsonValue.NULL
    }

    @Throws(IOException::class)
    private fun readTrue(): JsonValue {
        read()
        readRequiredChar('r')
        readRequiredChar('u')
        readRequiredChar('e')
        return JsonValue.TRUE
    }

    @Throws(IOException::class)
    private fun readFalse(): JsonValue {
        read()
        readRequiredChar('a')
        readRequiredChar('l')
        readRequiredChar('s')
        readRequiredChar('e')
        return JsonValue.FALSE
    }

    @Throws(IOException::class)
    private fun readRequiredChar(ch: Char) {
        if (!readChar(ch)) {
            throw expected("'$ch'")
        }
    }

    @Throws(IOException::class)
    private fun readString(): JsonValue {
        return JsonString(readStringInternal())
    }

    @Throws(IOException::class)
    private fun readStringInternal(): String {
        read()
        startCapture()
        while (current != '"'.toInt()) {
            if (current == '\\'.toInt()) {
                pauseCapture()
                readEscape()
                startCapture()
            } else if (current < 0x20) {
                throw expected("valid string character")
            } else {
                read()
            }
        }
        val string = endCapture()
        read()
        return string
    }

    @Throws(IOException::class)
    private fun readEscape() {
        read()
        when (current.toChar()) {
            '"', '/', '\\' -> captureBuffer.append(current.toChar())
            'b' -> captureBuffer.append('\b')
//            'f' -> captureBuffer.append('\f')
            'n' -> captureBuffer.append('\n')
            'r' -> captureBuffer.append('\r')
            't' -> captureBuffer.append('\t')
            'u' -> {
                val hexChars = CharArray(4)
                for (i in 0..3) {
                    read()
                    if (!isHexDigit) {
                        throw expected("hexadecimal digit")
                    }
                    hexChars[i] = current.toChar()
                }
                captureBuffer.append(Integer.parseInt(String(hexChars), 16).toChar())
            }
            else -> throw expected("valid escape sequence")
        }
        read()
    }

    @Throws(IOException::class)
    private fun readNumber(): JsonValue {
        startCapture()
        readChar('-')
        val firstDigit = current
        if (!readDigit()) {
            throw expected("digit")
        }
        if (firstDigit != '0'.toInt()) {
            while (readDigit()) {
            }
        }
        readFraction()
        readExponent()
        return JsonNumber(endCapture())
    }

    @Throws(IOException::class)
    private fun readFraction(): Boolean {
        if (!readChar('.')) {
            return false
        }
        if (!readDigit()) {
            throw expected("digit")
        }
        while (readDigit()) {
        }
        return true
    }

    @Throws(IOException::class)
    private fun readExponent(): Boolean {
        if (!readChar('e') && !readChar('E')) {
            return false
        }
        if (!readChar('+')) {
            readChar('-')
        }
        if (!readDigit()) {
            throw expected("digit")
        }
        while (readDigit()) {
        }
        return true
    }

    @Throws(IOException::class)
    private fun readChar(ch: Char): Boolean {
        if (current != ch.toInt()) {
            return false
        }
        read()
        return true
    }

    @Throws(IOException::class)
    private fun readDigit(): Boolean {
        if (!isDigit) {
            return false
        }
        read()
        return true
    }

    @Throws(IOException::class)
    private fun skipWhiteSpace() {
        while (isWhiteSpace) {
            read()
        }
    }

    @Throws(IOException::class)
    private fun read() {
        if (isEndOfText) {
            throw error("Unexpected end of input")
        }
        if (index == fill) {
            if (captureStart != -1) {
                captureBuffer.append(buffer, captureStart, fill - captureStart)
                captureStart = 0
            }
            bufferOffset += fill
            fill = reader.read(buffer, 0, buffer.size)
            index = 0
            if (fill == -1) {
                current = -1
                return
            }
        }
        if (current == '\n'.toInt()) {
            line++
            lineOffset = bufferOffset + index
        }
        current = buffer[index++].toInt()
    }

    private fun startCapture() {
        captureStart = index - 1
    }

    private fun pauseCapture() {
        val end = if (current == -1) index else index - 1
        captureBuffer.append(buffer, captureStart, end - captureStart)
        captureStart = -1
    }

    private fun endCapture(): String {
        val end = if (current == -1) index else index - 1
        val captured: String
        if (captureBuffer.isNotEmpty()) {
            captureBuffer.append(buffer, captureStart, end - captureStart)
            captured = captureBuffer.toString()
            captureBuffer.setLength(0)
        } else {
            captured = String(buffer, captureStart, end - captureStart)
        }
        captureStart = -1
        return captured
    }

    private fun expected(expected: String): ParseException {
        if (isEndOfText) {
            return error("Unexpected end of input")
        }
        return error("Expected " + expected)
    }

    private fun error(message: String): ParseException {
        val absIndex = bufferOffset + index
        val column = absIndex - lineOffset
        val offset = if (isEndOfText) absIndex else absIndex - 1
        return ParseException(message, offset, line, column - 1)
    }

    private val isWhiteSpace: Boolean
        get() = current == ' '.toInt() || current == '\t'.toInt() || current == '\n'.toInt() || current == '\r'.toInt()

    private val isDigit: Boolean
        get() = current >= '0'.toInt() && current <= '9'.toInt()

    private val isHexDigit: Boolean
        get() = current >= '0'.toInt() && current <= '9'.toInt()
                || current >= 'a'.toInt() && current <= 'f'.toInt()
                || current >= 'A'.toInt() && current <= 'F'.toInt()

    private val isEndOfText: Boolean
        get() = current == -1

    companion object {

        private val MIN_BUFFER_SIZE = 10
        private val DEFAULT_BUFFER_SIZE = 1024
    }

}
