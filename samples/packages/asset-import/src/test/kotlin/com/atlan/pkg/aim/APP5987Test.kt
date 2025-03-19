/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class APP5987Test : PackageTest("idd") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.ORACLE
    private lateinit var connection: Connection
    private val dataDomain1 = makeUnique("d1")
    private lateinit var d1: DataDomain
    private val testFile = "asset-import-domain.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    private fun createDatabase(connection: Connection): Database {
        val d1 = Database.creator("DB", connection.qualifiedName).build()
        val response = d1.save(client)
        return response.getResult(d1)
    }

    private fun createSchema(database: Database): Schema {
        val s1 = Schema.creator("SCH", database.qualifiedName).build()
        val response = s1.save(client)
        return response.getResult(s1)
    }

    private fun createTable(schema: Schema): Table {
        val t1 = Table.creator("TBL", schema.qualifiedName).build()
        val response = t1.save(client)
        return response.getResult(t1)
    }

    private fun createDomain(): DataDomain {
        val d1 = DataDomain.creator(dataDomain1).build()
        val response = d1.save(client)
        return response.getResult(d1)
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
        )

    private val tableAttrs: List<AtlanField> =
        listOf(
            Table.NAME,
            Table.STATUS,
            Table.CONNECTION_QUALIFIED_NAME,
            Table.CONNECTOR_TYPE,
            Table.DATABASE_NAME,
            Table.DATABASE_QUALIFIED_NAME,
            Table.SCHEMA_NAME,
            Table.SCHEMA_QUALIFIED_NAME,
            Table.SCHEMA,
        )

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
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        connection = createConnection()
        val database = createDatabase(connection)
        val schema = createSchema(database)
        val table = createTable(schema)
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

    @Test
    fun tableUpdated() {
        validateTable()
    }

    private fun validateTable() {
        val c1 = Connection.findByName(client, connectionName, connectorType, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Schema.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val tbl = found[0] as Table
    }

    override fun teardown() {
        removeDomain(dataDomain1)
        removeConnection(connectionName, connectorType)
    }

    @Test
    fun domain1Created() {
        assertEquals(dataDomain1, d1.name)
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
