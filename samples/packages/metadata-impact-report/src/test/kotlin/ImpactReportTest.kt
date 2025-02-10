/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.serde.xls.ExcelReader
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertTrue
import java.io.File
import kotlin.test.Test

/**
 * Test detection of duplicate assets.
 */
class ImpactReportTest : PackageTest("ir") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val dataDomain = makeUnique("d1")
    private val files =
        listOf(
            "debug.log",
            "mdir.xlsx",
        )

    override fun setup() {
        runCustomPackage(
            MetadataImpactReportCfg(
                dataDomain = dataDomain,
                includeDetails = true,
            ),
            Reporter::main,
        )
    }

    override fun teardown() {
        removeDomainAndChildren(dataDomain)
    }

    @Test(groups = ["mdir.create"])
    fun domainCreated() {
        val domain = DataDomain.findByName(client, dataDomain)
        assertNotNull(domain)
        assertEquals(dataDomain, domain[0].name)
    }

    @Test(groups = ["mdir.create"])
    fun subDomainsCreated() {
        val domain = DataDomain.findByName(client, dataDomain)
        val subDomains =
            DataDomain
                .select(client)
                .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.eq(domain[0].qualifiedName))
                .stream()
                .toList()
        assertEquals(3, subDomains.size)
        subDomains.forEach { subDomain ->
            when (subDomain.name) {
                Reporter.CAT_SAVINGS -> assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_SAVINGS], subDomain.description)
                Reporter.CAT_HEADLINES -> assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_HEADLINES], subDomain.description)
                Reporter.CAT_ADOPTION -> assertEquals(Reporter.SUBDOMAINS[Reporter.CAT_ADOPTION], subDomain.description)
            }
        }
    }

    @Test(groups = ["mdir.create"])
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
        assertEquals(21, products.size)
    }

    @Test(groups = ["mdir.runUpdate"], dependsOnGroups = ["mdir.create"])
    fun rerunReport() {
        runCustomPackage(
            MetadataImpactReportCfg(
                dataDomain = dataDomain,
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
