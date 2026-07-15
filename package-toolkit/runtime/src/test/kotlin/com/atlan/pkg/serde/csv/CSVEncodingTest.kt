/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import de.siegmar.fastcsv.reader.CsvReader
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Verifies the two decoding modes for input CSVs:
 *  - [CSVDecoding.UTF8_STRICT] (the default): clean UTF-8 is byte-perfect, and any non-UTF-8 byte
 *    fails loudly (rather than being silently corrupted into U+FFFD as the JDK default decoder does).
 *  - [CSVDecoding.CP1252_FALLBACK] (explicit opt-in): decodes UTF-8-first with a per-byte Windows-1252
 *    rescue, for the Excel-on-Windows / mixed-encoding files seen in the wild.
 *
 * The canonical failure: Excel's "Save As -> CSV" on Windows writes Windows-1252 (cp1252), where the
 * ellipsis '…' (U+2026) is the single byte 0x85 -- an invalid UTF-8 lead byte. Under the strict
 * default that now surfaces a clear error; under the cp1252 opt-in it is recovered. (CSA-458)
 */
class CSVEncodingTest {
    private val ellipsis = '…' // …
    private val leftQuote = '‘' // '
    private val windows1252: Charset = Charset.forName("windows-1252")

    private fun tempCsv(bytes: ByteArray): File =
        File.createTempFile("aim-encoding", ".csv").apply {
            outputStream().use { it.write(bytes) }
        }

    private fun tempCsv(
        content: String,
        charset: Charset,
        bom: ByteArray = ByteArray(0),
    ): File = tempCsv(bom + content.toByteArray(charset))

    /** Read the last field of the single data row, using the code path production uses. */
    private fun readLastField(
        file: File,
        decoding: CSVDecoding = CSVDecoding.UTF8_STRICT,
    ): String {
        CsvReader.builder().ofCsvRecord(CSVEncoding.open(file.toPath(), decoding)).use { reader ->
            val rows = reader.stream().toList()
            val data = rows[1]
            return data.getField(data.fieldCount - 1)
        }
    }

    private fun row(description: String) = "typeName,qualifiedName,description\nTable,default/x/db/sch/t,\"$description\"\n"

    // --- strict UTF-8 (default) -------------------------------------------------------------------

    @Test
    fun utf8EllipsisIsPreservedUnderStrictDefault() {
        val file = tempCsv(row("ends with $ellipsis"), StandardCharsets.UTF_8)
        val value = readLastField(file)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun utf8BomIsStrippedAndContentPreservedUnderStrictDefault() {
        val file = tempCsv(row("ends with $ellipsis"), StandardCharsets.UTF_8, bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
        // header column must still resolve cleanly (BOM must not leak into the first field)
        assertEquals(listOf("typeName", "qualifiedName", "description"), CSVXformer.getHeader(file.absolutePath))
        val value = readLastField(file)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
    }

    /** A 4-byte (astral) UTF-8 code point must survive intact (surrogate pair handling). */
    @Test
    fun astralUtf8CodePointIsPreservedUnderStrictDefault() {
        val emoji = "😀" // U+1F600 grinning face
        val file = tempCsv(row("emoji $emoji here"), StandardCharsets.UTF_8)
        val value = readLastField(file)
        assertTrue(value.contains(emoji), "Expected the astral code point to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    /**
     * Under the strict default, a cp1252 byte (here the ellipsis 0x85) is NOT silently corrected --
     * it fails loudly with a message that points at the offending byte/row and how to resolve it.
     */
    @Test
    fun strictDefaultRejectsNonUtf8WithActionableError() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        val thrown =
            assertFailsWith<Exception> {
                readLastField(file, CSVDecoding.UTF8_STRICT)
            }
        val messages =
            generateSequence<Throwable>(thrown) { it.cause }
                .mapNotNull { it.message }
                .joinToString(" | ")
        assertTrue(messages.contains("not valid UTF-8"), "Expected an actionable UTF-8 error, got: [$messages]")
        assertTrue(messages.contains("Windows-1252"), "Error should hint at the Windows-1252 opt-in: [$messages]")
        assertTrue(messages.contains("row"), "Error should reference the offending row: [$messages]")
    }

    // --- cp1252 fallback (explicit opt-in) --------------------------------------------------------

    @Test
    fun windows1252EllipsisIsRecoveredWithCp1252Fallback() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        val value = readLastField(file, CSVDecoding.CP1252_FALLBACK)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun windows1252SmartQuotesAreRecoveredWithCp1252Fallback() {
        val file = tempCsv(row("a $leftQuote quoted $leftQuote value"), windows1252)
        val value = readLastField(file, CSVDecoding.CP1252_FALLBACK)
        assertTrue(value.contains(leftQuote), "Expected smart quote to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    /** Clean UTF-8 must still decode correctly when the cp1252 fallback is enabled. */
    @Test
    fun cleanUtf8StillCorrectWithCp1252Fallback() {
        val file = tempCsv(row("ends with $ellipsis"), StandardCharsets.UTF_8)
        val value = readLastField(file, CSVDecoding.CP1252_FALLBACK)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    /**
     * The real VS customer file mixes a genuine UTF-8 ellipsis (E2 80 A6) with a cp1252 ellipsis
     * (0x85) in the same field. A single whole-file charset choice mangles one or the other; the
     * hybrid fallback decoder must recover both.
     */
    @Test
    fun mixedUtf8AndCp1252InSameFieldAreBothRecovered() {
        val prefix = "typeName,qualifiedName,description\nTable,default/x/db/sch/t,\"utf8=".toByteArray(StandardCharsets.UTF_8)
        val mid = " cp1252=".toByteArray(StandardCharsets.US_ASCII)
        val suffix = "\"\n".toByteArray(StandardCharsets.US_ASCII)
        val bytes = prefix + byteArrayOf(0xE2.toByte(), 0x80.toByte(), 0xA6.toByte()) + mid + byteArrayOf(0x85.toByte()) + suffix
        val file = tempCsv(bytes)
        val value = readLastField(file, CSVDecoding.CP1252_FALLBACK)
        assertEquals(2, value.count { it == ellipsis }, "Both the UTF-8 and cp1252 ellipses should be recovered: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
        assertFalse(value.contains("â€¦"), "Genuine UTF-8 ellipsis was mangled by a cp1252 decode: [$value]")
    }

    // --- config mapping ---------------------------------------------------------------------------

    @Test
    fun fromConfigMapsExpectedValues() {
        assertEquals(CSVDecoding.UTF8_STRICT, CSVDecoding.fromConfig("utf8"))
        assertEquals(CSVDecoding.UTF8_STRICT, CSVDecoding.fromConfig(null))
        assertEquals(CSVDecoding.UTF8_STRICT, CSVDecoding.fromConfig(""))
        assertEquals(CSVDecoding.CP1252_FALLBACK, CSVDecoding.fromConfig("cp1252"))
        assertEquals(CSVDecoding.CP1252_FALLBACK, CSVDecoding.fromConfig("windows-1252"))
    }
}
