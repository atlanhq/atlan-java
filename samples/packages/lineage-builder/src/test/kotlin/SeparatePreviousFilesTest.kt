/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanStatus
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.lb.Loader
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test that a Lineage Builder run between two Full Replacement runs of Asset Import on the same
 * connection does not stop Asset Import from removing an asset that drops out of its file.
 */
class SeparatePreviousFilesTest : PackageTest("spf") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.IBM_DB2
    private lateinit var connection: Connection

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, conn1, conn1Type).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    private fun prepAssets(
        filename: String,
        tables: List<String>,
    ): String {
        val cq = connection.qualifiedName
        val ct = conn1Type.value
        val output = Paths.get(testDirectory, filename).toFile()
        output.writeText(
            "qualifiedName,typeName,name,connectorType,connectionQualifiedName," +
                "databaseQualifiedName,databaseName,schemaQualifiedName,schemaName,description,database,schema\n",
        )
        output.appendText("$cq/DB,Database,DB,$ct,$cq,,,,,,,\n")
        output.appendText("$cq/DB/SCH,Schema,SCH,$ct,$cq,$cq/DB,DB,,,,Database@$cq/DB,\n")
        tables.forEach { tbl ->
            output.appendText("$cq/DB/SCH/$tbl,Table,$tbl,$ct,$cq,$cq/DB,DB,$cq/DB/SCH,SCH,,,Schema@$cq/DB/SCH\n")
        }
        return output.path
    }

    private fun prepLineage(filename: String): String {
        val ct = conn1Type.value
        val output = Paths.get(testDirectory, filename).toFile()
        output.writeText(
            "Source Type,Source Connector,Source Connection,Source Identity,Source Name," +
                "Target Type,Target Connector,Target Connection,Target Identity,Target Name," +
                "Transformation Connector,Transformation Connection,Transformation Identity,Transformation Name\n",
        )
        output.appendText("Table,$ct,$conn1,DB/SCH/T1,T1,Table,$ct,$conn1,DB/SCH/T2,T2,$ct,$conn1,DB/SCH/T1_to_T2,T1 > T2\n")
        return output.path
    }

    override fun setup() {
        connection = createConnection()
        // 1. Full Replacement import of T1, T2 and T3
        runCustomPackage(
            AssetImportCfg(
                assetsFile = prepAssets("assets1.csv", listOf("T1", "T2", "T3")),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
            ),
            Importer::main,
        )
        // 2. Lineage Builder on the same connection, between the two imports
        runCustomPackage(
            LineageBuilderCfg(
                lineageFile = prepLineage("lineage.csv"),
                lineageUpsertSemantic = "upsert",
                lineageFailOnErrors = false,
            ),
            Loader::main,
        )
        // 3. Full Replacement import again, with T3 dropped from the file
        runCustomPackage(
            AssetImportCfg(
                assetsFile = prepAssets("assets2.csv", listOf("T1", "T2")),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test
    fun droppedTableArchived() {
        val request =
            Table
                .select(client, true)
                .where(Table.QUALIFIED_NAME.eq("${connection.qualifiedName}/DB/SCH/T3"))
                .where(Table.STATUS.eq(AtlanStatus.DELETED))
                .includeOnResults(Table.STATUS)
                .toRequest()
        val response = retrySearchUntil(request, 1, true)
        assertEquals(1, response.assets.size)
        assertEquals(AtlanStatus.DELETED, response.assets[0].status)
    }

    @Test
    fun keptTablesStillActive() {
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(Table.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        assertEquals(setOf("T1", "T2"), response.assets.map { it.name }.toSet())
    }

    @Test
    fun lineageStillActive() {
        val request =
            LineageProcess
                .select(client)
                .where(LineageProcess.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includeOnResults(LineageProcess.STATUS)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        assertEquals(1, response.assets.size)
        assertEquals(AtlanStatus.ACTIVE, response.assets[0].status)
    }

    private fun savedFiles(prefix: String): List<String> =
        Paths
            .get(testDirectory, prefix, connection.qualifiedName)
            .toFile()
            .listFiles()
            ?.map { it.name }
            ?.filter { it.endsWith(".processed") }
            ?: listOf()

    @Test
    fun eachPackageSavesToItsOwnPrefix() {
        // Two Asset Import runs, one saved file each
        assertEquals(2, savedFiles(Importer.PREVIOUS_FILES_PREFIX).size)
        // One Lineage Builder run, one saved file for its assets and one for its lineage
        assertEquals(2, savedFiles("csa-lineage-builder").size)
    }
}
