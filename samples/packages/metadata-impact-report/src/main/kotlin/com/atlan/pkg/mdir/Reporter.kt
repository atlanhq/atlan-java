/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir

import MetadataImpactReportCfg
import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.mdir.metrics.AUM
import com.atlan.pkg.mdir.metrics.AwD
import com.atlan.pkg.mdir.metrics.AwDC
import com.atlan.pkg.mdir.metrics.AwDU
import com.atlan.pkg.mdir.metrics.AwO
import com.atlan.pkg.mdir.metrics.AwOG
import com.atlan.pkg.mdir.metrics.AwOU
import com.atlan.pkg.mdir.metrics.DLA
import com.atlan.pkg.mdir.metrics.DLAxL
import com.atlan.pkg.mdir.metrics.GCM
import com.atlan.pkg.mdir.metrics.GTM
import com.atlan.pkg.mdir.metrics.GUM
import com.atlan.pkg.mdir.metrics.HQV
import com.atlan.pkg.mdir.metrics.Metric
import com.atlan.pkg.mdir.metrics.SUT
import com.atlan.pkg.mdir.metrics.TLA
import com.atlan.pkg.mdir.metrics.TLAwL
import com.atlan.pkg.mdir.metrics.TLAxL
import com.atlan.pkg.mdir.metrics.TLAxQ
import com.atlan.pkg.mdir.metrics.TLAxU
import com.atlan.pkg.mdir.metrics.UTA
import com.atlan.pkg.mdir.metrics.UTQ
import com.atlan.pkg.serde.xls.ExcelWriter
import com.atlan.util.AssetBatch
import org.apache.poi.ss.usermodel.Sheet
import java.io.File
import java.text.NumberFormat
import java.util.Locale

/**
 * Produce the metadata impact report
 */
object Reporter {
    private val logger = Utils.getLogger(Reporter.javaClass.name)

    const val CAT_HEADLINES = "Headline numbers"
    const val CAT_SAVINGS = "Cost savings"
    const val CAT_ADOPTION = "Adoption metrics"

    val CATEGORIES =
        mapOf(
            CAT_HEADLINES to "**Metrics that break down Atlan-managed assets as overall numbers.** These are mostly useful to contextualize the overall asset footprint of your data ecosystem.",
            CAT_SAVINGS to "**Metrics that can be used to discover potential cost savings.** These are areas you may want to investigate for cost savings, though there are caveats with each one that are worth reviewing to understand potential limitations.",
            CAT_ADOPTION to "**Metrics that can be used to monitor Atlan's adoption within your organization.** You may want to consider these alongside some of the headline numbers to calculate percentages of enrichment points that are important to your organization.",
        )

