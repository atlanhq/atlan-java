/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test management of Atlan tag definitions.
 */
class TagManagementTest : PackageTest("tm") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val c1Type = AtlanConnectorType.BIGID
    private val t1 = makeUnique("t1")
    private val t2 = makeUnique("t2")

    private lateinit var connection: Connection

    private val assetsFile = "assets.csv"
    private val tagsFile = "tags.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            assetsFile,
            tagsFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare copies of the files with unique names for objects
        val tagsIn = Paths.get("src", "test", "resources", tagsFile).toFile()
        val tagsOut = Paths.get(testDirectory, tagsFile).toFile()
        tagsIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{TAG1}}", t1)
                        .replace("{{TAG2}}", t2)
                        .replace("{{CONNECTION}}", c1)
                        .replace("{{CTYPE}}", c1Type.value)
                tagsOut.appendText("$revised\n")
            }
        }
        val assetsIn = Paths.get("src", "test", "resources", assetsFile).toFile()
        val assetsOut = Paths.get(testDirectory, assetsFile).toFile()
        assetsIn.useLines { lines ->
            lines.forEachIndexed { idx, line ->
                if (idx == 0) {
                    assetsOut.appendText("$line,atlanTags\n")
                } else {
                    val revised =
                        line
                            .replace("{{CONNECTION}}", connection.qualifiedName)
                            .replace("{{CTYPE}}", c1Type.value)
                    if (revised.contains("Table")) {
                        assetsOut.appendText("$revised,$t2\n")
                    } else if (revised.contains("View")) {
                        assetsOut.appendText("$revised,$t1 {{${c1Type.value}/$c1@@$t1??=Y}}\n")
                    } else {
                        assetsOut.appendText("$revised,\n")
                    }
                }
            }
        }
    }

    private fun modifyFiles() {
        // Prepare copies of the files with unique names for objects
        val assetsIn = Paths.get("src", "test", "resources", assetsFile).toFile()
        val assetsOut = Paths.get(testDirectory, revisedFile).toFile()
        assetsIn.useLines { lines ->
            lines.forEachIndexed { idx, line ->
                if (idx == 0) {
                    assetsOut.appendText("$line,atlanTags\n")
                } else {
                    val revised =
                        line
                            .replace("{{CONNECTION}}", connection.qualifiedName)
                            .replace("{{CTYPE}}", c1Type.value)
                    if (revised.contains("View")) {
                        assetsOut.appendText("$revised,++$t1 {{${c1Type.value}/$c1@@$t1??=Z}}\n")
                    } else {
                        assetsOut.appendText("$revised,\n")
                    }
                }
            }
        }
    }

    private fun createConnection(): Connection {
        val conn = Connection.creator(client, c1, c1Type).build()
        val response = conn.save(client).block()
        return response.getResult(conn)
    }

    override fun setup() {
        connection = createConnection()
        prepFiles()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, assetsFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                tagsFile = Paths.get(testDirectory, tagsFile).toString(),
                tagsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
        removeTag(t1)
        removeTag(t2)
    }

    private val tableAttrs: List<AtlanField> =
        listOf(
            Table.NAME,
            Table.ATLAN_TAGS,
        )

    @Test(groups = ["aim.tm.create"])
    fun tagOnTable() {
        validateTable()
    }

    @Test(groups = ["aim.tm.create"])
    fun tagOnView() {
        validateView("Y")
    }

    @Test(groups = ["aim.tm.create"])
    fun tableFindableByTag() {
        findTable()
    }

    @Test(groups = ["aim.tm.create"])
    fun viewFindableByTagValue() {
        findView("Y")
    }

    @Test(groups = ["aim.tm.runUpdate"], dependsOnGroups = ["aim.tm.create"])
    fun upsertRevisions() {
        modifyFiles()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "update",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
                assetsDeltaReloadCalculation = "changes",
                assetsPreviousFileDirect = Paths.get(testDirectory, assetsFile).toString(),
                assetsTagHandling = "APPEND",
            ),
            Importer::main,
        )
    }

    @Test(groups = ["aim.tm.update"], dependsOnGroups = ["aim.tm.runUpdate"])
    fun tagStillOnTable() {
        validateTable()
    }

    @Test(groups = ["aim.tm.update"], dependsOnGroups = ["aim.tm.runUpdate"])
    fun tagUpdatedOnView() {
        validateView("Z")
    }

    @Test(groups = ["aim.tm.update"], dependsOnGroups = ["aim.tm.runUpdate"])
    fun tableStillFindableByTag() {
        findTable()
    }

    @Test(groups = ["aim.tm.update"], dependsOnGroups = ["aim.tm.runUpdate"])
    fun viewFindableByUpdatedTagValue() {
        findView("Z")
    }

    private fun validateTable() {
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val tbl = found[0] as Table
        assertNotNull(tbl.atlanTags)
        assertEquals(1, tbl.atlanTags.size)
        assertEquals(t2, tbl.atlanTags.first().typeName)
    }

    private fun findTable() {
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .tagged(listOf(t2))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        assertTrue(found.first() is Table)
    }

    private fun validateView(tagValue: String) {
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val view = found[0] as View
        assertNotNull(view.atlanTags)
        assertEquals(1, view.atlanTags.size)
        val tag = view.atlanTags.first()
        assertEquals(t1, tag.typeName)
        assertNotNull(tag.sourceTagAttachments)
        assertEquals(1, tag.sourceTagAttachments.size)
        val sta = tag.sourceTagAttachments.first()
        assertNotNull(sta)
        assertEquals(t1, sta.sourceTagName)
        assertEquals(c1Type.value, sta.sourceTagConnectorName)
        assertEquals("${connection.qualifiedName}/$t1", sta.sourceTagQualifiedName)
        assertNotNull(sta.sourceTagValues)
        assertEquals(1, sta.sourceTagValues.size)
        assertEquals(tagValue, sta.sourceTagValues.first().tagAttachmentValue)
        assertNull(sta.sourceTagValues.first().tagAttachmentKey)
    }

    private fun findView(tagValue: String) {
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .taggedWithValue(t1, tagValue, true)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        assertTrue(found.first() is View)
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
