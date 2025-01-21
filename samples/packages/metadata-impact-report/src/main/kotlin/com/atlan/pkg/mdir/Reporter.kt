/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir

import MetadataImpactReportCfg
import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Table
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
import com.atlan.pkg.serde.TabularWriter
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.xls.ExcelWriter
import com.atlan.util.AssetBatch
import java.io.File
import java.nio.file.Paths
import java.text.NumberFormat
import java.util.Locale

/**
 * Produce the metadata impact report
 */
object Reporter {
    private val logger = Utils.getLogger(Reporter.javaClass.name)

    private const val FILENAME = "mdir.xlsx"

    private val CSV_FILES =
        mapOf(
            "overview" to "overview.csv",
            "AUM" to "aum.csv",
            "AwD" to "awd.csv",
            "AwDC" to "awdc.csv",
            "AwDU" to "awdu.csv",
            "AwO" to "awo.csv",
            "AwOG" to "awog.csv",
            "AwOU" to "awou.csv",
            "DLA" to "dla.csv",
            "DLAxL" to "dlaxl.csv",
            "GCM" to "gcm.csv",
            "GTM" to "gtm.csv",
            "GUM" to "gum.csv",
            "HQV" to "hqv.csv",
            "SUT" to "sut.csv",
            "TLA" to "tla.csv",
            "TLAwL" to "tlawl.csv",
            "TLAxL" to "tlaxl.csv",
            "TLAxQ" to "tlaxq.csv",
            "TLAxU" to "tlaxu.csv",
            "UTA" to "uta.csv",
            "UTQ" to "utq.csv",
        )

    const val CAT_HEADLINES = "Headline numbers"
    const val CAT_SAVINGS = "Cost savings"
    const val CAT_ADOPTION = "Adoption metrics"

    val CATEGORIES =
        mapOf(
            CAT_HEADLINES to "**Metrics that break down Atlan-managed assets as overall numbers.** These are mostly useful to contextualize the overall asset footprint of your data ecosystem.",
            CAT_SAVINGS to "**Metrics that can be used to discover potential cost savings.** These are areas you may want to investigate for cost savings, though there are caveats with each one that are worth reviewing to understand potential limitations.",
            CAT_ADOPTION to "**Metrics that can be used to monitor Atlan's adoption within your organization.** You may want to consider these alongside some of the headline numbers to calculate percentages of enrichment points that are important to your organization.",
        )

    val SUBDOMAINS =
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

            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            Paths.get(outputDirectory).toFile().mkdirs()

            // Touch every file, just so they exist, to avoid any workflow failures
            val xlsxFile = "$outputDirectory${File.separator}$FILENAME"
            Paths.get(xlsxFile).toFile().createNewFile()
            CSV_FILES.forEach { (_, filename) ->
                val filePath = "$outputDirectory${File.separator}$filename"
                Paths.get(filePath).toFile().createNewFile()
            }

