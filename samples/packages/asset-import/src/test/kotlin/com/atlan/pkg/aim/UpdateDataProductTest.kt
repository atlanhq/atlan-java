/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Readme
import com.atlan.model.enums.DataProductStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateDataProductTest : PackageTest("udp") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val dataDomain = makeUnique("d1")
    private val dataProduct = makeUnique("p1")
    private lateinit var dd: DataDomain
    private lateinit var dp: DataProduct
    private val testFile = "product-with-domain.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for domains and products
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{DATADOMAIN}}", dataDomain)
                        .replace("{{DATAPRODUCT}}", dataProduct)
                output.appendText("$revised\n")
            }
        }
    }

    private val dataDomainAttrs: List<AtlanField> =
        listOf(
            DataDomain.NAME,
            DataDomain.ASSET_ICON,
            DataDomain.ASSET_THEME_HEX,
            DataDomain.ASSET_COVER_IMAGE,
            DataDomain.USER_DESCRIPTION,
            DataDomain.OWNER_USERS,
            DataDomain.OWNER_GROUPS,
            DataDomain.CERTIFICATE_STATUS,
            DataDomain.CERTIFICATE_STATUS_MESSAGE,
            DataDomain.PARENT_DOMAIN,
            DataDomain.PARENT_DOMAIN_QUALIFIED_NAME,
            DataDomain.SUPER_DOMAIN_QUALIFIED_NAME,
            DataDomain.ANNOUNCEMENT_TYPE,
            DataDomain.ANNOUNCEMENT_TITLE,
            DataDomain.ANNOUNCEMENT_MESSAGE,
            DataDomain.README,
        )

    private val dataProductAttrs: List<AtlanField> =
        listOf(
            DataProduct.NAME,
            DataProduct.ASSET_COVER_IMAGE,
            DataProduct.USER_DESCRIPTION,
            DataProduct.OWNER_USERS,
            DataProduct.OWNER_GROUPS,
            DataProduct.CERTIFICATE_STATUS,
            DataProduct.CERTIFICATE_STATUS_MESSAGE,
            DataProduct.DATA_PRODUCT_STATUS,
            DataProduct.DAAP_STATUS,
            DataProduct.DATA_DOMAIN,
            DataProduct.README,
        )

    private fun createAssets() {
        val ddCreate = DataDomain.creator(dataDomain).build()
        val ddResponse = ddCreate.save(client)
        dd = ddResponse.getResult(ddCreate)
        val dpCreate =
            DataProduct
                .creator(client, dataProduct, dd.qualifiedName, GlossaryTerm.select(client).build())
                .dataProductStatus(DataProductStatus.DRAFT)
                .daapStatus(DataProductStatus.DRAFT)
                .build()
        val dpResponse = dpCreate.save(client)
        dp = dpResponse.getResult(dpCreate)
    }

    override fun setup() {
        prepFile()
        createAssets()
    }

    override fun teardown() {
        removeProduct(dataProduct)
        removeDomain(dataDomain)
    }

    @Test(groups = ["aim.udp.create"])
    fun domainUnchanged() {
        val d1 = findDataDomain(dataDomain)
        assertEquals(dataDomain, d1.name)
    }

    @Test(groups = ["aim.udp.create"])
    fun productStatusInitial() {
        val p1 = findDataProductWithRetry(dataProduct)
        assertEquals(dataProduct, p1.name)
        assertEquals(DataProductStatus.DRAFT, p1.dataProductStatus)
        assertEquals(DataProductStatus.DRAFT, p1.daapStatus)
    }

    @Test(groups = ["aim.udp.runUpsert"], dependsOnGroups = ["aim.udp.create"])
    fun upsert() {
        runCustomPackage(
            AssetImportCfg(
                dataProductsFile = Paths.get(testDirectory, testFile).toString(),
                dataProductsUpsertSemantic = "upsert",
                dataProductsAttrToOverwrite = listOf(),
                dataProductsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    @Test(groups = ["aim.udp.testUpsert"], dependsOnGroups = ["aim.udp.runUpsert"])
    fun productStatusUnchanged() {
        val p1 = findDataProductWithRetry(dataProduct)
        assertEquals(dataProduct, p1.name)
        assertEquals(DataProductStatus.DRAFT, p1.dataProductStatus)
        assertEquals(DataProductStatus.DRAFT, p1.daapStatus)
    }

    @Test(groups = ["aim.udp.runUpdate"], dependsOnGroups = ["aim.udp.testUpsert"])
    fun update() {
        runCustomPackage(
            AssetImportCfg(
                dataProductsFile = Paths.get(testDirectory, testFile).toString(),
                dataProductsUpsertSemantic = "update",
                dataProductsAttrToOverwrite = listOf(),
                dataProductsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    @Test(dependsOnGroups = ["aim.udp.runUpdate"])
    fun productStatusStillUnchanged() {
        val p1 = findDataProductWithRetry(dataProduct)
        assertEquals(dataProduct, p1.name)
        assertEquals(DataProductStatus.DRAFT, p1.dataProductStatus)
        assertEquals(DataProductStatus.DRAFT, p1.daapStatus)
    }

    private fun findDataDomain(domainName: String): DataDomain {
        val request =
            DataDomain
                .select(client)
                .where(DataDomain.NAME.eq(domainName))
                .includesOnResults(dataDomainAttrs)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val dataDomains = response.stream().filter { a: Asset? -> a is DataDomain }.toList()
        assertEquals(1, dataDomains.size)
        return dataDomains[0] as DataDomain
    }

    private fun findDataProductWithRetry(productName: String): DataProduct {
        val request =
            DataProduct
                .select(client)
                .where(DataProduct.NAME.eq(productName))
                .includesOnResults(dataProductAttrs)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        return response
            .stream()
            .filter { a: Asset? -> a is DataProduct }
            .findFirst()
            .get() as DataProduct
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
