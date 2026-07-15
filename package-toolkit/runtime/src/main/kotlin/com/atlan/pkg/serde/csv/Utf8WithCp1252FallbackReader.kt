/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.Reader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * A streaming [Reader] that decodes bytes as UTF-8 wherever they form a valid UTF-8 sequence, and
 * falls back to Windows-1252 (cp1252) for any individual byte that does not.
 *
 * Real-world CSVs (particularly those edited/exported through Excel on Windows) are frequently
 * *mixed* encoding: mostly cp1252 with the occasional genuine UTF-8 sequence, or vice versa. A
 * single whole-file charset choice therefore always mangles part of the content. Decoding UTF-8-first
 * with a per-byte cp1252 rescue preserves both:
 *  - genuine multi-byte UTF-8 (e.g. '…' as E2 80 A6) is decoded correctly, and
 *  - stray cp1252 bytes (e.g. '…' as the single byte 0x85, or smart quotes 0x91-0x94) are recovered
 *    instead of being silently turned into the U+FFFD replacement character.
 *
 * Bytes that are already the UTF-8 encoding of U+FFFD (EF BF BD) in the source file were corrupted
 * upstream, before this reader ever runs; they decode as valid UTF-8 and are preserved as-is (there
 * is no information left to recover). (CSA-458)
 */
class Utf8WithCp1252FallbackReader(
    input: InputStream,
) : Reader() {
    // Up to 3 look-ahead bytes may need to be pushed back when a multi-byte UTF-8 sequence turns
    // out to be invalid partway through.
    private val stream = PushbackInputStream(BufferedInputStream(input), 3)

    // Holds decoded characters not yet handed back to the caller (e.g. the low surrogate of an
    // astral code point, or overflow when the caller's buffer is full).
    private val pending = ArrayDeque<Char>()

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
        val lead = stream.read()
        if (lead == -1) return null
        if (lead < 0x80) return charArrayOf(lead.toChar())

        val seqLen =
            when (lead) {
                in 0xC2..0xDF -> 2
                in 0xE0..0xEF -> 3
                in 0xF0..0xF4 -> 4
                else -> 0 // continuation byte or invalid lead -> not a valid UTF-8 start
            }
        if (seqLen == 0) return rescueCp1252(lead)

        val bytes = ByteArray(seqLen)
        bytes[0] = lead.toByte()
        var read = 1
        while (read < seqLen) {
            val next = stream.read()
            if (next == -1 || next !in 0x80..0xBF) {
                // Not a valid continuation: push back everything after the lead byte (in order),
                // then rescue the lead byte alone as cp1252.
                if (next != -1) stream.unread(next)
                for (j in read - 1 downTo 1) {
                    stream.unread(bytes[j].toInt() and 0xFF)
                }
                return rescueCp1252(lead)
            }
            bytes[read] = next.toByte()
            read++
        }
        return String(bytes, StandardCharsets.UTF_8).toCharArray()
    }

    /** Decode a single byte using cp1252 (undefined cp1252 bytes fall back to U+FFFD). */
    private fun rescueCp1252(b: Int): CharArray = String(byteArrayOf(b.toByte()), WINDOWS_1252).toCharArray()

    override fun close() = stream.close()

    companion object {
        private val WINDOWS_1252: Charset = Charset.forName("windows-1252")
    }
}
