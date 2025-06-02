/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.annotations.Test

/**
 * Test run of asset import.
 */
class OneOffTest : PackageTest("oo") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val downloads = "/Users/christopher/Downloads/samples/"
    private val glossariesFile = "$downloads/JPMC Compass Samples - glossaries.csv"
    private val assetsFile = "$downloads/JPMC Compass Samples - assets.csv"
    private val productsFile = "$downloads/JPMC Compass Samples - products.csv"
    private val tagsFile = "$downloads/JPMC Compass Samples - tags.csv"

    override fun setup() {
        runCustomPackage(
            AssetImportCfg(
                glossariesFile = glossariesFile,
                glossariesUpsertSemantic = "upsert",
                assetsFile = assetsFile,
                assetsUpsertSemantic = "upsert",
                dataProductsFile = productsFile,
                dataProductsUpsertSemantic = "upsert",
                tagsFile = tagsFile,
            ),
            Importer::main,
        )
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
