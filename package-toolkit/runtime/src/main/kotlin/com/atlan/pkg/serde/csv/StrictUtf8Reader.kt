/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.nio.charset.StandardCharsets

/**
 * A streaming [Reader] that decodes bytes as strict UTF-8 and *fails loudly* on the first byte that
 * is not valid UTF-8, rather than silently substituting the Unicode replacement character (U+FFFD)
 * the way the JDK's default decoder does.
 *
 * This is the default decoding for input CSVs: a "successful" import of a clean UTF-8 file is then
 * guaranteed to be byte-perfect, and a file that is *not* clean UTF-8 (for example an Excel-on-Windows
 * cp1252 export) surfaces a clear error naming the offending byte and row -- instead of quietly
 * corrupting the content. Callers that knowingly have a legacy encoding can opt in to the
 * [Utf8WithCp1252FallbackReader] instead (see [CSVEncoding] and the package's input-encoding option).
 *
 * The offending row is reported as the number of newlines decoded so far plus one, i.e. the 1-based
 * line of the file the bad byte falls on. It is approximate in the sense that it counts physical
 * lines, not CSV records (a quoted field may span multiple lines), which is close enough to point a
 * user at the problem area. (CSA-458)
 */
class StrictUtf8Reader(
    input: InputStream,
) : Reader() {
    private val stream = BufferedInputStream(input)

    // Holds decoded characters not yet handed back to the caller (e.g. the low surrogate of an
    // astral code point, or overflow when the caller's buffer is full).
    private val pending = ArrayDeque<Char>()

    // Position tracking, used purely to make the error message actionable.
    private var byteOffset = 0L
    private var linesSeen = 0L

    override fun read(
        cbuf: CharArray,
        off: Int,
        len: Int,
    ): Int {
        if (len == 0) return 0
        var count = 0
        while (count < len) {
            if (pending.isEmpty()) {
                val decoded = decodeNext() ?: break
                decoded.forEach { pending.addLast(it) }
                if (pending.isEmpty()) break
            }
            cbuf[off + count] = pending.removeFirst()
            count++
        }
        return if (count == 0) -1 else count
    }

    /** Decode the next logical character(s) from the stream, or null at end-of-stream. */
    private fun decodeNext(): CharArray? {
        val lead = nextByte()
        if (lead == -1) return null
        if (lead == '\n'.code) linesSeen++
        if (lead < 0x80) return charArrayOf(lead.toChar())

        val seqLen =
            when (lead) {
                in 0xC2..0xDF -> 2
                in 0xE0..0xEF -> 3
                in 0xF0..0xF4 -> 4
                else -> 0 // continuation byte or invalid lead -> not a valid UTF-8 start
            }
        if (seqLen == 0) throw invalidByte(lead)

        val bytes = ByteArray(seqLen)
        bytes[0] = lead.toByte()
        var read = 1
        while (read < seqLen) {
            val next = nextByte()
            if (next == -1 || next !in 0x80..0xBF) {
                throw invalidByte(lead)
            }
            bytes[read] = next.toByte()
            read++
        }
        return String(bytes, StandardCharsets.UTF_8).toCharArray()
    }

    private fun nextByte(): Int {
        val b = stream.read()
        if (b != -1) byteOffset++
        return b
    }

    private fun invalidByte(b: Int): IOException =
        InvalidUtf8EncodingException(
            "Invalid UTF-8 byte 0x%02X at byte offset %d (around row %d). The input file is not valid UTF-8. ".format(
                b,
                byteOffset,
                linesSeen + 1,
            ) + "Re-save the file as UTF-8, or set the input file encoding to Windows-1252 in the package configuration.",
        )

    override fun close() = stream.close()
}

/** Raised when [StrictUtf8Reader] encounters a byte that is not valid UTF-8. */
class InvalidUtf8EncodingException(
    message: String,
) : IOException(message)
