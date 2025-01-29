/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.serde.csv.CSVXformer
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test detection of duplicate assets.
 */
class ImpactReportCSVTest : PackageTest("irc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossaryName = makeUnique("g1")
    private val files =
        listOf(
            "debug.log",
            "overview.csv",
            "dlaxl.csv",
            "hqv.csv",
            "sut.csv",
            "tlaxl.csv",
            "tlaxq.csv",
            "tlaxu.csv",
        )

    override fun setup() {
        runCustomPackage(
            MetadataImpactReportCfg(
                glossaryName = glossaryName,
                includeDetails = true,
                fileFormat = "CSV",
            ),
            Reporter::main,
        )
    }

    override fun teardown() {
        removeGlossary(glossaryName)
    }

    @Test
    fun glossaryCreated() {
        val glossary = Glossary.findByName(client, glossaryName)
        assertNotNull(glossary)
        assertEquals(glossaryName, glossary.name)
    }

    @Test
    fun categoriesCreated() {
        val glossaryQN = Glossary.findByName(client, glossaryName).qualifiedName!!
        val request =
            GlossaryCategory
                .select(client)
                .where(GlossaryCategory.ANCHOR.eq(glossaryQN))
                .includeOnResults(GlossaryCategory.NAME)
                .includeOnResults(GlossaryCategory.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 3)
        val categories = response.assets
        assertEquals(3, categories.size)
        categories.forEach { category ->
            when (category.name) {
                Reporter.CAT_HEADLINES -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_HEADLINES], category.description)
                Reporter.CAT_SAVINGS -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_SAVINGS], category.description)
                Reporter.CAT_ADOPTION -> assertEquals(Reporter.CATEGORIES[Reporter.CAT_ADOPTION], category.description)
            }
        }
    }

    @Test
    fun termsCreated() {
        val glossaryQN = Glossary.findByName(client, glossaryName).qualifiedName!!
        val request =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossaryQN))
                .includeOnResults(GlossaryTerm.DISPLAY_NAME)
                .includeOnResults(GlossaryTerm.DESCRIPTION)
                .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS)
                .includeOnResults(GlossaryTerm.CERTIFICATE_STATUS_MESSAGE)
                .includeOnResults(GlossaryTerm.ANNOUNCEMENT_TYPE)
                .includeOnResults(GlossaryTerm.ANNOUNCEMENT_TITLE)
                .includeOnResults(GlossaryTerm.CATEGORIES)
                .includeOnRelations(GlossaryCategory.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 21)
        val terms = response.stream().toList()
        assertEquals(21, terms.size)
        terms.forEach { term ->
            term as GlossaryTerm
            assertFalse(term.certificateStatusMessage.isNullOrBlank())
            assertFalse(term.description.isNullOrBlank())
            assertTrue(term.name.startsWith(term.displayName))
            when (term.name) {
                "AUM - Assets Under Management" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "AwD - Assets with Descriptions" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.INFORMATION, term.announcementType)
                    assertEquals("Note", term.announcementTitle)
                }
                "AwDC - Assets with Descriptions Crawled" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "AwDU - Assets with Descriptions User-Entered" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "AwO - Assets with Owners" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.INFORMATION, term.announcementType)
                    assertEquals("Note", term.announcementTitle)
                }
                "AwOG - Assets with Owners - Groups" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "AwOU - Assets with Owners - Users" -> {
                    assertEquals(Reporter.CAT_ADOPTION, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "DLA - Dashboard-Level Assets" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "DLAxL - Dashboard-Level Assets without Lineage" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "GCM - Glossary Categories Managed" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "GTM - Glossary Terms Managed" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "GUM - Glossaries Under Management" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "HQV - Highly-Queried Views" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "SUT - Single-User Tables" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "TLA - Table-Level Assets" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "TLAwL - Table-Level Assets with Lineage" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "TLAxL - Table-Level Assets without Lineage" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "TLAxQ - Table-Level Assets without Queries" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "TLAxU - Table-Level Assets without Usage" -> {
                    assertEquals(Reporter.CAT_SAVINGS, term.categories.first().name)
                    assertEquals(CertificateStatus.DRAFT, term.certificateStatus)
                    assertEquals(AtlanAnnouncementType.WARNING, term.announcementType)
                    assertEquals("Caveats", term.announcementTitle)
                }
                "UTA - Usage-Tracked Assets" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
                "UTQ - Usage-Tracked Queries" -> {
                    assertEquals(Reporter.CAT_HEADLINES, term.categories.first().name)
                    assertEquals(CertificateStatus.VERIFIED, term.certificateStatus)
                }
            }
        }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun excelIsEmpty() {
        val xlFile = "$testDirectory${File.separator}mdir.xlsx"
        assertTrue(File(xlFile).isFile)
        assertEquals(0, File(xlFile).length())
    }

    @Test
    fun testOverview() {
        val file = "$testDirectory${File.separator}overview.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .ignoreDifferentFieldCount(false)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
                assertFalse(row["Metric"].isNullOrBlank())
                assertFalse(row["Description"].isNullOrBlank())
                assertFalse(row["Result"].isNullOrBlank())
            }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
