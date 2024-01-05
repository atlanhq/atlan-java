/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption

import AdoptionExportCfg
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.exports.AssetViews
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KotlinLogging
import java.io.File

/**
 * Actually run the export of adoption details.
 */
object AdoptionExporter {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<AdoptionExportCfg>()

        val includeViews = Utils.getOrDefault(config.includeViews, "BY_VIEWS")
        val maxAssets = Utils.getOrDefault(config.maximumAssets, 100)
        val maxSearches = Utils.getOrDefault(config.maximumSearches, 50)

        val exportFile = "$outputDirectory${File.separator}adoption-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            if (includeViews != "NONE") {
                AssetViews(xlsx, logger, includeViews, maxAssets.toInt()).export()
            }
            if (config.includeSearches == true) {
                TODO("Export searches")
            }
            if (config.includeChanges == true) {
                TODO("Export asset changes")
            }
        }

        val emails = Utils.getOrDefault(config.emailAddresses, "")
            .split(',')
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()
        if (emails.isNotEmpty()) {
            Utils.sendEmail(
                "[Atlan] Adoption Export results",
                emails,
                "Hi there! As requested, please find attached the results of the Adoption Export package.\n\nAll the best!\nAtlan",
                listOf(File(exportFile)),
            )
        }
    }
}
