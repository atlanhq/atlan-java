/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.model.relations.UserDefRelationship
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test management of user-defined relationships.
 */
class TermsWithUserDefinedRelationshipsTest : PackageTest("twudr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val g1 = makeUnique("g1")
    private val testFile = "gtc_with_udrs.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare copies of the files with unique names for objects
        val gtcIn = Paths.get("src", "test", "resources", testFile).toFile()
        val gtcOut = Paths.get(testDirectory, testFile).toFile()
        gtcIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line.replace("{GLOSSARY}", g1)
                gtcOut.appendText("$revised,\n")
            }
        }
    }

    override fun setup() {
        prepFiles()
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
        removeGlossary(g1)
    }

    private val gtcAttrs: List<AtlanField> =
        listOf(
            GlossaryTerm.NAME,
            GlossaryTerm.USER_DEF_RELATIONSHIP_TOS,
            GlossaryTerm.USER_DEF_RELATIONSHIP_FROMS,
        )

    @Test
    fun glossaryCreated() {
        val glossary = Glossary.findByName(client, g1)
        assertNotNull(glossary)
    }

    @Test
    fun term1Created() {
        val term = GlossaryTerm.findByName(client, "Term1", g1, gtcAttrs)!!
        val related = term.userDefRelationshipTos
        assertNotNull(related)
        assertEquals(1, related.size)
        val relnAttrs = related.first().relationshipAttributes
        assertTrue(relnAttrs is UserDefRelationship)
        val udr = relnAttrs as UserDefRelationship
        assertEquals("links to", udr.toTypeLabel)
        assertEquals("linked from", udr.fromTypeLabel)
    }

    @Test
    fun term2Created() {
        val term = GlossaryTerm.findByName(client, "Term2", g1, gtcAttrs)!!
        val related = term.userDefRelationshipFroms
        assertNotNull(related)
        assertEquals(1, related.size)
        val relnAttrs = related.first().relationshipAttributes
        assertTrue(relnAttrs is UserDefRelationship)
        val udr = relnAttrs as UserDefRelationship
        assertEquals("links to", udr.toTypeLabel)
        assertEquals("linked from", udr.fromTypeLabel)
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
