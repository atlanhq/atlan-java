/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import kotlin.test.Test

/**
 * Test export of only assets, no glossaries.
 */
class NoGlossariesTest : PackageTest("ng") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val files =
        listOf(
            "asset-export.csv",
            "debug.log",
            "glossary-export.csv",
        )

    override fun setup() {
        runCustomPackage(
            AssetExportBasicCfg(
                exportScope = "ALL",
                qnPrefix = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)?.get(0)?.qualifiedName!!,
                includeGlossaries = false,
            ),
            Exporter::main,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files.subList(0, files.size - 1))
        validateFileExistsButEmpty(files.subList(2, files.size))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
