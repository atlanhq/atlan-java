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
import java.nio.file.Paths
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
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AdoptionExportCfg>().use { ctx ->

            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            val xlsxFile = "$outputDirectory${File.separator}$FILENAME"
            val changesFile = "$outputDirectory${File.separator}$CHANGES_FILE"
            val viewsFile = "$outputDirectory${File.separator}$VIEWS_FILE"
            val userSearchesFile = "$outputDirectory${File.separator}$USER_SEARCHES_FILE"
            val userChangesFile = "$outputDirectory${File.separator}$USER_CHANGES_FILE"
            val userViewsFile = "$outputDirectory${File.separator}$USER_VIEWS_FILE"

            // Touch every file, just so they exist, to avoid any workflow failures
            Paths.get(outputDirectory).toFile().mkdirs()
            validatePathIsSafe(outputDirectory, FILENAME)
            Paths.get(xlsxFile).toFile().createNewFile()
            validatePathIsSafe(outputDirectory, CHANGES_FILE)
            Paths.get(changesFile).toFile().createNewFile()
            validatePathIsSafe(outputDirectory, VIEWS_FILE)
            Paths.get(viewsFile).toFile().createNewFile()
            validatePathIsSafe(outputDirectory, USER_SEARCHES_FILE)
            Paths.get(userSearchesFile).toFile().createNewFile()
            validatePathIsSafe(outputDirectory, USER_CHANGES_FILE)
            Paths.get(userChangesFile).toFile().createNewFile()
            validatePathIsSafe(outputDirectory, USER_VIEWS_FILE)
            Paths.get(userViewsFile).toFile().createNewFile()

            val fileOutputs = mutableListOf<String>()

            ExcelWriter(xlsxFile).use { xlsx ->
                if (ctx.config.includeViews != "NONE") {
                    if (xlsxOutput) {
                        AssetViews(ctx, xlsx.createSheet("Views"), logger).export()
                    } else {
                        CSVWriter(viewsFile).use { csv -> AssetViews(ctx, csv, logger).export() }
                    }
                    if (ctx.config.viewsDetails == "YES") {
                        if (xlsxOutput) {
                            DetailedUserViews(ctx, xlsx.createSheet("User views"), logger).export()
                        } else {
                            CSVWriter(userViewsFile).use { csv -> DetailedUserViews(ctx, csv, logger).export() }
                        }
                    }
                }
                if (ctx.config.includeChanges == "YES") {
                    if (xlsxOutput) {
                        AssetChanges(ctx, xlsx.createSheet("Changes"), logger).export()
                    } else {
                        CSVWriter(changesFile).use { csv -> AssetChanges(ctx, csv, logger).export() }
                    }
                    if (ctx.config.changesDetails == "YES") {
                        if (xlsxOutput) {
                            DetailedUserChanges(ctx, xlsx.createSheet("User changes"), logger).export()
                        } else {
                            CSVWriter(userChangesFile).use { csv -> DetailedUserChanges(ctx, csv, logger).export() }
                        }
                    }
                }
                if (ctx.config.includeSearches == "YES") {
                    if (xlsxOutput) {
                        DetailedSearches(ctx, xlsx.createSheet("User searches"), logger).export()
                    } else {
                        CSVWriter(userSearchesFile).use { csv -> DetailedSearches(ctx, csv, logger).export() }
                    }
                }
            }
            if (xlsxOutput) {
                fileOutputs.add(xlsxFile)
            } else {
                File(xlsxFile).delete()
                File(xlsxFile).createNewFile()
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
                            xlsxFile,
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
