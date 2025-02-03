/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.core.CustomMetadataAttributes
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.fields.CustomMetadataField
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.cell.TimestampXformer
import com.atlan.util.AssetBatch
import java.io.File
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test migration of asset metadata.
 */
class EnrichmentMigratorSingleTargetTest : PackageTest("st") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val c2 = makeUnique("c2")
    private val c1Type = AtlanConnectorType.GAINSIGHT
    private val c2Type = AtlanConnectorType.GRAPHQL
    private val cm1 = makeUnique("cm")
    private val now = Instant.now().toEpochMilli()

    private val files =
        listOf(
            "asset-export.csv",
            "transformed-file.csv",
            "debug.log",
        )

    private fun createConnections() {
        AssetBatch(client, 5).use { batch ->
            batch.add(Connection.creator(client, c1, c1Type).build())
            batch.add(Connection.creator(client, c2, c2Type).build())
            batch.flush()
        }
    }

    private fun createCustomMetadata() {
        CustomMetadataDef
            .creator(cm1)
            .attributeDef(AttributeDef.of(client, "dateSingle", AtlanCustomAttributePrimitiveType.DATE, false))
            .build()
            .create(client)
    }

    private fun createAssets() {
        val connection1 = Connection.findByName(client, c1, c1Type)[0]!!
        val connection2 = Connection.findByName(client, c2, c2Type)[0]!!
        AssetBatch(client, 20).use { batch ->
            val db1 = Database.creator("db1", connection1.qualifiedName).build()
            batch.add(db1)
            val db2 = Database.creator("db1", connection2.qualifiedName).build()
            batch.add(db2)
            val sch1 = Schema.creator("sch1", db1).build()
            batch.add(sch1)
            val sch2 = Schema.creator("sch1", db2).build()
            batch.add(sch2)
            val tbl1 =
                Table
                    .creator("tbl1", sch1)
                    .customMetadata(
                        cm1,
                        CustomMetadataAttributes
                            .builder()
                            .attribute("dateSingle", now)
                            .build(),
                    ).build()
            batch.add(tbl1)
            val tbl2 = Table.creator("tbl1", sch2).build()
            batch.add(tbl2)
            batch.flush()
        }
    }

    override fun setup() {
        createConnections()
        createCustomMetadata()
        createAssets()
        runCustomPackage(
            EnrichmentMigratorCfg(
                sourceConnection = listOf(Connection.findByName(client, c1, c1Type)?.get(0)?.qualifiedName!!),
                targetConnection = listOf(Connection.findByName(client, c2, c2Type)?.get(0)?.qualifiedName!!),
                failOnErrors = false,
                cmLimitType = "INCLUDE",
                customMetadata = "$cm1::dateSingle",
            ),
            EnrichmentMigrator::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}transformed-file.csv",
                assetsUpsertSemantic = "update",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeConnection(c2, c2Type)
        client.typeDefs.purge(client.customMetadataCache.getSidForName(cm1))
    }

    @Test
    fun datesOnTarget() {
        val targetConnection = Connection.findByName(client, c2, c2Type)[0]!!
        val cmField = CustomMetadataField.of(client, cm1, "dateSingle")
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(targetConnection.qualifiedName))
                .where(cmField.hasAnyValue())
                .includeOnResults(cmField)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        response
            .stream()
            .forEach {
                val cm = it.customMetadataSets
                assertNotNull(cm)
                val attrs = cm[cm1]?.attributes
                assertNotNull(attrs)
                assertEquals(1, attrs.size)
                assertEquals(now, attrs["dateSingle"])
            }
    }

    @Test
    fun customMetadataInFile() {
        val targetConnection = Connection.findByName(client, c2, c2Type)[0]!!
        fileHasLineStartingWith(
            filename = "transformed-file.csv",
            line =
                """
                "${targetConnection.qualifiedName}/db1/sch1/tbl1","Table","tbl1",,,,,,,,,,,,,,,,"${TimestampXformer.encode(now)}"
                """.trimIndent(),
        )
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
