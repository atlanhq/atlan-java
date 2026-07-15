/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Regression guard for CSA-458: a clean UTF-8 input file imported on the *default* (strict UTF-8)
 * encoding must round-trip byte-perfect exactly as before the encoding change -- i.e. the common,
 * unchanged path continues to work and nothing functionally regressed for well-formed input.
 */
class Utf8DefaultEncodingImportTest : PackageTest("utf8def") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossaryName = makeUnique("g")
    private val testFile = "utf8_input.csv"

    // Same typographic characters as the cp1252 test, but here written as genuine UTF-8.
    private val description = "Ends with an ellipsis … and ‘smart quotes’ — plus café and emoji 😀"

    private val header =
        "qualifiedName,typeName,name,anchor,parentCategory,categories,displayName,description,userDescription," +
            "ownerUsers,ownerGroups,certificateStatus,certificateStatusMessage,announcementType,announcementTitle," +
            "announcementMessage,atlanTags,links,readme,starredDetails,seeAlso,preferredTerms,synonyms,antonyms," +
            "translatedTerms,validValuesFor,classifies"

    private val glossaryAttrs: List<AtlanField> = listOf(Glossary.NAME, Glossary.USER_DESCRIPTION)

    private fun prepFile() {
        val row = ",AtlasGlossary,$glossaryName,,,,,,\"$description\",,,,,,,,,,,,,,,,,,"
        val content = "$header\n$row\n"
        val output = Paths.get(testDirectory, testFile).toFile()
        output.writeBytes(content.toByteArray(StandardCharsets.UTF_8))
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, testFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = true,
                // No inputEncoding override -- exercise the strict UTF-8 default (the common path).
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeGlossary(glossaryName)
    }

    @Test(groups = ["aim.utf8def.create"])
    fun cleanUtf8RoundTripsOnStrictDefault() {
        val glossary = Glossary.findByName(client, glossaryName, glossaryAttrs)
        assertNotNull(glossary)
        assertEquals(glossaryName, glossary.name)
        assertNotNull(glossary.userDescription)
        assertFalse(glossary.userDescription.contains('�'), "Description was corrupted with U+FFFD: [${glossary.userDescription}]")
        assertTrue(glossary.userDescription.contains('…'), "Ellipsis was lost: [${glossary.userDescription}]")
        assertEquals(description, glossary.userDescription)
    }
}
