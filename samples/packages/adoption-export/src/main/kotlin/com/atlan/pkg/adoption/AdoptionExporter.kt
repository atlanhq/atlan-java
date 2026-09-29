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
import com.atlan.pkg.serde.TabularWriter
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

    /**
     * A single logical portion of the export: one worksheet when producing XLSX output,
     * one CSV file otherwise.
     */
    internal enum class Section(
        val csvFileName: String,
        val sheetName: String,
    ) {
        VIEWS(VIEWS_FILE, "Views"),
        USER_VIEWS(USER_VIEWS_FILE, "User views"),
        CHANGES(CHANGES_FILE, "Changes"),
        USER_CHANGES(USER_CHANGES_FILE, "User changes"),
        USER_SEARCHES(USER_SEARCHES_FILE, "User searches"),
    }

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
            val csvFiles = Section.entries.associateWith { validatePathIsSafe(outputDirectory, it.csvFileName) }
            csvFiles.values.forEach { it.toFile().createNewFile() }

            val fileOutputs = mutableListOf<String>()

            ExcelWriter(xlsxFile.toString()).use { xlsx ->
                sectionsToExport(ctx.config).forEach { section ->
                    if (xlsxOutput) {
                        export(ctx, section, xlsx.createSheet(section.sheetName))
                    } else {
                        val csvFile = csvFiles.getValue(section).toString()
                        CSVWriter(csvFile).use { csv -> export(ctx, section, csv) }
                        // Only the CSVs that were actually written are delivered, so that
                        // empty placeholder files are never emailed or uploaded
                        fileOutputs.add(csvFile)
                    }
                }
            }
            if (xlsxOutput) {
                fileOutputs.add(xlsxFile.toString())
            } else {
                xlsxFile.toFile().delete()
                xlsxFile.toFile().createNewFile()
            }

            logger.info { "Files to deliver (${ctx.config.deliveryType}): ${fileOutputs.joinToString()}" }

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        if (fileOutputs.isEmpty()) {
                            logger.warn { "No output files were produced -- the email will be sent without any attachments." }
                        }
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

    /**
     * Determine which portions of the export the provided configuration asks for.
     * This is the single source of truth for both the contents of the export and the
     * files that are delivered by email or to object storage.
     *
     * @param config the configuration of the package
     * @return the sections to export, in the order they should appear
     */
    internal fun sectionsToExport(config: AdoptionExportCfg): List<Section> =
        buildList {
            if (config.includeViews != "NONE") {
                add(Section.VIEWS)
                if (config.viewsDetails == "YES") {
                    add(Section.USER_VIEWS)
                }
            }
            if (config.includeChanges == "YES") {
                add(Section.CHANGES)
                if (config.changesDetails == "YES") {
                    add(Section.USER_CHANGES)
                }
            }
            if (config.includeSearches == "YES") {
                add(Section.USER_SEARCHES)
            }
        }

    private fun export(
        ctx: PackageContext<AdoptionExportCfg>,
        section: Section,
        writer: TabularWriter,
    ) = when (section) {
        Section.VIEWS -> AssetViews(ctx, writer, logger).export()
        Section.USER_VIEWS -> DetailedUserViews(ctx, writer, logger).export()
        Section.CHANGES -> AssetChanges(ctx, writer, logger).export()
        Section.USER_CHANGES -> DetailedUserChanges(ctx, writer, logger).export()
        Section.USER_SEARCHES -> DetailedSearches(ctx, writer, logger).export()
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