            val domain =
                if (ctx.config.includeDataProducts == "TRUE") {
                    createDomainIdempotent(ctx.client, ctx.config.dataDomain)
                } else {
                    null
                }
            val subdomainNameToGuid = createSubDomainsIdempotent(ctx.client, domain)
            val fileOutputs = runReports(ctx, outputDirectory, batchSize, domain, subdomainNameToGuid)

//            val glossary =
//                if (ctx.config.includeGlossary == "TRUE") {
//                    createGlossaryIdempotent(ctx.client, ctx.config.glossaryName)
//                } else {
//                    null
//                }
//            val categoryNameToGuid = createCategoriesIdempotent(ctx.client, glossary)
//            val fileOutputs = runReports(ctx, outputDirectory, batchSize, glossary, categoryNameToGuid)

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        Utils.sendEmail(
                            "[Atlan] Metadata Impact Report",
                            emails,
                            "Hi there! As requested, please find attached the Metadata Impact Report.\n\nAll the best!\nAtlan",
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

    private fun createGlossaryIdempotent(
        client: AtlanClient,
        glossaryName: String,
    ): Glossary =
        try {
            Glossary.findByName(client, glossaryName)
        } catch (e: NotFoundException) {
            val create =
                Glossary
                    .creator(glossaryName)
                    .assetIcon(AtlanIcon.PROJECTOR_SCREEN_CHART)
                    .build()
            val response = create.save(client)
            response.getResult(create)
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
    ): List<String> {
        if (ctx.config.fileFormat == "XLSX") {
            val outputFile = "$outputDirectory${File.separator}mdir.xlsx"
            ExcelWriter(outputFile).use { xlsx ->
                val overview = xlsx.createSheet("Overview")
                overview.writeHeader(
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
                    outputReport(ctx, metric, overview, xlsx.createSheet(metric.getShortName()), batchSize, glossary, categoryNameToGuid)
                }
            }
            return listOf(outputFile)
        } else {
            val overviewFile = "$outputDirectory${File.separator}${CSV_FILES["overview"]}"
            val outputFiles = mutableListOf<String>()
            CSVWriter(overviewFile).use { overview ->
                overview.writeHeader(
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
                    val metricFile = "$outputDirectory${File.separator}${CSV_FILES[metric.getShortName()]}"
                    CSVWriter(metricFile).use { details ->
                        outputReport(ctx, metric, overview, details, batchSize, glossary, categoryNameToGuid)
                    }
                    outputFiles.add(metricFile)
                }
            }
            return outputFiles
        }
    }

    private fun outputReport(
        ctx: PackageContext<MetadataImpactReportCfg>,
        metric: Metric,
        overview: TabularWriter,
        details: TabularWriter,
        batchSize: Int,
        glossary: Glossary? = null,
        categoryNameToGuid: Map<String, String>? = null,
    ) {
        logger.info { "Quantifying metric: ${metric.name} ..." }
        val quantified = metric.quantify()
        val term =
            if (ctx.config.includeGlossary == "TRUE") {
                writeMetricToGlossary(ctx.client, metric, quantified, glossary!!, categoryNameToGuid!!)
            } else {
                null
            }
        writeMetricToFile(ctx.client, metric, quantified, overview, details, ctx.config.includeDetails, term, batchSize)
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
            builder
                .announcementType(AtlanAnnouncementType.WARNING)
                .announcementTitle("Caveats")
                .announcementMessage(metric.caveats)
                .certificateStatus(CertificateStatus.DRAFT)
        } else {
            builder.certificateStatus(CertificateStatus.VERIFIED)
        }
        if (metric.notes.isNotBlank()) {
            builder
                .announcementType(AtlanAnnouncementType.INFORMATION)
                .announcementTitle("Note")
                .announcementMessage(metric.notes)
        }
        val term =
            builder
                .displayName(metric.displayName)
                .description(metric.description)
                .certificateStatusMessage(prettyQuantity)
                .category(GlossaryCategory.refByGuid(categoryNameToGuid[metric.category]))
                .build()
        val response = term.save(client)
        return response.getResult(term) ?: term.trimToRequired().guid(response.getAssignedGuid(term)).build()
    }

    private fun writeMetricToFile(
        client: AtlanClient,
        metric: Metric,
        quantified: Double,
        overview: TabularWriter,
        details: TabularWriter,
        includeDetails: Boolean,
        term: GlossaryTerm?,
        batchSize: Int,
    ) {
        overview.writeRecord(
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
            metric.outputDetailedRecords(details, term, batch)
            batch?.flush()
            batch?.close()
        }
    }

    private fun createDomainIdempotent(
        client: AtlanClient,
        domainName: String,
    ): DataDomain =
        try {
            DataDomain.findByName(client, domainName)[0]!!
        } catch (e: NotFoundException) {
            val create = DataDomain.creator(domainName).build()
            val response = create.save(client)
            response.getResult(create)
        }

