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
 * Verifies strict, per-charset decoding of input CSVs (the JDK basic encoding set):
 *  - the input is decoded strictly in the chosen [CSVDecoding]; clean input is byte-perfect, and
 *    bytes that are not valid for that charset fail loudly rather than being silently corrupted into
 *    the Unicode replacement character U+FFFD (the JDK default decoder behaviour);
 *  - [CSVDecoding.UTF_8] is the default.
 *
 * The canonical failure: Excel's "Save As -> CSV" on Windows writes Windows-1252 (cp1252), where the
 * ellipsis '…' (U+2026) is the single byte 0x85 -- an invalid UTF-8 lead byte. Under the strict UTF-8
 * default that now surfaces a clear error; selecting [CSVDecoding.WINDOWS_1252] decodes it correctly. (CSA-458)
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
        decoding: CSVDecoding = CSVDecoding.UTF_8,
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
     * Under the strict UTF-8 default, a cp1252 byte (here the ellipsis 0x85) is NOT silently corrected
     * -- it fails loudly with a message that points at the offending row and charset and how to resolve it.
     */
    @Test
    fun strictDefaultRejectsNonUtf8WithActionableError() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        val thrown =
            assertFailsWith<Exception> {
                readLastField(file, CSVDecoding.UTF_8)
            }
        val messages =
            generateSequence<Throwable>(thrown) { it.cause }
                .mapNotNull { it.message }
                .joinToString(" | ")
        assertTrue(messages.contains("not valid UTF-8"), "Expected an actionable UTF-8 error, got: [$messages]")
        assertTrue(messages.contains("input file encoding"), "Error should hint at the encoding option: [$messages]")
        assertTrue(messages.contains("row"), "Error should reference the offending row: [$messages]")
    }

    /** US-ASCII selected but a non-ASCII byte present -> fail loud (unmappable/malformed). */
    @Test
    fun usAsciiRejectsHighByte() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        assertFailsWith<Exception> { readLastField(file, CSVDecoding.US_ASCII) }
    }

    // --- explicit legacy charsets -----------------------------------------------------------------

    @Test
    fun windows1252EllipsisIsRecoveredWhenSelected() {
        val file = tempCsv(row("ends with $ellipsis"), windows1252)
        val value = readLastField(file, CSVDecoding.WINDOWS_1252)
        assertTrue(value.contains(ellipsis), "Expected the ellipsis to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    @Test
    fun windows1252SmartQuotesAreRecoveredWhenSelected() {
        val file = tempCsv(row("a $leftQuote quoted $leftQuote value"), windows1252)
        val value = readLastField(file, CSVDecoding.WINDOWS_1252)
        assertTrue(value.contains(leftQuote), "Expected smart quote to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    /** ISO-8859-1 (Latin-1): the accented 'é' (byte 0xE9) round-trips when that charset is selected. */
    @Test
    fun latin1AccentIsRecoveredWhenSelected() {
        val file = tempCsv(row("café"), Charset.forName("ISO-8859-1"))
        val value = readLastField(file, CSVDecoding.ISO_8859_1)
        assertTrue(value.contains("café"), "Expected the accented e to be preserved, got: [$value]")
        assertFalse(value.contains('�'), "Value was corrupted with U+FFFD: [$value]")
    }

    // --- config mapping & charset validity --------------------------------------------------------

    @Test
    fun fromConfigMapsCanonicalNamesAndAliases() {
        assertEquals(CSVDecoding.UTF_8, CSVDecoding.fromConfig("UTF-8"))
        assertEquals(CSVDecoding.UTF_8, CSVDecoding.fromConfig(null))
        assertEquals(CSVDecoding.UTF_8, CSVDecoding.fromConfig(""))
        assertEquals(CSVDecoding.UTF_8, CSVDecoding.fromConfig("totally-unknown-charset"))
        assertEquals(CSVDecoding.WINDOWS_1252, CSVDecoding.fromConfig("windows-1252"))
        // alias resolution via Charset.forName
        assertEquals(CSVDecoding.WINDOWS_1252, CSVDecoding.fromConfig("cp1252"))
        assertEquals(CSVDecoding.ISO_8859_1, CSVDecoding.fromConfig("latin1"))
        assertEquals(CSVDecoding.US_ASCII, CSVDecoding.fromConfig("ascii"))
    }

    /**
     * Every declared encoding must resolve to an installed JVM charset (guards typos in canonical
     * names), and its config value must round-trip back to the same entry via [CSVDecoding.fromConfig].
     */
    @Test
    fun everyDeclaredEncodingResolvesAndRoundTrips() {
        CSVDecoding.entries.forEach { d ->
            val cs = d.charset // must not throw for an unknown/misspelled charset name
            assertTrue(cs.canEncode() || cs.name().isNotEmpty(), "Charset did not resolve for $d")
            assertEquals(d, CSVDecoding.fromConfig(d.charsetName), "fromConfig round-trip failed for ${d.charsetName}")
        }
    }

    /**
     * Behavioural coverage for the whole basic encoding set: for every encoding, take the subset of a
     * rich sample that the charset can represent, write those bytes in that charset, then decode them
     * back through the production [CSVEncoding.open] path and assert the value is byte-perfect. This
     * exercises the actual decode of each charset (single-byte legacy, multi-byte, and the
     * BOM-consuming UTF-16/UTF-32 families), not merely that the charset resolves.
     */
    @Test
    fun everyEncodingRoundTripsRepresentableCharacters() {
        // A spread of Latin, typographic, Cyrillic, Greek and currency characters (BMP only; no CSV
        // metacharacters). Each charset is tested against just the characters it can represent.
        val candidate = "ASCII az09 café über niño Ω Δ б Я … ‘ ’ “ ” — ¥ € £"
        var covered = 0
        CSVDecoding.entries.forEach { d ->
            val cs = d.charset
            if (!cs.canEncode()) return@forEach // a decode-only charset cannot be written from a String
            val encoder = cs.newEncoder()
            val representable = candidate.filter { encoder.canEncode(it) }
            assertTrue(
                representable.contains("ASCII az09"),
                "Sanity: ASCII must always be representable in ${d.charsetName}, got: [$representable]",
            )
            val file = tempCsv(row(representable), cs)
            val value = readLastField(file, d)
            assertEquals(representable, value, "Round-trip failed for ${d.charsetName}")
            assertFalse(value.contains('�'), "U+FFFD introduced by ${d.charsetName}: [$value]")
            covered++
        }
        // Guard against the sample or filter silently degrading coverage to just a handful of charsets.
        assertTrue(covered >= 35, "Expected to round-trip the vast majority of the 40 encodings, only covered $covered")
    }

    /** UTF-16 is multi-byte and consumes its own byte-order mark; content must survive a BOM'd file. */
    @Test
    fun utf16WithBomRoundTrips() {
        val text = "utf16 café … ‘q’ — Ω"
        val file = tempCsv(row(text), Charset.forName("UTF-16")) // writes a BOM
        // header must resolve cleanly (BOM consumed by the UTF-16 decoder, not leaked into field 1)
        assertEquals(listOf("typeName", "qualifiedName", "description"), CSVXformer.getHeader(file.absolutePath, ',', CSVDecoding.UTF_16))
        val value = readLastField(file, CSVDecoding.UTF_16)
        assertEquals(text, value, "UTF-16 (with BOM) did not round-trip: [$value]")
        assertFalse(value.contains('�'), "U+FFFD in UTF-16 round-trip: [$value]")
    }
}
