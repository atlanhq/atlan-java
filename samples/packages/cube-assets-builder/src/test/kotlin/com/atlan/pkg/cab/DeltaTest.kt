/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.util.FileBasedDelta
import org.testng.Assert.assertTrue
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test pre-processing of full-load CSV files to detect which assets should be removed.
 */
class DeltaTest : PackageTest("cd") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.ESSBASE
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
                        .replace("{{CONNECTION1}}", conn1)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
        val ctx =
            PackageContext(
                config = CubeAssetsBuilderCfg(),
                client = client,
                reusedClient = true,
            )
        ctx.connectionCache.inject(conn1, conn1Type.value, conn1QN)
        delta = FileBasedDelta(ctx, AssetImporter.Companion, Utils.getLogger(this.javaClass.name), compareChecksums = true)
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
        assertEquals(3, delta!!.assetsToDelete.size)
        val types =
            delta!!
                .assetsToDelete
                .entrySet()
                .map { it.key.typeName }
                .toList()
                .toSet()
        assertEquals(2, types.size)
        assertTrue(types.contains(CubeHierarchy.TYPE_NAME))
        assertTrue(types.contains(CubeField.TYPE_NAME))
    }

    @Test
    fun specificAssetsToDelete() {
        delta!!.assetsToDelete.entrySet().forEach {
            when (it.key.typeName) {
                CubeHierarchy.TYPE_NAME -> {
                    assertEquals("$conn1QN/TEST_CUBE/TEST_DIM/TEST_HIERARCHY2", it.key.qualifiedName)
                }

                CubeField.TYPE_NAME -> {
                    assertTrue(
                        "$conn1QN/TEST_CUBE/TEST_DIM/TEST_HIERARCHY2/COL4" == it.key.qualifiedName ||
                            "$conn1QN/TEST_CUBE/TEST_DIM/TEST_HIERARCHY2/COL4/COL5" == it.key.qualifiedName,
                    )
                }
            }
        }
    }

    @Test
    fun totalAssetsToReload() {
        assertEquals(3, delta!!.assetsToReload.size)
        val types =
            delta!!
                .assetsToReload
                .entrySet()
                .map { it.key.typeName }
                .toList()
                .toSet()
        assertEquals(3, types.size)
        assertTrue(types.contains(Connection.TYPE_NAME))
        assertTrue(types.contains(CubeDimension.TYPE_NAME))
        assertTrue(types.contains(CubeField.TYPE_NAME))
    }

    @Test
    fun specificAssetsToReload() {
        delta!!.assetsToReload.entrySet().forEach {
            when (it.key.typeName) {
                Connection.TYPE_NAME -> assertEquals(conn1QN, it.key.qualifiedName)
                CubeDimension.TYPE_NAME -> assertEquals("$conn1QN/TEST_CUBE/TEST_DIM", it.key.qualifiedName)
                CubeField.TYPE_NAME -> assertEquals("$conn1QN/TEST_CUBE/TEST_DIM/TEST_HIERARCHY1/COL1", it.key.qualifiedName)
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
