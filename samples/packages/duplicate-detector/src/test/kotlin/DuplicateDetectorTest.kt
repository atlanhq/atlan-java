/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test detection of duplicate assets.
 */
class DuplicateDetectorTest : PackageTest("dd") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val testId = makeUnique("dd")
    private val files =
        listOf(
            "debug.log",
        )
    private val glossaryName = testId

    override fun setup() {
        runCustomPackage(
            DuplicateDetectorCfg(
                glossaryName = glossaryName,
                qnPrefix = "default/snowflake",
                controlConfigStrategy = "default",
                assetTypes = listOf(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME),
            ),
            DuplicateDetector::main,
        )
    }

    override fun teardown() {
        removeGlossary(glossaryName)
    }

    @Test
    fun glossaryCreated() {
        val glossary = Glossary.findByName(client, glossaryName)
        assertNotNull(glossary)
        assertEquals(testId, glossary.name)
    }

    @Test
    fun termsCreated() {
        val glossaryQN = Glossary.findByName(client, glossaryName).qualifiedName!!
        val terms =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossaryQN))
                .includeOnResults(GlossaryTerm.DESCRIPTION)
                .includeOnResults(GlossaryTerm.ASSIGNED_ENTITIES)
                .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS)
                .stream()
                .toList()
        assertTrue(terms.isNotEmpty())
        terms.forEach { term ->
            term as GlossaryTerm
            assertTrue(term.name.startsWith("Dup. ("))
            assertTrue(term.assignedEntities.isNotEmpty())
            assertTrue(term.description.startsWith("Assets with the same set of"))
            assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
        }
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
