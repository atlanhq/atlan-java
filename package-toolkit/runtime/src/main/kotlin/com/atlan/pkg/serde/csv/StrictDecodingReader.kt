/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

/**
 * A streaming [Reader] that decodes bytes in a given [Charset] and *fails loudly* on the first byte
 * sequence that is not valid for that charset, rather than silently substituting the Unicode
 * replacement character (U+FFFD) the way the JDK's default decoder behaviour does.
 *
 * A "successful" read of a file that matches its declared encoding is therefore guaranteed
 * byte-perfect, and a file that does not match surfaces a clear, actionable error naming the charset
 * and the approximate row -- instead of quietly corrupting the content. (CSA-458)
 *
 * The row is the number of newlines decoded before the failure, plus one -- the 1-based physical line
 * the bad bytes fall on. It is approximate in that it counts physical lines rather than CSV records
 * (a quoted field may span multiple lines), which is close enough to point a user at the problem.
 */
class StrictDecodingReader(
    input: InputStream,
    private val charset: Charset,
) : Reader() {
    private val delegate: InputStreamReader =
        InputStreamReader(
            BufferedInputStream(input),
            charset
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT),
        )

    // Physical lines decoded so far, used purely to make the error message actionable.
    private var linesSeen = 0L

    override fun read(
        cbuf: CharArray,
        off: Int,
        len: Int,
    ): Int {
        val count =
            try {
                delegate.read(cbuf, off, len)
            } catch (e: CharacterCodingException) {
                throw invalidBytes(e)
            }
        for (i in off until off + count) {
            if (cbuf[i] == '\n') linesSeen++
        }
        return count
    }

    private fun invalidBytes(cause: CharacterCodingException): IOException =
        InvalidEncodingException(
            "The input file is not valid ${charset.name()} (around row ${linesSeen + 1}). " +
                "Re-save the file as UTF-8, or choose the correct input file encoding in the package configuration.",
            cause,
        )

    override fun close() = delegate.close()
}

/** Raised when [StrictDecodingReader] encounters bytes that are not valid for the chosen charset. */
class InvalidEncodingException(
    message: String,
    cause: Throwable? = null,
) : IOException(message, cause)