    private fun createSubDomainsIdempotent(
        client: AtlanClient,
        domain: DataDomain?,
    ): Map<String, String> {
        if (domain == null) return emptyMap()
        val nameToResolved = mutableMapOf<String, String>()
        val placeholderToName = mutableMapOf<String, String>()
        AssetBatch(client, 20).use { batch ->
            SUBDOMAINS.forEach { (name, description) ->
                val builder =
                    try {
                        val found = DataDomain.findByName(client, name)[0]
                        found.trimToRequired().guid(found.guid)
                    } catch (e: NotFoundException) {
                        DataDomain.creator(name, domain.qualifiedName)
                    }
                val subdomain = builder.description(description).build()
                placeholderToName[subdomain.guid] = name
                batch.add(subdomain)
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
        domain: DataDomain? = null,
        subdomainNameToGuid: Map<String, String>? = null,
    ): List<String> {
        if (ctx.config.fileFormat == "XLSX") {
            val outputFile = "$outputDirectory${File.separator}mdir.xlsx"
            ExcelWriter(outputFile).use { xlsx ->
                val overview = xlsx.createSheet("Overview")
                overview.writeHeader(
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
                    outputReportDomain(ctx, metric, overview, xlsx.createSheet(metric.getShortName()), batchSize, domain, subdomainNameToGuid)
                }
            }
            return listOf(outputFile)
        } else {
            val overviewFile = "$outputDirectory${File.separator}${CSV_FILES["overview"]}"
            val outputFiles = mutableListOf<String>()
            CSVWriter(overviewFile).use { overview ->
                overview.writeHeader(
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
                    val metricFile = "$outputDirectory${File.separator}${CSV_FILES[metric.getShortName()]}"
                    CSVWriter(metricFile).use { details ->
                        outputReportDomain(ctx, metric, overview, details, batchSize, domain, subdomainNameToGuid)
                    }
                    outputFiles.add(metricFile)
                }
            }
            return outputFiles
        }
    }

    private fun outputReportDomain(
        ctx: PackageContext<MetadataImpactReportCfg>,
        metric: Metric,
        overview: TabularWriter,
        details: TabularWriter,
        batchSize: Int,
        domain: DataDomain? = null,
        subdomainNameToGuid: Map<String, String>? = null,
    ) {
        logger.info { "Quantifying metric: ${metric.name} ..." }
        val quantified = metric.quantify()
        val product =
            if (ctx.config.includeDataProducts == "TRUE") {
                writeMetricToDomain(ctx.client, metric, quantified, domain!!, subdomainNameToGuid!!)
            } else {
                null
            }
        writeMetricToFile(ctx.client, metric, quantified, overview, details, ctx.config.includeDetails, product, batchSize)
    }

    private fun writeMetricToDomain(
        client: AtlanClient,
        metric: Metric,
        quantified: Double,
        domain: DataDomain,
        subdomainNameToGuid: Map<String, String>,
    ): DataProduct {
        val builder =
            try {
                DataProduct.findByName(client, metric.name)[0]!!.trimToRequired()
            } catch (e: NotFoundException) {
                val qualifiedName = DataDomain.findByName(client, metric.category)[0]!!.qualifiedName
                val assets =
                    Table
                        .select(client)
                        .where(Table.CERTIFICATE_STATUS.eq(CertificateStatus.VERIFIED))
                        .where(Table.ATLAN_TAGS.eq(qualifiedName))
                        .build()
                DataProduct.creator(client, metric.name, qualifiedName, assets)
            }
        val prettyQuantity = NumberFormat.getNumberInstance(Locale.US).format(quantified)
        if (metric.caveats.isNotBlank()) {
            builder
                .announcementType(AtlanAnnouncementType.WARNING)
                .announcementTitle("Caveats")
                .announcementMessage(metric.caveats)
                .certificateStatus(CertificateStatus.DRAFT)
        } else {
            builder.certificateStatus(CertificateStatus.VERIFIED)
        }
        if (metric.notes.isNotBlank()) {
            builder
                .announcementType(AtlanAnnouncementType.INFORMATION)
                .announcementTitle("Note")
                .announcementMessage(metric.notes)
        }

        val product =
            builder
                .displayName(metric.displayName)
                .description(metric.description)
                .certificateStatusMessage(prettyQuantity)
                .build()
        val response = product.save(client)
        return response.getResult(product) ?: product.trimToRequired().guid(response.getAssignedGuid(product)).build()
    }

    private fun writeMetricToFile(
        client: AtlanClient,
        metric: Metric,
        quantified: Double,
        overview: TabularWriter,
        details: TabularWriter,
        includeDetails: Boolean,
        product: DataProduct?,
        batchSize: Int,
    ) {
        overview.writeRecord(
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
                if (product != null) {
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
            metric.outputDetailedRecords(details, product, batch)
            batch?.flush()
            batch?.close()
        }
    }
}
