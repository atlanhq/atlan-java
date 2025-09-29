/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of a glossary and its inter-related contents.
 */
class ManyTermToTermTest : PackageTest("mt2t") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossary1 = "jpkg_mt2t_g1_UYYhA" // TODO: makeUnique("g1")

    private val testFile = "term-to-term-many.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{GLOSSARY1}}", glossary1)
                output.appendText("$revised\n")
            }
        }
    }

    private val glossaryAttrs: List<AtlanField> =
        listOf(
            Glossary.NAME,
            Glossary.USER_DESCRIPTION,
        )

    private val termAttrs: List<AtlanField> =
        listOf(
            GlossaryTerm.NAME,
            GlossaryTerm.ANCHOR,
            GlossaryTerm.USER_DESCRIPTION,
            GlossaryTerm.README,
            GlossaryTerm.SEE_ALSO,
        )

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
        removeGlossary(glossary1)
    }

    @Test
    fun glossary1Created() {
        val g1 = Glossary.findByName(client, glossary1, glossaryAttrs)
        assertNotNull(g1)
        assertEquals(glossary1, g1.name)
        assertEquals("Test glossary for asset import package term-to-term relationships.", g1.userDescription)
    }

    @Test
    fun termsCreatedG1() {
        val g1 = Glossary.findByName(client, glossary1)!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(g1.qualifiedName))
                .includesOnResults(termAttrs)
                .includeOnRelations(Glossary.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .includeOnRelations(Link.LINK)
                .toRequest()
        val response = retrySearchUntil(request, 70)
        val g1terms = response.assets
        assertEquals(70, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            when (term.name) {
                "Term1" -> validateTerm1(term)
                "Term2" -> validateTerm2(term)
            }
        }
    }

    private fun validateTerm1(term: GlossaryTerm) {
        assertEquals("Test term 1 for asset import package term-to-term relationships.", term.userDescription)
        assertEquals(AtlanStatus.ACTIVE, term.status)
        assertNotNull(term.readme)
        assertEquals("<h1>This is term1!</h1>", term.readme.description)
        assertEquals(63, term.seeAlso.size)
        // assertEquals(setOf("Term2"), term.seeAlso.map(IGlossaryTerm::getName).toSet())
    }

    private fun validateTerm2(term: GlossaryTerm) {
        assertEquals("Test term 2 for asset import package term-to-term relationships.", term.userDescription)
        assertEquals(AtlanStatus.ACTIVE, term.status)
        assertNotNull(term.readme)
        assertEquals("<h2>This is term2.</h2>", term.readme.description)
        assertEquals(7, term.seeAlso.size)
        // assertEquals(setOf("Term1"), term.seeAlso.map(IGlossaryTerm::getName).toSet())
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
