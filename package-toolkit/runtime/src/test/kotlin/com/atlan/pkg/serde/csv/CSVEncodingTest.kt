/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import de.siegmar.fastcsv.reader.CsvReader
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Verifies that non-UTF-8 (and mixed-encoding) input CSVs are decoded losslessly, rather than
 * silently corrupting non-ASCII characters into the Unicode replacement character U+FFFD.
 *
 * The canonical failure: Excel's "Save As -> CSV" on Windows writes Windows-1252 (cp1252), where the
 * ellipsis '…' (U+2026) is the single byte 0x85 -- an invalid UTF-8 lead byte that the default JDK
 * decoder replaces with U+FFFD. Real customer files are frequently *mixed* (some cp1252 bytes, some
 * genuine UTF-8), so the decoder must handle both within a single file. (CSA-458)
 */
class CSVEncodingTest {
    private val ellipsis = '…' // …
    private val leftQuote = '‘' // '
    private val windows1252: Charset = Charset.forName("windows-1252")

    private fun tempCsv(
        bytes: ByteArray,
    ): File =
        File.createTempFile("aim-encoding", ".csv").apply {
            outputStream().use { it.write(bytes) }
        }

    private fun tempCsv(
        content: String,
        charset: Charset,
        bom: ByteArray = ByteArray(0),
    ): File = tempCsv(bom + content.toByteArray(charset))

    /** Read the last field of the single data row, using the code path production uses. */
    private fun readLastField(file: File): String {
        CsvReader.builder().ofCsvRecord(CSVEncoding.open(file.toPath())).use { reader ->
            val rows = reader.stream().toList()
            val data = rows[1]
            return data.getField(data.fieldCount - 1)
        }
    }

    private fun row(description: String) = "typeName,qualifiedName,description\nTable,default/x/db/sch/t,\"$description\"\n"

    @Test
    fun windows1252EllipsisIsPreservedNotCorrupted() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        val value = readLastField(file)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun windows1252SmartQuotesArePreserved() {
        val file = tempCsv(row("a $leftQuote quoted $leftQuote value"), windows1252)
        val value = readLastField(file)
        assertTrue(value.contains(leftQuote), "Expected smart quote to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun utf8EllipsisIsPreserved() {
        val file = tempCsv(row("ends with $ellipsis"), StandardCharsets.UTF_8)
        val value = readLastField(file)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun utf8BomIsStrippedAndContentPreserved() {
        val file = tempCsv(row("ends with $ellipsis"), StandardCharsets.UTF_8, bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
        // header column must still resolve cleanly (BOM must not leak into the first field)
        assertEquals(listOf("typeName", "qualifiedName", "description"), CSVXformer.getHeader(file.absolutePath))
        val value = readLastField(file)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
    }

    /**
     * The real VS customer file mixes a genuine UTF-8 ellipsis (E2 80 A6) with a cp1252 ellipsis
     * (0x85) in the same field. A single whole-file charset choice mangles one or the other; the
     * hybrid decoder must recover both.
     */
    @Test
    fun mixedUtf8AndCp1252InSameFieldAreBothRecovered() {
        val prefix = "typeName,qualifiedName,description\nTable,default/x/db/sch/t,\"utf8=".toByteArray(StandardCharsets.UTF_8)
        val mid = " cp1252=".toByteArray(StandardCharsets.US_ASCII)
        val suffix = "\"\n".toByteArray(StandardCharsets.US_ASCII)
        val bytes = prefix + byteArrayOf(0xE2.toByte(), 0x80.toByte(), 0xA6.toByte()) + mid + byteArrayOf(0x85.toByte()) + suffix
        val file = tempCsv(bytes)
        val value = readLastField(file)
        assertEquals(2, value.count { it == ellipsis }, "Both the UTF-8 and cp1252 ellipses should be recovered: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
        assertFalse(value.contains("â€¦"), "Genuine UTF-8 ellipsis was mangled by a cp1252 decode: [$value]")
    }

    /** A 4-byte (astral) UTF-8 code point must survive intact (surrogate pair handling). */
    @Test
    fun astralUtf8CodePointIsPreserved() {
        val emoji = "😀" // U+1F600 grinning face
        val file = tempCsv(row("emoji $emoji here"), StandardCharsets.UTF_8)
        val value = readLastField(file)
        assertTrue(value.contains(emoji), "Expected the astral code point to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }
}
