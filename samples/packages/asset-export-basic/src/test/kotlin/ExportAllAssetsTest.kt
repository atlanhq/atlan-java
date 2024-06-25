/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import kotlin.test.Test

/**
 * Test export of all assets and glossaries.
 */
class ExportAllAssetsTest : PackageTest() {

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
        "glossary-export.csv",
    )

    override fun setup() {
        setup(
            AssetExportBasicCfg(
                exportScope = "ALL",
                qnPrefix = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)?.qualifiedName,
                includeGlossaries = true,
            ),
        )
        Exporter.main(arrayOf(testDirectory))
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
