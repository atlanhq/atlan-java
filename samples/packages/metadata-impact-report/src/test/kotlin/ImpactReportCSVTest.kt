/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.serde.csv.CSVXformer
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import org.testng.Assert
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test detection of duplicate assets.
 */
class ImpactReportCSVTest : PackageTest("irc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val dataDomain = makeUnique("cd1")
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
                dataDomain = dataDomain,
                includeDetails = true,
                fileFormat = "CSV",
            ),
            Reporter::main,
        )
    }

    override fun teardown() {
        removeDomainAndChildren(dataDomain)
    }

    @Test
    fun domainCreated() {
        val domain = DataDomain.findByName(client, dataDomain)
        Assert.assertNotNull(domain)
        Assert.assertEquals(dataDomain, domain[0].name)
    }

    @Test
    fun subDomainsCreated() {
        val domain = DataDomain.findByName(client, dataDomain)
        val subDomains =
            DataDomain
                .select(client)
                .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.eq(domain[0].qualifiedName))
                .stream()
                .toList()
        Assert.assertEquals(3, subDomains.size)
        subDomains.forEach { subDomain ->
            when (subDomain.name) {
                Reporter.CAT_SAVINGS -> Assert.assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_SAVINGS], subDomain.description)
                Reporter.CAT_HEADLINES -> Assert.assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_HEADLINES], subDomain.description)
                Reporter.CAT_ADOPTION -> Assert.assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_ADOPTION], subDomain.description)
            }
        }
    }

    @Test
    fun dataProductsCreated() {
        val domain = DataDomain.findByName(client, dataDomain)
        val subDomains =
            DataDomain
                .select(client)
                .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.eq(domain[0].qualifiedName))
                .stream()
                .toList()
        val products = mutableListOf<Asset>()
        subDomains.forEach { subDomain ->
            val subProducts =
                DataProduct
                    .select(client)
                    .where(DataProduct.PARENT_DOMAIN_QUALIFIED_NAME.eq(subDomain.qualifiedName))
                    .stream()
                    .toList()
            products.addAll(subProducts)
        }
        Assert.assertEquals(21, products.size)
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
