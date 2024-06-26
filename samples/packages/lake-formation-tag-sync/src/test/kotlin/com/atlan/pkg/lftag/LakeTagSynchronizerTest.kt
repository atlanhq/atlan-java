/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import LakeFormationTagSyncCfg
import com.atlan.Atlan
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.model.typedefs.EnumDef
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.Files
import mu.KotlinLogging
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals

private const val DATABASE_NAME = "db_test"

private const val attr1 = "Security Classification"

private const val attr2 = "Privacy Sensitivity"

private const val attr3 = "Data Load Method"

private const val CONNECTION_MAP_JSON = "connection_map.json"

private const val PREFIX = "stuff"

private const val METADATA_MAP_JSON = "metadata_map.json"

private const val S3_BUCKET = "lakestoragetag"

class LakeTagSynchronizerTest : PackageTest() {
    override val logger = KotlinLogging.logger {}
    private val c1 = makeUnique("lftagdb")
    private var connectionQualifiedName = ""
    private var tableGuid = ""
    private var columnGuid = ""
    private val cm1 = makeUnique("lftcm")
    private val enum1 = makeUnique("lfenum")
    private val mapper = jacksonObjectMapper()
    private val connectorType = AtlanConnectorType.MINISQL

    private fun createConnections() {
        Connection.creator(c1, connectorType)
            .build()
            .save()
            .block()
    }

    private fun createAssets() {
        val client = Atlan.getDefaultClient()
        val connection1 = Connection.findByName(c1, connectorType)[0]!!
        connectionQualifiedName = connection1.qualifiedName
        val batch = AssetBatch(client, 20)
        val db = Database.creator(DATABASE_NAME, connection1.qualifiedName).build()
        batch.add(db)
        val sch = Schema.creator("sch", db).build()
        batch.add(sch)
        val tbl = Table.creator("tbl1", sch).build()
        batch.add(tbl)
        val column = Column.creator("col1", tbl, 1).build()
        batch.add(column)
        val response = batch.flush()
        response.getCreatedAssets(Table::class.java).forEach { table ->
            this.tableGuid = table.guid
        }
        response.getCreatedAssets(Column::class.java).forEach { col ->
            this.columnGuid = col.guid
        }
    }

    private fun createConnectionMap() {
        val connectionMap = mapOf("dev" to "$connectionQualifiedName/$DATABASE_NAME")
        File("$testDirectory/$CONNECTION_MAP_JSON").bufferedWriter().use { out ->
            out.write(mapper.writeValueAsString(connectionMap))
        }
    }

    private fun uploadFiles() {
        Utils.uploadOutputFile("$testDirectory/$CONNECTION_MAP_JSON", PREFIX, CONNECTION_MAP_JSON)
        File("$testDirectory/$CONNECTION_MAP_JSON").delete()
        Utils.uploadOutputFile("$testDirectory/$METADATA_MAP_JSON", PREFIX, METADATA_MAP_JSON)
        File("$testDirectory/$METADATA_MAP_JSON").delete()
        Utils.uploadOutputFile("./src/test/resources/lftag_association_1.json", PREFIX, "lftag_association_1.json")
    }

    private fun createEnums() {
        val enumDef = EnumDef.creator(
            enum1,
            listOf("public", "non-pi", "fullhistory"),
        )
            .build()
        enumDef.create()
    }
    private fun createCustomMetadata() {
        createEnums()
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of(attr1, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(attr2, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(attr3, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .build()
            .create()
    }

    private fun createMetadataMap() {
        val metaDataMap = mapOf(
            "security_classification" to "$cm1::$attr1",
            "privacy_sensitivity" to "$cm1::$attr2",
            "data_load_method" to "$cm1::$attr3",
        )
        File("$testDirectory/$METADATA_MAP_JSON").bufferedWriter().use { out ->
            out.write(mapper.writeValueAsString(metaDataMap))
        }
    }

    private fun createCredentials() {
        val credentials = mapOf(
            "authType" to "s3",
            "host" to "",
            "port" to 0,
            "username" to System.getenv("AWS_ACCESS_KEY_ID"),
            "password" to System.getenv("AWS_SECRET_ACCESS_KEY"),
            "extra" to mapOf(
                "region" to System.getenv("AWS_DEFAULT_REGION"),
                "s3_bucket" to S3_BUCKET,
            ),
            "connector" to "",
            "connectorConfigName" to "",
            "connectorType" to "",
            "description" to "",
            "connection" to "",
            "id" to "",
            "name" to "",
            "isActive" to true,
            "level" to "",
            "metadata" to "",
            "tenantId" to "",
            "createdBy" to "",
            "createdAt" to 0,
            "updatedAt" to 0,
            "version" to "",
        )
        val credentialsFile = File("/tmp/credentials/success/result-0.json")
        Files.createParentDirs(credentialsFile)
        credentialsFile.bufferedWriter().use { out ->
            out.write(mapper.writeValueAsString(credentials))
        }
    }

    override fun setup() {
        createCredentials()
        createConnections()
        createAssets()
        createConnectionMap()
        createCustomMetadata()
        createMetadataMap()
        uploadFiles()
        setup(
            LakeFormationTagSyncCfg(
                "CLOUD",
                "s3",
                PREFIX,
                null,
                null,
                null,
            ),
        )
        LakeTagSynchronizer.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    @Test
    fun validateTableTagged() {
        val table = Table.get(tableGuid)
        val attribute1 = table.getCustomMetadata(cm1, attr1)
        assertEquals("public", attribute1)
        val attribute3 = table.getCustomMetadata(cm1, attr3)
        assertEquals("fullhistory", attribute3)
    }

    @Test
    fun validateColumnTagged() {
        val column = Column.get(columnGuid)
        val attribute1 = column.getCustomMetadata(cm1, attr1)
        assertEquals("public", attribute1)
    }

    @Test
    fun validateFilesCreated() {
        validateFilesExist(
            listOf(
                "debug.log",
                CONNECTION_MAP_JSON,
                METADATA_MAP_JSON,
                "lftag_association_1.json",
                "lftag_association_1.csv",
            ),
        )
    }

    override fun teardown() {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, AtlanConnectorType.REDSHIFT)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
        EnumDef.purge(enum1)
    }
}
