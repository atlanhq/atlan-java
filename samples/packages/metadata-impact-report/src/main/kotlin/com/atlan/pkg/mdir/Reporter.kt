/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir

import MetadataImpactReportCfg
import com.atlan.Atlan
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.CertificateStatus
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
import mu.KotlinLogging
import org.apache.poi.ss.usermodel.Sheet
import java.io.File
import java.text.NumberFormat
import java.util.Locale

/**
 * Produce the metadata impact report
 */
object Reporter {
    private val logger = KotlinLogging.logger {}

    const val CAT_HEADLINES = "Headline numbers"
    const val CAT_SAVINGS = "Cost savings"
    const val CAT_ADOPTION = "Adoption metrics"

    val CATEGORIES = mapOf(
        CAT_HEADLINES to "**Metrics that break down Atlan-managed assets as overall numbers.** These are mostly useful to contextualize the overall asset footprint of your data ecosystem.",
        CAT_SAVINGS to "**Metrics that can be used to discover potential cost savings.** These are areas you may want to investigate for cost savings, though there are caveats with each one that are worth reviewing to understand potential limitations.",
        CAT_ADOPTION to "**Metrics that can be used to monitor Atlan's adoption within your organization.** You may want to consider these alongside some of the headline numbers to calculate percentages of enrichment points that are important to your organization.",
    )

    private val reports = listOf(
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
        val config = Utils.setPackageOps<MetadataImpactReportCfg>()
        val batchSize = 50
        val glossaryName = Utils.getOrDefault(config.glossaryName, "Metadata metrics")
        val includeDetails = Utils.getOrDefault(config.includeDetails, false)

        val glossary = createGlossaryIdempotent(glossaryName)
        val categoryNameToGuid = createCategoriesIdempotent(glossary)
        runReports(outputDirectory, batchSize, includeDetails, glossary, categoryNameToGuid)
    }

    private fun createGlossaryIdempotent(glossaryName: String): Glossary {
        return try {
            Glossary.findByName(glossaryName)
        } catch (e: NotFoundException) {
            val create = Glossary.creator(glossaryName)
                .assetIcon(AtlanIcon.PROJECTOR_SCREEN_CHART)
                .build()
            val response = create.save()
            response.getResult(create)
        }
    }

    private fun createCategoriesIdempotent(glossary: Glossary): Map<String, String> {
        val nameToResolved = mutableMapOf<String, String>()
        val placeholderToName = mutableMapOf<String, String>()
        val batch = AssetBatch(Atlan.getDefaultClient(), 20)
        CATEGORIES.forEach { (name, description) ->
            val builder = try {
                val found = GlossaryCategory.findByNameFast(name, glossary.qualifiedName, listOf(GlossaryCategory.ANCHOR))[0]
                found.trimToRequired().guid(found.guid)
            } catch (e: NotFoundException) {
                GlossaryCategory.creator(name, glossary)
            }
            val category = builder.description(description).build()
            placeholderToName[category.guid] = name
            batch.add(category)
        }
        batch.flush()
        batch.resolvedGuids.forEach { (placeholder, resolved) ->
            val name = placeholderToName[placeholder]!!
            nameToResolved[name] = resolved
        }
        return nameToResolved
    }

    private fun runReports(outputDirectory: String, batchSize: Int, includeDetails: Boolean, glossary: Glossary, categoryNameToGuid: Map<String, String>) {
        ExcelWriter("$outputDirectory${File.separator}mdir.xlsx").use { xlsx ->
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
                val metric = Metric.get(repClass, Atlan.getDefaultClient(), batchSize, logger)
                logger.info { "Quantifying metric: ${metric.name} ..." }
                val quantified = metric.quantify()
                writeMetricToGlossary(metric, quantified, glossary, categoryNameToGuid)
                writeMetricToExcel(metric, quantified, xlsx, overview, includeDetails)
            }
        }
    }

    private fun writeMetricToGlossary(metric: Metric, quantified: Double, glossary: Glossary, categoryNameToGuid: Map<String, String>) {
        val builder = try {
            GlossaryTerm.findByNameFast(metric.name, glossary.qualifiedName).trimToRequired()
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
        val term = builder.displayName(metric.displayName)
            .description(metric.description)
            .certificateStatusMessage(prettyQuantity)
            .category(GlossaryCategory.refByGuid(categoryNameToGuid[metric.category]))
            .build()
        term.save()
    }

    private fun writeMetricToExcel(metric: Metric, quantified: Double, xlsx: ExcelWriter, overview: Sheet, includeDetails: Boolean) {
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
            metric.outputDetailedRecords(xlsx)
        }
    }
}