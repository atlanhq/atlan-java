/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.BufferedInputStream
import java.io.Reader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

/**
 * The character encoding to use when decoding an input CSV.
 *
 * This is the JDK 8 "Basic Encoding Set" (the charsets contained in lib/rt.jar), which is guaranteed
 * to be available on every JRE -- see
 * https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html. The Extended
 * Encoding Set (lib/charsets.jar) is intentionally not offered.
 *
 * Whichever charset is chosen, the input is decoded *strictly*: any byte sequence that is not valid
 * for that charset fails loudly (see [StrictDecodingReader]) rather than being silently substituted
 * with the Unicode replacement character (U+FFFD). [UTF_8] is the default and the recommended
 * encoding; the other entries let a user who knowingly has a legacy-encoded file (for example an
 * Excel-on-Windows [WINDOWS_1252] export) declare that encoding explicitly. (CSA-458)
 *
 * Each entry's [charsetName] is the exact canonical java.nio charset name, so it round-trips through
 * both [Charset.forName] and the package configuration value.
 */
enum class CSVDecoding(
    val charsetName: String,
) {
    CESU_8("CESU-8"),
    GB18030("GB18030"),
    IBM00858("IBM00858"),
    IBM437("IBM437"),
    IBM775("IBM775"),
    IBM850("IBM850"),
    IBM852("IBM852"),
    IBM855("IBM855"),
    IBM857("IBM857"),
    IBM862("IBM862"),
    IBM866("IBM866"),
    ISO_8859_1("ISO-8859-1"),
    ISO_8859_2("ISO-8859-2"),
    ISO_8859_4("ISO-8859-4"),
    ISO_8859_5("ISO-8859-5"),
    ISO_8859_7("ISO-8859-7"),
    ISO_8859_9("ISO-8859-9"),
    ISO_8859_13("ISO-8859-13"),
    ISO_8859_15("ISO-8859-15"),
    KOI8_R("KOI8-R"),
    KOI8_U("KOI8-U"),
    US_ASCII("US-ASCII"),
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    UTF_16BE("UTF-16BE"),
    UTF_16LE("UTF-16LE"),
    UTF_32("UTF-32"),
    UTF_32BE("UTF-32BE"),
    UTF_32LE("UTF-32LE"),
    X_UTF_32BE_BOM("x-UTF-32BE-BOM"),
    X_UTF_32LE_BOM("x-UTF-32LE-BOM"),
    WINDOWS_1250("windows-1250"),
    WINDOWS_1251("windows-1251"),
    WINDOWS_1252("windows-1252"),
    WINDOWS_1253("windows-1253"),
    WINDOWS_1254("windows-1254"),
    WINDOWS_1257("windows-1257"),
    X_IBM737("x-IBM737"),
    X_IBM874("x-IBM874"),
    X_UTF_16LE_BOM("x-UTF-16LE-BOM"),
    ;

    /** The resolved JVM [Charset] for this encoding. */
    val charset: Charset get() = Charset.forName(charsetName)

    companion object {
        /**
         * Map a package-configuration string (the canonical charset name produced by the UI dropdown)
         * to an encoding, defaulting to [UTF_8] for any unrecognised or blank value. Matching is
         * case-insensitive and also accepts any registered alias of the charset (e.g. "cp1252" ->
         * windows-1252, "latin1" -> ISO-8859-1).
         */
        fun fromConfig(value: String?): CSVDecoding {
            val v = value?.trim().orEmpty()
            if (v.isEmpty()) return UTF_8
            entries.firstOrNull { it.charsetName.equals(v, ignoreCase = true) }?.let { return it }
            val resolved = runCatching { Charset.forName(v) }.getOrNull() ?: return UTF_8
            return entries.firstOrNull { it.charset == resolved } ?: UTF_8
        }
    }
}

/**
 * Opens input CSV files for reading with strict, fail-loud encoding handling.
 *
 * Historically the readers passed a [Path] straight to FastCSV, which decodes as UTF-8 using the
 * JDK's default decoder behaviour -- that *silently* substitutes any byte which is not valid UTF-8
 * with the Unicode replacement character (U+FFFD). Files produced by common tools (most notably
 * Excel's "Save As -> CSV" on Windows, which writes Windows-1252) were therefore corrupted, e.g. the
 * ellipsis '…' (the single byte 0x85 in cp1252) became U+FFFD.
 *
 * The input is now decoded in the [CSVDecoding] the caller specifies (UTF-8 by default), and any byte
 * that is not valid for that charset raises a clear error rather than being silently mangled. A
 * "successful" import is therefore guaranteed byte-perfect for the declared encoding.
 *
 * Note on the UTF-8 BOM: this class strips a leading UTF-8 BOM from the byte stream (below) when
 * decoding as UTF-8. [CSVXformer.trimWhitespace] independently trims a leading U+FEFF from
 * already-decoded field values as a belt-and-suspenders measure; do not remove one assuming the other
 * covers it. (UTF-16/UTF-32 byte-order marks are consumed by their respective charset decoders.)
 */
object CSVEncoding {
    private val UTF_8_BOM = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    /**
     * Open the provided CSV file as a character stream, decoding it strictly in the requested encoding.
     *
     * @param path location of the CSV file to read
     * @param decoding the charset to decode with (defaults to strict UTF-8)
     * @return a [Reader] positioned at the first content character (after any UTF-8 BOM)
     */
    fun open(
        path: Path,
        decoding: CSVDecoding = CSVDecoding.UTF_8,
    ): Reader {
        val stream = BufferedInputStream(Files.newInputStream(path))
        if (decoding == CSVDecoding.UTF_8 && startsWithUtf8Bom(path)) {
            // Skip the UTF-8 BOM bytes so they do not surface as a stray U+FEFF in the first field.
            stream.skipNBytes(UTF_8_BOM.size.toLong())
        }
        return StrictDecodingReader(stream, decoding.charset)
    }

    private fun startsWithUtf8Bom(path: Path): Boolean {
        val buffer = ByteArray(UTF_8_BOM.size)
        val read = Files.newInputStream(path).use { it.read(buffer) }
        return read == UTF_8_BOM.size && buffer.contentEquals(UTF_8_BOM)
    }
}