    private val reports =
        listOf(
            AUM::class.java,
            TLA::class.java,
            DLA::class.java,
            GUM::class.java,
            GCM::class.java,
            GTM::class.java,
            UTQ::class.java,
            UTA::class.java,
            HQV::class.java,
            TLAwL::class.java,
            TLAxL::class.java,
            DLAxL::class.java,
            AwD::class.java,
            AwDC::class.java,
            AwDU::class.java,
            AwO::class.java,
            AwOG::class.java,
            AwOU::class.java,
            TLAxQ::class.java,
            SUT::class.java,
            TLAxU::class.java,
        )

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<MetadataImpactReportCfg>().use { ctx ->
            val batchSize = 300

            val glossary =
                if (ctx.config.includeGlossary == "TRUE") {
                    createGlossaryIdempotent(ctx.client, ctx.config.glossaryName)
                } else {
                    null
                }
            val categoryNameToGuid = createCategoriesIdempotent(ctx.client, glossary)
            val reportFile = runReports(ctx, outputDirectory, batchSize, glossary, categoryNameToGuid)

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        Utils.sendEmail(
                            "[Atlan] Metadata Impact Report",
                            emails,
                            "Hi there! As requested, please find attached the Metadata Impact Report.\n\nAll the best!\nAtlan",
                            listOf(File(reportFile)),
                        )
                    }
                }

                "CLOUD" -> {
                    Utils.uploadOutputFile(
                        reportFile,
                        ctx.config.targetPrefix,
                        ctx.config.targetKey,
                    )
                }
            }
        }
    }

    private fun createGlossaryIdempotent(
        client: AtlanClient,
        glossaryName: String,
    ): Glossary {
        return try {
            Glossary.findByName(client, glossaryName)
        } catch (e: NotFoundException) {
            val create =
                Glossary.creator(glossaryName)
                    .assetIcon(AtlanIcon.PROJECTOR_SCREEN_CHART)
                    .build()
            val response = create.save(client)
            response.getResult(create)
        }
    }

    private fun createCategoriesIdempotent(
        client: AtlanClient,
        glossary: Glossary?,
    ): Map<String, String> {
        if (glossary == null) return emptyMap()
        val nameToResolved = mutableMapOf<String, String>()
        val placeholderToName = mutableMapOf<String, String>()
        AssetBatch(client, 20).use { batch ->
            CATEGORIES.forEach { (name, description) ->
                val builder =
                    try {
                        val found = GlossaryCategory.findByNameFast(client, name, glossary.qualifiedName)[0]
                        found.trimToRequired().guid(found.guid)
                    } catch (e: NotFoundException) {
                        GlossaryCategory.creator(name, glossary)
                    }
                val category = builder.description(description).build()
                placeholderToName[category.guid] = name
                batch.add(category)
            }
            batch.flush()
            placeholderToName.forEach { (guid, name) ->
                val resolved = batch.resolvedGuids.getOrDefault(guid, guid)
                nameToResolved[name] = resolved
            }
        }
        return nameToResolved
    }

    private fun runReports(
        ctx: PackageContext<MetadataImpactReportCfg>,
        outputDirectory: String,
        batchSize: Int = 300,
        glossary: Glossary? = null,
        categoryNameToGuid: Map<String, String>? = null,
    ): String {
        val outputFile = "$outputDirectory${File.separator}mdir.xlsx"
        ExcelWriter(outputFile).use { xlsx ->
            val overview = xlsx.createSheet("Overview")
            xlsx.addHeader(
                overview,
                mapOf(
                    "Metric" to "",
                    "Description" to "",
                    "Result" to "Numeric result for the metric",
                    "Caveats" to "Any caveats to be aware of with the metric",
                    "Notes" to "Any other information to be aware of with the metric",
                    // "Percentage" to "Percentage of total for the metric",
                ),
            )
            reports.forEach { repClass ->
                val metric = Metric.get(repClass, ctx.client, batchSize, logger)
                logger.info { "Quantifying metric: ${metric.name} ..." }
                val quantified = metric.quantify()
                val term =
                    if (ctx.config.includeGlossary == "TRUE") {
                        writeMetricToGlossary(ctx.client, metric, quantified, glossary!!, categoryNameToGuid!!)
                    } else {
                        null
                    }
                writeMetricToExcel(ctx.client, metric, quantified, xlsx, overview, ctx.config.includeDetails, term, batchSize)
            }
        }
        return outputFile
    }

    private fun writeMetricToGlossary(
        client: AtlanClient,
        metric: Metric,
        quantified: Double,
        glossary: Glossary,
        categoryNameToGuid: Map<String, String>,
    ): GlossaryTerm {
        val builder =
            try {
                GlossaryTerm.findByNameFast(client, metric.name, glossary.qualifiedName).trimToRequired()
            } catch (e: NotFoundException) {
                GlossaryTerm.creator(metric.name, glossary)
            }
        val prettyQuantity = NumberFormat.getNumberInstance(Locale.US).format(quantified)
        if (metric.caveats.isNotBlank()) {
            builder.announcementType(AtlanAnnouncementType.WARNING)
                .announcementTitle("Caveats")
                .announcementMessage(metric.caveats)
                .certificateStatus(CertificateStatus.DRAFT)
        } else {
            builder.certificateStatus(CertificateStatus.VERIFIED)
        }
        if (metric.notes.isNotBlank()) {
            builder.announcementType(AtlanAnnouncementType.INFORMATION)
                .announcementTitle("Note")
                .announcementMessage(metric.notes)
        }
        val term =
            builder.displayName(metric.displayName)
                .description(metric.description)
                .certificateStatusMessage(prettyQuantity)
                .category(GlossaryCategory.refByGuid(categoryNameToGuid[metric.category]))
                .build()
        val response = term.save(client)
        return response.getResult(term) ?: term.trimToRequired().guid(response.getAssignedGuid(term)).build()
    }

    private fun writeMetricToExcel(
        client: AtlanClient,
        metric: Metric,
        quantified: Double,
        xlsx: ExcelWriter,
        overview: Sheet,
        includeDetails: Boolean,
        term: GlossaryTerm?,
        batchSize: Int,
    ) {
        xlsx.appendRow(
            overview,
            listOf(
                metric.name,
                metric.description,
                quantified,
                metric.caveats,
                metric.notes,
            ),
        )
        if (includeDetails) {
            val batch =
                if (term != null) {
                    AssetBatch(
                        client,
                        batchSize,
                        false,
                        AssetBatch.CustomMetadataHandling.IGNORE,
                        true,
                        false,
                        false,
                        false,
                        AssetCreationHandling.FULL,
                        false,
                    )
                } else {
                    null
                }
            metric.outputDetailedRecords(xlsx, term, batch)
            batch?.flush()
            batch?.close()
        }
    }
}
