/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import AssetImportCfg
import RelationalAssetsBuilderCfg
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.Importer.PREVIOUS_FILES_PREFIX
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Paths
import kotlin.IllegalStateException
import kotlin.test.assertFailsWith

/**
 * Test creation of relational assets where one of the columns has an invalid parent.
 */
class InvalidParentTest : PackageTest("ip") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.FIREBOLT

    private val testFile = "input.csv"

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "invalid_parent.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{CONNECTION}", conn1)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
            ),
            Importer::main,
        )
        assertFailsWith(IllegalStateException::class, "Could not find any table/view at: $conn1/azure-cosmos-db/cosmosdb/xyz/schemaMismatch") {
            runCustomPackage(
                AssetImportCfg(
                    assetsFile = "$testDirectory${File.separator}current-file-transformed.csv",
                    assetsUpsertSemantic = "upsert",
                    assetsPreviousFilePrefix = PREVIOUS_FILES_PREFIX,
                ),
                com.atlan.pkg.aim.Importer::main,
            )
        }
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test
    fun placeholder() {
        // do nothing -- only test is that the import fails as part of BeforeClass
    }
}
