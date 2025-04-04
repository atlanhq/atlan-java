/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import LakeFormationTagSyncCfg
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
import com.atlan.pkg.lftag.LakeTagSynchronizer.CONNECTION_MAP_JSON
import com.atlan.pkg.lftag.LakeTagSynchronizer.METADATA_MAP_JSON
import com.atlan.pkg.lftag.LakeTagSynchronizer.TAG_FILE_NAME_PREFIX
import com.atlan.util.AssetBatch
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.copyTo
import kotlin.test.assertEquals

private const val PUBLIC = "public"

private const val NON_PI = "non-pi"

private const val FULL_HISTORY = "fullhistory"

class LakeTagSynchronizerTest : PackageTest("lts") {
    override val logger = Utils.getLogger(this.javaClass.name)
    private val c1 = makeUnique("c1")
    private var connectionQualifiedName = ""
    private var schemaGuid = ""
    private var tableGuid = ""
    private var columnGuid = ""
    private val cm1 = makeUnique("cm")

    private val enum1 = makeUnique("enum")
    private val mapper = jacksonObjectMapper()

    private val connectorType = AtlanConnectorType.MINISQL
    private val databaseName = "db_test"
    private val attr1 = "Security Classification"
    private val attr2 = "Privacy Sensitivity"
    private val attr3 = "Data Load Method"
    private val directoryPrefix = LakeTagSynchronizer.OUTPUT_SUBDIR

    private fun createConnections() {
        Connection
            .creator(client, c1, connectorType)
            .build()
            .save(client)
            .block()
    }

    private fun createAssets() {
        val connection1 = Connection.findByName(client, c1, connectorType)[0]!!
        connectionQualifiedName = connection1.qualifiedName
        AssetBatch(client, 20).use { batch ->
            val db = Database.creator(databaseName, connection1.qualifiedName).build()
            batch.add(db)
            val sch = Schema.creator("sch", db).build()
            batch.add(sch)
            val tbl = Table.creator("tbl1", sch).build()
            batch.add(tbl)
            val column = Column.creator("col1", tbl, 1).build()
            batch.add(column)
            val response = batch.flush()
            schemaGuid = response.getResult(sch).guid
            tableGuid = response.getResult(tbl).guid
            columnGuid = response.getResult(column).guid
        }
    }

    private fun createConnectionMap() {
        val connectionMap = mapOf("dev" to "$connectionQualifiedName/$databaseName")
        Paths
            .get(testDirectory, directoryPrefix, CONNECTION_MAP_JSON)
            .toFile()
            .appendText(mapper.writeValueAsString(connectionMap))
    }

    private fun copyTagFile() {
        Paths
            .get("src", "test", "resources", "${TAG_FILE_NAME_PREFIX}_1.json")
            .copyTo(Paths.get(testDirectory, directoryPrefix, "${TAG_FILE_NAME_PREFIX}_1.json"))
    }

    private fun createEnums() {
        val enumDef =
            EnumDef
                .creator(
                    enum1,
                    listOf(PUBLIC, NON_PI, FULL_HISTORY),
                ).build()
        enumDef.create(client)
    }

    private fun createCustomMetadata() {
        createEnums()
        CustomMetadataDef
            .creator(cm1)
            .attributeDef(AttributeDef.of(client, attr1, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(client, attr2, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(client, attr3, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .build()
            .create(client)
    }

    private fun createMetadataMap() {
        val metaDataMap =
            mapOf(
                "security_classification" to "$cm1::$attr1",
                "privacy_sensitivity" to "$cm1::$attr2",
                "data_load_method" to "$cm1::$attr3",
            )
        Paths
            .get(testDirectory, directoryPrefix, METADATA_MAP_JSON)
            .toFile()
            .appendText(mapper.writeValueAsString(metaDataMap))
    }

    override fun setup() {
        createConnections()
        createAssets()
        Paths.get(testDirectory, directoryPrefix).toFile().mkdirs()
        createConnectionMap()
        createCustomMetadata()
        createMetadataMap()
        copyTagFile()
        runCustomPackage(
            LakeFormationTagSyncCfg(
                "DIRECT",
                "s3",
                true,
            ),
            LakeTagSynchronizer::main,
        )
        Thread.sleep(15000)
    }

    override fun teardown() {
        removeConnection(c1, connectorType)
        client.typeDefs.purge(client.customMetadataCache.getSidForName(cm1))
        EnumDef.purge(client, enum1)
    }

    @Test
    fun validateSchemaTagged() {
        val schema = Schema.get(client, schemaGuid)
        val attribute1 = schema.getCustomMetadata(cm1, attr1)
        assertEquals(PUBLIC, attribute1)
        val attribute3 = schema.getCustomMetadata(cm1, attr2)
        assertEquals(NON_PI, attribute3)
    }

    @Test
    fun validateTableTagged() {
        val table = Table.get(client, tableGuid)
        val attribute1 = table.getCustomMetadata(cm1, attr1)
        assertEquals(PUBLIC, attribute1)
        val attribute3 = table.getCustomMetadata(cm1, attr3)
        assertEquals(FULL_HISTORY, attribute3)
    }

    @Test
    fun validateColumnTagged() {
        val column = Column.get(client, columnGuid)
        assertEquals("col1", column.name)
        val attribute1 = column.getCustomMetadata(cm1, attr1)
        assertEquals(PUBLIC, attribute1)
    }

    @Test
    fun validateFilesCreated() {
        validateFilesExist(
            listOf(
                "debug.log",
                "$directoryPrefix${File.separator}$CONNECTION_MAP_JSON",
                "$directoryPrefix${File.separator}$METADATA_MAP_JSON",
                "$directoryPrefix${File.separator}${TAG_FILE_NAME_PREFIX}_1.json",
                "lftag_association_1.csv",
            ),
        )
    }
}
