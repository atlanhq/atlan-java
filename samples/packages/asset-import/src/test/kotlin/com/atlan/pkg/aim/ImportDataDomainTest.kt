/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.IDataMesh
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ImportDataDomainTest : PackageTest() {

    private val dataDomain1 = makeUnique("idd1")
    private val dataDomain2 = makeUnique("idd2")
    private val dataDomain3 = makeUnique("idd3")
    private val dataProduct1 = makeUnique("idp1")
    private val tag1 = makeUnique("idt1")

    private val testFile = "input.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", "data_domain.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{{DATADOMAIN1}}", dataDomain1)
                    .replace("{{DATADOMAIN2}}", dataDomain2)
                    .replace("{{DATADOMAIN3}}", dataDomain3)
                    .replace("{{DATAPRODUCT1}}", dataProduct1)
                    .replace("{{TAG1}}", tag1)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createTags() {
        val maxNetworkRetries = 30
        val client = Atlan.getDefaultClient()
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.AIRPLANE, AtlanTagColor.GREEN).build()
//        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.ROBOT, AtlanTagColor.RED).build()
        client.typeDefs.create(
            listOf(t1),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
    }

    private val dataDomainAttrs: List<AtlanField> = listOf(
        DataDomain.NAME,
        DataDomain.USER_DESCRIPTION,
        DataDomain.OWNER_USERS,
        DataDomain.OWNER_GROUPS,
        DataDomain.CERTIFICATE_STATUS,
        DataDomain.CERTIFICATE_STATUS_MESSAGE,
        DataDomain.PARENT_DOMAIN,
        DataDomain.PARENT_DOMAIN_QUALIFIED_NAME ,
        DataDomain.SUPER_DOMAIN_QUALIFIED_NAME
    )

    private val dataProductAttrs: List<AtlanField> = listOf(
        DataProduct.NAME,
        DataProduct.USER_DESCRIPTION,
        DataProduct.OWNER_USERS,
        DataProduct.OWNER_GROUPS,
        DataProduct.CERTIFICATE_STATUS,
        DataProduct.CERTIFICATE_STATUS_MESSAGE,
        DataProduct.DATA_DOMAIN
    )

    @BeforeClass
    fun beforeClass() {
        prepFile()
        createTags()
        setup(
            AssetImportCfg(
                assetsFile = null,
                assetsUpsertSemantic = null,
                assetsAttrToOverwrite = null,
                assetsFailOnErrors = true,
                glossariesFile = null,
                glossariesUpsertSemantic = null,
                glossariesAttrToOverwrite = null,
                glossariesFailOnErrors = true,
                dataProductsFile = Paths.get(testDirectory, testFile).toString(),
                dataProductsUpsertSemantic = "upsert",
                dataProductsAttrToOverwrite = listOf(),
                dataProductsFailOnErrors = true,
            ),
        )
        Importer.main(arrayOf(testDirectory))
    }

    @Test
    fun domain1Created() {
        val level1_domains = DataDomain.findByName(dataDomain1, dataDomainAttrs)
        assertNotNull(level1_domains)
        assertEquals(level1_domains.size, 1)
        val d1 = level1_domains[0]
        assertEquals(dataDomain1, d1.name)
        assertEquals("Test domain for asset import", d1.userDescription)
        assertEquals(setOf("ernest"), d1.ownerUsers)
        assertEquals(setOf("admins"), d1.ownerGroups)
        assertEquals(d1.qualifiedName, DataDomain.generateQualifiedName(IDataMesh.generateSlugForName(dataDomain1), null))
        val level2_domains = DataDomain.findByName(dataDomain2, dataDomainAttrs)
        assertNotNull(level2_domains)
        assertEquals(level2_domains.size, 1)
        assertEquals(CertificateStatus.VERIFIED, d1.certificateStatus)
        val d2 = level2_domains[0]
        assertEquals(dataDomain2, d2.name)
        assertEquals("Test subdomain", d2.userDescription)
        assertEquals(setOf("ernest"), d2.ownerUsers)
        assertEquals(setOf("admins"), d2.ownerGroups)
        assertEquals(d1.guid, d2.parentDomain.guid)
        assertEquals(d2.qualifiedName, DataDomain.generateQualifiedName(IDataMesh.generateSlugForName(dataDomain2), d1.qualifiedName))
        assertEquals(d2.parentDomainQualifiedName, d1.qualifiedName)
        assertEquals(d1.qualifiedName, d2.superDomainQualifiedName)
        val level3_domains = DataDomain.findByName(dataDomain3, dataDomainAttrs)
        assertNotNull(level3_domains)
        assertEquals(level3_domains.size, 1)
        assertEquals(CertificateStatus.DRAFT, d2.certificateStatus)
        assertEquals("With a message!", d2.certificateStatusMessage)
        val d3 = level3_domains[0]
        assertEquals(dataDomain3, d3.name)
        assertEquals("Test sub sub domain", d3.userDescription)
        assertEquals(setOf("ernest"), d3.ownerUsers)
        assertEquals(setOf("admins"), d3.ownerGroups)
        assertEquals(d2.guid, d3.parentDomain.guid)
        assertEquals(d3.qualifiedName, DataDomain.generateQualifiedName(IDataMesh.generateSlugForName(dataDomain3), d2.qualifiedName))
        assertEquals(d3.parentDomainQualifiedName, d2.qualifiedName)
        assertEquals(d1.qualifiedName, d3.superDomainQualifiedName)
        val p1 = findDataProductWithRetry(dataProduct1)
        assertEquals(dataProduct1, p1.name)
        assertEquals("Test data product", p1.userDescription)
        assertEquals(setOf("ernest"), p1.ownerUsers)
        assertEquals(setOf("admins"), p1.ownerGroups)
        assertEquals(d3.qualifiedName, p1.dataDomain.qualifiedName)
    }

    private fun findDataProductWithRetry(productName: String): DataProduct {
        val request = DataProduct.select()
            .where(DataProduct.NAME.eq(productName))
            .includesOnResults(dataProductAttrs)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        return response.stream().filter { a: Asset? -> a is DataProduct }.findFirst().get() as DataProduct
    }


    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeDomain(dataDomain1)
        removeDomain(dataDomain2)
        removeDomain(dataDomain3)
        removeProduct(dataProduct1)
        removeTag(tag1)
//        removeTag(tag2)
        teardown(context.failedTests.size() > 0)
    }
}
