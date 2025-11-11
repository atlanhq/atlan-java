/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption

import AdoptionExportCfg
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.validatePathIsSafe
import com.atlan.pkg.adoption.exports.AssetChanges
import com.atlan.pkg.adoption.exports.AssetViews
import com.atlan.pkg.adoption.exports.DetailedSearches
import com.atlan.pkg.adoption.exports.DetailedUserChanges
import com.atlan.pkg.adoption.exports.DetailedUserViews
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.xls.ExcelWriter
import java.io.File
import kotlin.math.min

/**
 * Actually run the export of adoption details.
 */
object AdoptionExporter {
    private val logger = Utils.getLogger(AdoptionExporter.javaClass.name)

    private const val FILENAME = "adoption-export.xlsx"

    private const val CHANGES_FILE = "changes.csv"
    private const val VIEWS_FILE = "views.csv"
    private const val USER_SEARCHES_FILE = "user-searches.csv"
    private const val USER_CHANGES_FILE = "user-changes.csv"
    private const val USER_VIEWS_FILE = "user-views.csv"

    @JvmStatic
    fun main(args: Array<String>) {
        val od = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AdoptionExportCfg>().use { ctx ->

            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            val outputDirectory = validatePathIsSafe(od)
            outputDirectory.toFile().mkdirs()

            // Touch every file, just so they exist, to avoid any workflow failures
            val xlsxFile = validatePathIsSafe(outputDirectory, FILENAME)
            xlsxFile.toFile().createNewFile()
            val changesFile = validatePathIsSafe(outputDirectory, CHANGES_FILE)
            changesFile.toFile().createNewFile()
            val viewsFile = validatePathIsSafe(outputDirectory, VIEWS_FILE)
            viewsFile.toFile().createNewFile()
            val userSearchesFile = validatePathIsSafe(outputDirectory, USER_SEARCHES_FILE)
            userSearchesFile.toFile().createNewFile()
            val userChangesFile = validatePathIsSafe(outputDirectory, USER_CHANGES_FILE)
            userChangesFile.toFile().createNewFile()
            val userViewsFile = validatePathIsSafe(outputDirectory, USER_VIEWS_FILE)
            userViewsFile.toFile().createNewFile()

            val fileOutputs = mutableListOf<String>()

            ExcelWriter(xlsxFile.toString()).use { xlsx ->
                if (ctx.config.includeViews != "NONE") {
                    if (xlsxOutput) {
                        AssetViews(ctx, xlsx.createSheet("Views"), logger).export()
                    } else {
                        CSVWriter(viewsFile.toString()).use { csv -> AssetViews(ctx, csv, logger).export() }
                    }
                    if (ctx.config.viewsDetails == "YES") {
                        if (xlsxOutput) {
                            DetailedUserViews(ctx, xlsx.createSheet("User views"), logger).export()
                        } else {
                            CSVWriter(userViewsFile.toString()).use { csv -> DetailedUserViews(ctx, csv, logger).export() }
                        }
                    }
                }
                if (ctx.config.includeChanges == "YES") {
                    if (xlsxOutput) {
                        AssetChanges(ctx, xlsx.createSheet("Changes"), logger).export()
                    } else {
                        CSVWriter(changesFile.toString()).use { csv -> AssetChanges(ctx, csv, logger).export() }
                    }
                    if (ctx.config.changesDetails == "YES") {
                        if (xlsxOutput) {
                            DetailedUserChanges(ctx, xlsx.createSheet("User changes"), logger).export()
                        } else {
                            CSVWriter(userChangesFile.toString()).use { csv -> DetailedUserChanges(ctx, csv, logger).export() }
                        }
                    }
                }
                if (ctx.config.includeSearches == "YES") {
                    if (xlsxOutput) {
                        DetailedSearches(ctx, xlsx.createSheet("User searches"), logger).export()
                    } else {
                        CSVWriter(userSearchesFile.toString()).use { csv -> DetailedSearches(ctx, csv, logger).export() }
                    }
                }
            }
            if (xlsxOutput) {
                fileOutputs.add(xlsxFile.toString())
            } else {
                xlsxFile.toFile().delete()
                xlsxFile.toFile().createNewFile()
            }

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        Utils.sendEmail(
                            "[Atlan] Adoption Export results",
                            emails,
                            "Hi there! As requested, please find attached the results of the Adoption Export package.\n\nAll the best!\nAtlan",
                            fileOutputs.map { File(it) },
                        )
                    }
                }

                "CLOUD" -> {
                    if (xlsxOutput) {
                        Utils.uploadOutputFile(
                            xlsxFile.toString(),
                            ctx.config.targetPrefix,
                            ctx.config.targetKey,
                        )
                    } else {
                        fileOutputs.forEach {
                            // When using CSVs, ignore any key specified and use the filename itself
                            Utils.uploadOutputFile(
                                it,
                                ctx.config.targetPrefix,
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAssetDetails(
        ctx: PackageContext<AdoptionExportCfg>,
        keyMap: Map<String, Any>,
    ): Map<String, Asset> {
        val fullList = keyMap.keys.toList()
        val totalCount = fullList.size
        val idxBatchSize = 300
        val detailMap = mutableMapOf<String, Asset>()
        for (i in 0..totalCount step idxBatchSize) {
            val subList = fullList.subList(i, min(i + idxBatchSize, totalCount))
            ctx.client.assets
                .select()
                .where(Asset.GUID.`in`(subList))
                .includeOnResults(Asset.TYPE_NAME)
                .includeOnResults(Asset.QUALIFIED_NAME)
                .includeOnResults(Asset.NAME)
                .stream()
                .forEach {
                    detailMap[it.guid] = it
                }
        }
        return detailMap
    }
}
