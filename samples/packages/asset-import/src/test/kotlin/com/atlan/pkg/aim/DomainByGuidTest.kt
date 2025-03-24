/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Database
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class DomainByGuidTest : PackageTest("dbg") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private lateinit var connection: Connection
    private val dataDomain1 = makeUnique("d1")
    private val db = makeUnique("db1")
    private lateinit var d1: DataDomain
    private val testFile = "domain-by-guid.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun createDomain(): DataDomain {
        val d1 = DataDomain.creator(dataDomain1).build()
        val response = d1.save(client)
        return response.getResult(d1)
    }

    private fun prepFile(
        connectionQN: String = connection.qualifiedName,
        dataDomainGuid: String = d1.guid,
    ) {
        // Prepare a copy of the file with unique names for domains and products
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{DATADOMAIN_GUID}}", dataDomainGuid)
                        .replace("{{CONNECTION}}", connectionQN)
                        .replace("{{DB}}", db)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        d1 = createDomain()
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        Database
            .select(client)
            .where(Database.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
            .where(Database.NAME.eq(db))
            .stream()
            .findFirst()
            .ifPresent {
                Database.purge(client, it.guid)
            }
        removeDomain(dataDomain1)
    }

    @Test
    fun tableUpdated() {
        val request =
            Table
                .select(client)
                .where(Asset.DOMAIN_GUIDS.eq(d1.guid))
                .where(Asset.NAME.eq(db))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
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
