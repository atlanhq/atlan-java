/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Column
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test pre-processing of full-load CSV files to detect which assets should be removed.
 */
class AssetRemoverTest : PackageTest() {
    private val conn1 = makeUnique("ar")
    private val conn1Type = AtlanConnectorType.MPARTICLE
    private val conn1QN = "default/${conn1Type.value}/1234567890"

    private val previousFile = "assets.csv"
    private val currentFile = "assets_latest.csv"
    private var remover: AssetRemover? = null

    private val files = listOf(
        previousFile,
        currentFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val previousIn = Paths.get("src", "test", "resources", previousFile).toFile()
        val previousOut = Paths.get(testDirectory, previousFile).toFile()
        replaceVars(previousIn, previousOut)
        val latestIn = Paths.get("src", "test", "resources", currentFile).toFile()
        val latestOut = Paths.get(testDirectory, currentFile).toFile()
        replaceVars(latestIn, latestOut)
    }

    private fun replaceVars(input: File, output: File) {
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{{CONNECTION1}}", conn1)
                output.appendText("$revised\n")
            }
        }
    }

    @BeforeClass
    fun beforeClass() {
        prepFile()
        val connectionsMap = mapOf(
            AssetImporter.ConnectionIdentity(conn1, conn1Type.value) to conn1QN,
        )
        remover = AssetRemover(connectionsMap, KotlinLogging.logger {})
        remover!!.calculateDeletions(
            Paths.get(testDirectory, currentFile).toString(),
            Paths.get(testDirectory, previousFile).toString(),
        )
    }

    @Test
    fun hasSomethingToDelete() {
        assertTrue(remover!!.hasAnythingToDelete())
    }

    @Test
    fun totalAssetsToDelete() {
        assertEquals(3, remover!!.assetsToDelete.size)
        val types = remover!!.assetsToDelete.map { it.typeName }.toSet()
        assertEquals(2, types.size)
        assertTrue(types.contains(View.TYPE_NAME))
        assertTrue(types.contains(Column.TYPE_NAME))
    }

    @Test
    fun specificAssetsToDelete() {
        remover!!.assetsToDelete.forEach {
            when (it.typeName) {
                View.TYPE_NAME -> assertTrue("$conn1QN/TEST_DB/TEST_SCHEMA/TEST_VIEW" == it.qualifiedName)
                Column.TYPE_NAME -> {
                    assertTrue(
                        "$conn1QN/TEST_DB/TEST_SCHEMA/TEST_VIEW/COL3" == it.qualifiedName ||
                            "$conn1QN/TEST_DB/TEST_SCHEMA/TEST_VIEW/COL4" == it.qualifiedName,
                    )
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
        teardown(context.failedTests.size() > 0)
    }
}
