/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test

/**
 * Test export of all assets and glossaries.
 */
class ExportAllAssetsTest : PackageTest() {

    // TODO: allow configuration to specify the output directory
    //  (default to tmp/ if not specified), so that we can generate
    //  output for different tests to different sub-directories, without
    //  any overlaps / conflicts

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
        "glossary-export.csv",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            AssetExportBasicCfg(
                exportScope = "ALL",
                qnPrefix = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)?.qualifiedName,
                includeGlossaries = true,
            ),
        )
        Exporter.main(arrayOf())
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files, "tmp")
    }

    @Test
    fun errorFreeLog() {
        errorFreeLog("debug.log", "tmp")
    }

    @AfterClass(alwaysRun = true)
    fun afterClass() {
        removeFiles(files, "tmp")
        teardown()
    }
}
