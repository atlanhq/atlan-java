/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.FileBasedDelta
import org.testng.Assert.assertTrue
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test pre-processing of full-load CSV files to detect which assets should be removed.
 */
class DeltaTest : PackageTest("aid") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.ICEBERG
    private val conn1QN = "default/${conn1Type.value}/1234567890"

    private val previousFile = "assets.csv"
    private val currentFile = "assets_latest.csv"
    private var delta: FileBasedDelta? = null

    private val files =
        listOf(
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

    private fun replaceVars(
        input: File,
        output: File,
    ) {
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", conn1QN)
                        .replace("{{CTYPE}}", conn1Type.value)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        val connectionsMap =
            mapOf(
                AssetResolver.ConnectionIdentity(conn1, conn1Type.value) to conn1QN,
            )
        delta = FileBasedDelta(connectionsMap, AssetImporter, Utils.getLogger(this.javaClass.name), compareChecksums = true)
        delta!!.calculateDelta(
            Paths.get(testDirectory, currentFile).toString(),
            Paths.get(testDirectory, previousFile).toString(),
        )
    }

    @Test
    fun hasSomethingToDelete() {
        assertTrue(delta!!.hasAnythingToDelete())
    }

    @Test
    fun totalAssetsToDelete() {
        assertEquals(1, delta!!.assetsToDelete.size)
        val types =
            delta!!
                .assetsToDelete
                .entrySet()
                .map { it.key.typeName }
                .toList()
                .toSet()
        assertEquals(1, types.size)
        assertTrue(types.contains(View.TYPE_NAME))
    }

    @Test
    fun specificAssetsToDelete() {
        delta!!.assetsToDelete.entrySet().forEach {
            when (it.key.typeName) {
                View.TYPE_NAME -> assertTrue("$conn1QN/DB/SCH/VIEW" == it.key.qualifiedName)
            }
        }
    }

    @Test
    fun totalAssetsToReload() {
        assertEquals(2, delta!!.assetsToReload.size)
        val types =
            delta!!
                .assetsToReload
                .entrySet()
                .map { it.key.typeName }
                .toList()
                .toSet()
        assertEquals(2, types.size)
        assertTrue(types.contains(Schema.TYPE_NAME))
        assertTrue(types.contains(MaterializedView.TYPE_NAME))
    }

    @Test
    fun specificAssetsToReload() {
        delta!!.assetsToReload.entrySet().forEach {
            when (it.key.typeName) {
                Schema.TYPE_NAME -> assertEquals("$conn1QN/DB/SCH", it.key.qualifiedName)
                MaterializedView.TYPE_NAME -> assertEquals("$conn1QN/DB/SCH/MVIEW", it.key.qualifiedName)
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
}
