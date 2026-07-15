/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

/**
 * How to decode the bytes of an input CSV that is not prefixed with a UTF-16/UTF-32 byte-order mark.
 */
enum class CSVDecoding {
    /**
     * Decode as strict UTF-8 and fail loudly (see [StrictUtf8Reader]) on any byte that is not valid
     * UTF-8. This is the default: a "successful" import of a clean UTF-8 file is guaranteed byte-perfect,
     * and a non-UTF-8 file surfaces a clear error rather than being silently corrupted.
     */
    UTF8_STRICT,

    /**
     * Decode UTF-8-first with a per-byte Windows-1252 (cp1252) fallback (see
     * [Utf8WithCp1252FallbackReader]). An explicit opt-in for callers who knowingly have legacy
     * Excel-on-Windows / cp1252 (or mixed-encoding) files.
     */
    CP1252_FALLBACK,
    ;

    companion object {
        /**
         * Map a package-configuration string (as produced by the UI radio) to a decoding mode,
         * defaulting to [UTF8_STRICT] for any unrecognised or blank value.
         */
        fun fromConfig(value: String?): CSVDecoding =
            when (value?.trim()?.lowercase()) {
                "cp1252", "windows-1252", "windows1252" -> CP1252_FALLBACK
                else -> UTF8_STRICT
            }
    }
}

/**
 * Opens input CSV files for reading with encoding handling that avoids silently corrupting non-ASCII
 * content.
 *
 * Historically the readers passed a [Path] straight to FastCSV, which decodes as UTF-8 using the
 * JDK's default decoder behaviour -- that *silently* substitutes any byte which is not valid UTF-8
 * with the Unicode replacement character (U+FFFD). Files produced by common tools (most notably
 * Excel's "Save As -> CSV" on Windows, which writes Windows-1252) were therefore corrupted, e.g. the
 * ellipsis '…' (the single byte 0x85 in cp1252) became U+FFFD.
 *
 * Strategy:
 *  - If the file starts with a UTF-16 or UTF-32 BOM, decode with that (BOM-aware) charset.
 *  - Otherwise decode according to the requested [CSVDecoding]: strict UTF-8 (default, fail loud) or
 *    UTF-8-first with a per-byte Windows-1252 fallback (explicit opt-in). A leading UTF-8 BOM, if
 *    present, is skipped.
 *
 * Note on the UTF-8 BOM: this class is the single owner of stripping it from the byte stream (below).
 * [CSVXformer.trimWhitespace] independently trims a leading U+FEFF from already-decoded field values
 * as a belt-and-suspenders measure; do not remove one assuming the other covers it.
 */
object CSVEncoding {
    private val UTF_8_BOM = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    /**
     * Open the provided CSV file as a character stream, decoding it losslessly.
     *
     * @param path location of the CSV file to read
     * @param decoding how to decode non-BOM-prefixed bytes (defaults to strict UTF-8)
     * @return a [Reader] positioned at the first content character (after any BOM)
     */
    fun open(
        path: Path,
        decoding: CSVDecoding = CSVDecoding.UTF8_STRICT,
    ): Reader {
        val bom = readLeadingBytes(path, 4)
        detectBomCharset(bom)?.let { charset ->
            // UTF-16/UTF-32 charsets consume their own BOM; hand the raw stream to an InputStreamReader.
            return InputStreamReader(BufferedInputStream(Files.newInputStream(path)), charset)
        }
        val stream = BufferedInputStream(Files.newInputStream(path))
        if (startsWith(bom, UTF_8_BOM)) {
            // Skip the UTF-8 BOM bytes so they do not surface as a stray U+FEFF in the first field.
            stream.skipNBytes(UTF_8_BOM.size.toLong())
        }
        return when (decoding) {
            CSVDecoding.UTF8_STRICT -> StrictUtf8Reader(stream)
            CSVDecoding.CP1252_FALLBACK -> Utf8WithCp1252FallbackReader(stream)
        }
    }

    private fun readLeadingBytes(
        path: Path,
        count: Int,
    ): ByteArray {
        val buffer = ByteArray(count)
        val read =
            Files.newInputStream(path).use { input ->
                input.read(buffer)
            }
        return if (read <= 0) ByteArray(0) else buffer.copyOf(read)
    }

    /** Detect a UTF-16/UTF-32 BOM (UTF-8 BOM is handled separately, as it does not change the decoder). */
    private fun detectBomCharset(bom: ByteArray): Charset? {
        fun b(i: Int): Int = if (i < bom.size) bom[i].toInt() and 0xFF else -1
        return when {
            // UTF-32 BOMs must be checked before UTF-16 LE, since they share the 0xFF 0xFE prefix.
            b(0) == 0xFF && b(1) == 0xFE && b(2) == 0x00 && b(3) == 0x00 -> Charset.forName("UTF-32")

            b(0) == 0x00 && b(1) == 0x00 && b(2) == 0xFE && b(3) == 0xFF -> Charset.forName("UTF-32")

            b(0) == 0xFF && b(1) == 0xFE -> Charset.forName("UTF-16")

            b(0) == 0xFE && b(1) == 0xFF -> Charset.forName("UTF-16")

            else -> null
        }
    }

    private fun startsWith(
        data: ByteArray,
        prefix: ByteArray,
    ): Boolean {
        if (data.size < prefix.size) return false
        for (i in prefix.indices) {
            if (data[i] != prefix[i]) return false
        }
        return true
    }
}
