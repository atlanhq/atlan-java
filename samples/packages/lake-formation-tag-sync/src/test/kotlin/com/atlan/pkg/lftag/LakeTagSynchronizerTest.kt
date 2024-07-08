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
import com.atlan.pkg.lftag.LakeTagSynchronizer.CONNECTION_MAP_JSON
import com.atlan.pkg.lftag.LakeTagSynchronizer.METADATA_MAP_JSON
import com.atlan.pkg.lftag.LakeTagSynchronizer.TAG_FILE_NAME_PREFIX
import com.atlan.util.AssetBatch
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.copyTo
import kotlin.test.assertEquals

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
    private val databaseName = "db_test"
    private val attr1 = "Security Classification"
    private val attr2 = "Privacy Sensitivity"
    private val attr3 = "Data Load Method"
    private val directoryPrefix = "stuff"

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
        val db = Database.creator(databaseName, connection1.qualifiedName).build()
        batch.add(db)
        val sch = Schema.creator("sch", db).build()
        batch.add(sch)
        val tbl = Table.creator("tbl1", sch).build()
        batch.add(tbl)
        val column = Column.creator("col1", tbl, 1).build()
        batch.add(column)
        val response = batch.flush()
        tableGuid = response.getResult(tbl).guid
        columnGuid = response.getResult(column).guid
    }

    private fun createConnectionMap() {
        val connectionMap = mapOf("dev" to "$connectionQualifiedName/$databaseName")
        Paths.get(testDirectory, directoryPrefix, CONNECTION_MAP_JSON).toFile()
            .appendText(mapper.writeValueAsString(connectionMap))
    }

    private fun copyTagFile() {
        Paths.get("src", "test", "resources", "${TAG_FILE_NAME_PREFIX}_1.json")
            .copyTo(Paths.get(testDirectory, directoryPrefix, "${TAG_FILE_NAME_PREFIX}_1.json"))
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
        Paths.get(testDirectory, directoryPrefix, METADATA_MAP_JSON).toFile()
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
        setup(
            LakeFormationTagSyncCfg(
                "DIRECT",
                "s3",
                directoryPrefix,
            ),
        )
        LakeTagSynchronizer.main(arrayOf(testDirectory))
        Thread.sleep(15000)
    }

    override fun teardown() {
        val client = Atlan.getDefaultClient()
        removeConnection(c1, connectorType)
        client.typeDefs.purge(client.customMetadataCache.getIdForName(cm1))
        EnumDef.purge(enum1)
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
        assertEquals("col1", column.name)
        val attribute1 = column.getCustomMetadata(cm1, attr1)
        assertEquals("public", attribute1)
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
