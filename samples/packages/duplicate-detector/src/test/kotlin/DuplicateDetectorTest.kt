/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.PackageTest
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test detection of duplicate assets.
 */
class DuplicateDetectorTest : PackageTest() {

    private val testId = makeUnique("dupdetect")
    private val files = listOf(
        "debug.log",
    )
    private val glossaryName = testId

    @BeforeClass
    fun beforeClass() {
        setup(
            DuplicateDetectorCfg(
                glossaryName = glossaryName,
                qnPrefix = "default/snowflake",
                controlConfigStrategy = "default",
                assetTypes = listOf(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME),
            ),
        )
        DuplicateDetector.main(arrayOf())
    }

    @Test
    fun glossaryCreated() {
        val glossary = Glossary.findByName(glossaryName)
        assertNotNull(glossary)
        assertEquals(testId, glossary.name)
    }

    @Test
    fun termsCreated() {
        val glossaryQN = Glossary.findByName(glossaryName).qualifiedName!!
        val terms = GlossaryTerm.select()
            .where(GlossaryTerm.ANCHOR.eq(glossaryQN))
            .includeOnResults(GlossaryTerm.DESCRIPTION)
            .includeOnResults(GlossaryTerm.ASSIGNED_ENTITIES)
            .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS)
            .stream()
            .toList()
        assertTrue(terms.size > 0)
        terms.forEach { term ->
            term as GlossaryTerm
            assertTrue(term.name.startsWith("Dup. ("))
            assertTrue(term.assignedEntities.size > 0)
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

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        val glossary = Glossary.findByName(glossaryName)!!
        val terms = GlossaryTerm.select()
            .where(GlossaryTerm.ANCHOR.eq(glossary.qualifiedName))
            .stream()
            .map { it.guid }
            .toList()
        Atlan.getDefaultClient().assets.delete(terms, AtlanDeleteType.HARD)
        Glossary.purge(glossary.guid)
        teardown(context.failedTests.size() > 0)
    }
}
