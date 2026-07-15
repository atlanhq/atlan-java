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
 *  - Otherwise decode UTF-8-first with a per-byte Windows-1252 fallback (see
 *    [Utf8WithCp1252FallbackReader]), which correctly handles clean UTF-8, clean cp1252, and the
 *    mixed-encoding files seen in the wild. A leading UTF-8 BOM, if present, is skipped.
 */
object CSVEncoding {
    private val UTF_8_BOM = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    /**
     * Open the provided CSV file as a character stream, decoding it losslessly.
     *
     * @param path location of the CSV file to read
     * @return a [Reader] positioned at the first content character (after any BOM)
     */
    fun open(path: Path): Reader {
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
        return Utf8WithCp1252FallbackReader(stream)
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
