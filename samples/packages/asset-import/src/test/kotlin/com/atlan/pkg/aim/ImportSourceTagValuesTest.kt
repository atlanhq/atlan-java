/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Test import of source tag values.
 */
class ImportSourceTagValuesTest : PackageTest() {

    private val table = makeUnique("istv")

    private val testFile = "source_tag_values.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{{CONNECTION}}", connectionQN)
                    .replace("{{TABLE}}", table)
                output.appendText("$revised\n")
            }
        }
    }

    @BeforeClass
    fun beforeClass() {
        val snowflakeConnection = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        prepFile(snowflakeConnection.qualifiedName)
        setup(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
        )
        Importer.main(arrayOf(testDirectory))
    }

    @Test
    fun tableWithTagValues() {
        val snowflakeConnection = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        val snowflakeQN = snowflakeConnection.qualifiedName
        val dbtQN = Connection.findByName("development", AtlanConnectorType.DBT)?.get(0)?.qualifiedName!!
        val request = Table.select()
            .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeQN))
            .where(Table.NAME.eq(table))
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val result = response.assets.getOrNull(0)
        assertNotNull(result)
        assertTrue(result is Table)
        val table = result as Table
        val tags = table.atlanTags
        assertEquals(1, tags.size)
        tags.forEach { tag ->
            assertFalse(tag.propagate)
            val attachments = tag.sourceTagAttachments
            assertFalse(attachments.isNullOrEmpty())
            assertEquals(1, attachments.size)
            assertNotNull(attachments[0].sourceTagGuid)
            when (tag.typeName) {
                "Mart" -> {
                    assertEquals("mart", attachments[0].sourceTagName)
                    assertEquals("$dbtQN/account/24670/project/211208/tag/mart", attachments[0].sourceTagQualifiedName)
                    assertTrue(attachments[0].sourceTagValues.isNullOrEmpty())
                }
                "Confidential" -> {
                    assertEquals("CONFIDENTIAL", attachments[0].sourceTagName)
                    assertEquals("$snowflakeQN/ANALYTICS/WIDE_WORLD_IMPORTERS/CONFIDENTIAL", attachments[0].sourceTagQualifiedName)
                    assertEquals(1, attachments[0].sourceTagValues.size)
                    assertTrue(attachments[0].sourceTagValues[0].tagAttachmentKey.isNullOrBlank())
                    assertEquals("Not Restricted", attachments[0].sourceTagValues[0].tagAttachmentValue)
                }
                "Hourly" -> {
                    assertEquals("hourly", attachments[0].sourceTagName)
                    assertEquals("$dbtQN/account/24670/project/211208/tag/hourly", attachments[0].sourceTagQualifiedName)
                    assertTrue(attachments[0].sourceTagValues.isNullOrEmpty())
                }
                "Wide_world_importers" -> {
                    assertEquals("wide_world_importers", attachments[0].sourceTagName)
                    assertEquals("$dbtQN/account/24670/project/211208/tag/wide_world_importers", attachments[0].sourceTagQualifiedName)
                    assertTrue(attachments[0].sourceTagValues.isNullOrEmpty())
                }
                else -> {
                    throw AssertionError("Unexpected tag: ${tag.typeName}")
                }
            }
        }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        val snowflakeConnection = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        Table.select()
            .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeConnection.qualifiedName))
            .where(Table.NAME.eq(table))
            .stream()
            .findFirst()
            .ifPresent {
                Table.purge(it.guid)
            }
        teardown(context.failedTests.size() > 0)
    }
}
