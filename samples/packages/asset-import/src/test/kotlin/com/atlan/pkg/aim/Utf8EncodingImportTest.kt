/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.charset.Charset
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * End-to-end proof for CSA-458: importing a CSV that is encoded as Windows-1252 (as Excel's
 * "Save As -> CSV" produces on Windows) must round-trip typographic Unicode characters intact,
 * rather than storing them as the replacement character U+FFFD.
 *
 * This runs the full path against a live tenant: cp1252 CSV -> decode -> asset write -> Atlas
 * storage -> read-back -> assertion.
 */
class Utf8EncodingImportTest : PackageTest("utf8") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossaryName = makeUnique("g")
    private val testFile = "cp1252_input.csv"

    // '…' U+2026, '‘'/'’' U+2018/U+2019, '—' U+2014, 'é' U+00E9 -- all representable in cp1252.
    private val description = "Ends with an ellipsis … and ‘smart quotes’ — plus café"

    private val header =
        "qualifiedName,typeName,name,anchor,parentCategory,categories,displayName,description,userDescription," +
            "ownerUsers,ownerGroups,certificateStatus,certificateStatusMessage,announcementType,announcementTitle," +
            "announcementMessage,atlanTags,links,readme,starredDetails,seeAlso,preferredTerms,synonyms,antonyms," +
            "translatedTerms,validValuesFor,classifies"

    private val glossaryAttrs: List<AtlanField> = listOf(Glossary.NAME, Glossary.USER_DESCRIPTION)

    private fun prepFile() {
        val row = ",AtlasGlossary,$glossaryName,,,,,,\"$description\",,,,,,,,,,,,,,,,,,"
        val content = "$header\n$row\n"
        // Write the CSV as Windows-1252 bytes, exactly as an Excel-on-Windows "Save As CSV" would.
        val output = Paths.get(testDirectory, testFile).toFile()
        output.writeBytes(content.toByteArray(Charset.forName("windows-1252")))
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeGlossary(glossaryName)
    }

    @Test(groups = ["aim.utf8.create"])
    fun descriptionRoundTripsWithoutCorruption() {
        val glossary = Glossary.findByName(client, glossaryName, glossaryAttrs)
        assertNotNull(glossary)
        assertEquals(glossaryName, glossary.name)
        assertNotNull(glossary.userDescription)
        assertFalse(glossary.userDescription.contains('�'), "Description was corrupted with U+FFFD: [${glossary.userDescription}]")
        assertTrue(glossary.userDescription.contains('…'), "Ellipsis was lost: [${glossary.userDescription}]")
        assertEquals(description, glossary.userDescription)
    }
}
