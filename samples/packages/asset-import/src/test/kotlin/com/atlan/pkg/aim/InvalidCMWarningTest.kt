/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test import of an invalid enumerated value in custom metadata.
 */
class InvalidCMWarningTest : PackageTest("icmw") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val table = makeUnique("t1")

    private val testFile = "invalid-cm-value.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", connectionQN)
                        .replace("{{TABLE}}", table)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        prepFile(snowflakeConnection.qualifiedName)
    }

    override fun teardown() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        Table
            .select(client)
            .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeConnection.qualifiedName))
            .where(Table.NAME.eq(table))
            .stream()
            .findFirst()
            .ifPresent {
                Table.purge(client, it.guid)
            }
    }

    @Test
    fun loadsWithoutError() {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = false,
                assetsConfig = "advanced",
            ),
            Importer::main,
        )
    }

    @Test
    fun tableCreatedWithoutCM() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeConnection.qualifiedName))
                .where(Table.NAME.eq(table))
                ._includesOnResults(client.customMetadataCache.getAttributesForSearchResults("Data Quality"))
                .toRequest()
        val tablesFound = retrySearchUntil(request, 1)
        assertNotNull(tablesFound)
        assertEquals(1, tablesFound.assets.size)
        val t = tablesFound.assets[0]
        assertNotNull(t)
        assertEquals(table, t.name)
        val cmType = t.getCustomMetadata("Data Quality", "Type")
        assertNull(cmType)
    }

    @Test
    fun warningLogged() {
        logHasMessage(
            "WARN",
            """
            abc
            """.trimIndent(),
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
