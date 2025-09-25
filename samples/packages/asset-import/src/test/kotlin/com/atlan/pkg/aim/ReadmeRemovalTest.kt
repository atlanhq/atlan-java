/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.IGlossaryCategory
import com.atlan.model.assets.IGlossaryTerm
import com.atlan.model.assets.ILink
import com.atlan.model.assets.Link
import com.atlan.model.assets.Readme
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ThreadSafeWriter
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import org.testng.Assert.assertNull
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.useLines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test removal of README after initial import.
 */
class ReadmeRemovalTest : PackageTest("rr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossary1 = makeUnique("g1")

    private val testFile = "term-to-term.csv"
    private val revisedFile = "readmes-removed.csv"

    private val files =
        listOf(
            testFile,
            revisedFile,
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
                        .replace("Term2@@@$glossary1", "")
                        .replace("Term1@@@$glossary1", "")
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("<h1>This is term1!</h1>", "")
                        .replace("<h2>This is term2.</h2>", "")
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

    @Test(groups = ["aim.rr.create"])
    fun glossaryCreated() {
        val g1 = Glossary.findByName(client, glossary1, glossaryAttrs)
        assertNotNull(g1)
        assertEquals(glossary1, g1.name)
        assertEquals("Test glossary for asset import package term-to-term relationships.", g1.userDescription)
    }

    @Test(groups = ["aim.rr.create"])
    fun termsWithReadmes() {
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
        val response = retrySearchUntil(request, 2)
        val g1terms = response.assets
        assertEquals(2, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            when (term.name) {
                "Term1" -> validateTerm1(term)
                "Term2" -> validateTerm2(term)
            }
        }
    }

    @Test(groups = ["aim.rr.runUpdate"], dependsOnGroups = ["aim.rr.create"])
    fun upsertIG() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = Paths.get(testDirectory, revisedFile).toString(),
                glossariesUpsertSemantic = "upsert",
                glossariesFailOnErrors = true,
                glossariesConfig = "advanced",
                glossariesAttrToOverwrite = listOf(Asset.README.atlanFieldName),
            ),
            Importer::main,
        )
        // Allow Elastic index to become consistent
        Thread.sleep(10000)
    }

    @Test(groups = ["aim.rr.update"], dependsOnGroups = ["aim.rr.runUpdate"])
    fun termsWithoutReadmes() {
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
        val response = retrySearchUntil(request, 2)
        val g1terms = response.assets
        assertEquals(2, g1terms.size)
        g1terms.forEach { term ->
            term as GlossaryTerm
            assertEquals(glossary1, term.anchor?.name)
            when (term.name) {
                "Term1" -> validateTerm1(term, true)
                "Term2" -> validateTerm2(term, true)
            }
        }
    }

    private fun validateTerm1(term: GlossaryTerm, afterUpdate: Boolean = false) {
        assertEquals("Test term 1 for asset import package term-to-term relationships.", term.userDescription)
        assertEquals(AtlanStatus.ACTIVE, term.status)
        if (afterUpdate) {
            assertEquals("", term.readme?.description ?: "")
            assertTrue(term.seeAlso.isNullOrEmpty())
        } else {
            assertNotNull(term.readme)
            assertEquals("<h1>This is term1!</h1>", term.readme.description)
        }
    }

    private fun validateTerm2(term: GlossaryTerm, afterUpdate: Boolean = false) {
        assertEquals("Test term 2 for asset import package term-to-term relationships.", term.userDescription)
        assertEquals(AtlanStatus.ACTIVE, term.status)
        if (afterUpdate) {
            assertEquals("", term.readme?.description ?: "")
            assertTrue(term.seeAlso.isNullOrEmpty())
        } else {
            assertNotNull(term.readme)
            assertEquals("<h2>This is term2.</h2>", term.readme.description)
        }
    }

    @Test(dependsOnGroups = ["aim.rr.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.rr.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
