/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdateSubdomainTest : PackageTest("usd") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val dataDomain = makeUnique("d1")
    private val subDomain = makeUnique("sd1")
    private lateinit var dd: DataDomain
    private lateinit var sd: DataDomain
    private val testFile = "subdomain-alone.csv"

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
                        .replace("{{SUBDOMAIN}}", subDomain)
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
            DataDomain.SUB_DOMAINS,
        )

    private fun createAssets() {
        val ddCreate = DataDomain.creator(dataDomain).build()
        val ddResponse = ddCreate.save(client)
        dd = ddResponse.getResult(ddCreate)
        val sdCreate =
            DataDomain.creator(subDomain, dd.qualifiedName).build()
        val sdResponse = sdCreate.save(client)
        sd = sdResponse.getResult(sdCreate)
    }

    override fun setup() {
        prepFile()
        createAssets()
    }

    override fun teardown() {
        removeDomain(subDomain)
        removeDomain(dataDomain)
    }

    @Test(groups = ["aim.usd.create"])
    fun exactlyTwoDomains() {
        val d1 = findDataDomain(dataDomain)
        assertEquals(dataDomain, d1.name)
        assertFalse(d1.subDomains.isNullOrEmpty())
        assertEquals(1, d1.subDomains.size)
        val d2 = findDataDomain(subDomain)
        assertEquals(subDomain, d2.name)
        assertTrue(d2.subDomains.isNullOrEmpty())
    }

    @Test(groups = ["aim.usd.runUpsert"], dependsOnGroups = ["aim.usd.create"])
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

    @Test(groups = ["aim.usd.testUpsert"], dependsOnGroups = ["aim.usd.runUpsert"])
    fun domainsUnchanged() {
        exactlyTwoDomains()
    }

    private fun findDataDomain(domainName: String): DataDomain {
        val request =
            DataDomain
                .select(client)
                .where(DataDomain.NAME.eq(domainName))
                .includesOnResults(dataDomainAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val dataDomains = response.stream().filter { a: Asset? -> a is DataDomain }.toList()
        assertEquals(1, dataDomains.size)
        return dataDomains[0] as DataDomain
    }

    @Test(dependsOnGroups = ["aim.usd.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.usd.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
