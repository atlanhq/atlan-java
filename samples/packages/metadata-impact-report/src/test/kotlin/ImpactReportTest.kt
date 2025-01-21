/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.serde.xls.ExcelReader
import org.testng.Assert.assertTrue
import java.io.File
import kotlin.test.Test

/**
 * Test detection of duplicate assets.
 */
class ImpactReportTest : PackageTest("ir") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossaryName = makeUnique("g1")
    private val files =
        listOf(
            "debug.log",
            "mdir.xlsx",
        )

    override fun setup() {
        runCustomPackage(
            MetadataImpactReportCfg(
                glossaryName = glossaryName,
                includeDetails = true,
            ),
            Reporter::main,
        )
    }

    override fun teardown() {
        removeGlossary(glossaryName)
    }

//    @Test(groups = ["mdir.create"])
//    fun glossaryCreated() {
//        val glossary = Glossary.findByName(client, glossaryName)
//        assertNotNull(glossary)
//        assertEquals(glossaryName, glossary.name)
//    }

//    @Test(groups = ["mdir.create"])
//    fun categoriesCreated() {
//        val glossaryQN = Glossary.findByName(client, glossaryName).qualifiedName!!
//        val request =
//            GlossaryCategory
//                .select(client)
//                .where(GlossaryCategory.ANCHOR.eq(glossaryQN))
//                .includeOnResults(GlossaryCategory.NAME)
//                .includeOnResults(GlossaryCategory.DESCRIPTION)
//                .toRequest()
//        val response = retrySearchUntil(request, 3)
//        val categories = response.assets
//        assertEquals(3, categories.size)
//        categories.forEach { category ->
//            when (category.name) {
//                Reporter.CAT_HEADLINES -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_HEADLINES], category.description)
//                Reporter.CAT_SAVINGS -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_SAVINGS], category.description)
//                Reporter.CAT_ADOPTION -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_ADOPTION], category.description)
//            }
//        }
//    }

//    @Test(groups = ["mdir.create"])
//    fun termsCreated() {
//        val glossaryQN = Glossary.findByName(client, glossaryName).qualifiedName!!
//        val request =
//            GlossaryTerm
//                .select(client)
//                .where(GlossaryTerm.ANCHOR.eq(glossaryQN))
//                .includeOnResults(GlossaryTerm.DISPLAY_NAME)
//                .includeOnResults(GlossaryTerm.DESCRIPTION)
//                .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS)
//                .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS_MESSAGE)
//                .includeOnResults(GlossaryTerm.ANNOUNCEMENT_TYPE)
//                .includeOnResults(GlossaryTerm.ANNOUNCEMENT_TITLE)
//                .includeOnResults(GlossaryTerm.CATEGORIES)
//                .includeOnRelations(GlossaryCategory.NAME)
//                .toRequest()
//        val response = retrySearchUntil(request, 21)
//        val terms = response.stream().toList()
//        assertEquals(21, terms.size)
//        terms.forEach { term ->
//            term as GlossaryTerm
//            assertFalse(term.certificateStatusMessage.isNullOrBlank())
//            assertFalse(term.description.isNullOrBlank())
//            assertTrue(term.name.startsWith(term.displayName))
//            when (term.name) {
//                "AUM - Assets Under Management" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "AwD - Assets with Descriptions" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.INFORMATION, term.announcementType)
//                    assertEquals("Note", term.announcementTitle)
//                }
//                "AwDC - Assets with Descriptions Crawled" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "AwDU - Assets with Descriptions User-Entered" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "AwO - Assets with Owners" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.INFORMATION, term.announcementType)
//                    assertEquals("Note", term.announcementTitle)
//                }
//                "AwOG - Assets with Owners - Groups" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "AwOU - Assets with Owners - Users" -> {
//                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "DLA - Dashboard-Level Assets" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "DLAxL - Dashboard-Level Assets without Lineage" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "GCM - Glossary Categories Managed" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "GTM - Glossary Terms Managed" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "GUM - Glossaries Under Management" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "HQV - Highly-Queried Views" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "SUT - Single-User Tables" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "TLA - Table-Level Assets" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "TLAwL - Table-Level Assets with Lineage" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "TLAxL - Table-Level Assets without Lineage" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "TLAxQ - Table-Level Assets without Queries" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "TLAxU - Table-Level Assets without Usage" -> {
//                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
//                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
//                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
//                    assertEquals("Caveats", term.announcementTitle)
//                }
//                "UTA - Usage-Tracked Assets" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//                "UTQ - Usage-Tracked Queries" -> {
//                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
//                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
//                }
//            }
//        }
//    }

//    @Test(groups = ["mdir.runUpdate"], dependsOnGroups = ["mdir.create"])
    @Test(groups = ["mdir.runUpdate"])
    fun rerunReport() {
        runCustomPackage(
            MetadataImpactReportCfg(
                includeGlossary = "TRUE",
                glossaryName = glossaryName,
                includeDetails = true,
            ),
            Reporter::main,
        )
    }

    @Test(dependsOnGroups = ["mdir.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["mdir.*"])
    fun hasExpectedSheets() {
        val xlFile = "$testDirectory${File.separator}mdir.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            assertTrue(xlsx.hasSheet("Overview"))
            assertTrue(xlsx.hasSheet("DLAxL"))
            assertTrue(xlsx.hasSheet("HQV"))
            assertTrue(xlsx.hasSheet("SUT"))
            assertTrue(xlsx.hasSheet("TLAxL"))
            assertTrue(xlsx.hasSheet("TLAxQ"))
            assertTrue(xlsx.hasSheet("TLAxU"))
        }
    }

    @Test(dependsOnGroups = ["mdir.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
