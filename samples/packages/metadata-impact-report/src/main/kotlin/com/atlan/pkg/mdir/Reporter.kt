/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir

import MetadataImpactReportCfg
import com.atlan.AtlanClient
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.enums.AtlanAnnouncementType
import com.atlan.model.enums.CertificateStatus
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.validatePathIsSafe
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
import java.nio.file.Path
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
        val od = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<MetadataImpactReportCfg>().use { ctx ->
            val batchSize = 300

            val xlsxOutput = ctx.config.fileFormat == "XLSX"

            val outputDirectory = validatePathIsSafe(od)
            outputDirectory.toFile().mkdirs()

            // Touch every file, just so they exist, to avoid any workflow failures
            val xlsxFile = validatePathIsSafe(outputDirectory, FILENAME)
            xlsxFile.toFile().createNewFile()
            CSV_FILES.forEach { (_, filename) ->
                val filePath = validatePathIsSafe(outputDirectory, filename)
                filePath.toFile().createNewFile()
            }

            val domain =
                if (ctx.config.includeDataProducts == "TRUE") {
                    createDomainIdempotent(ctx.client, ctx.config.dataDomain)
                } else {
                    null
                }

            val subdomainNameToQualifiedName = createSubDomainsIdempotent(ctx.client, domain)
            val fileOutputs = runReports(ctx, outputDirectory, batchSize, subdomainNameToQualifiedName)

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
                    if (emails.isNotEmpty()) {
                        Utils.sendEmail(
                            "[Atlan] Metadata Impact Report",
                            emails,
                            "Hi there! As requested, please find attached the Metadata Impact Report.\n\nAll the best!\nAtlan",
                            fileOutputs.map { it.toFile() },
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
                                it.toString(),
                                ctx.config.targetPrefix,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createDomainIdempotent(
        client: AtlanClient,
        domainName: String,
    ): Asset =
        try {
            val domain =
                DataDomain
                    .select(client)
                    .where(DataDomain.NAME.eq(domainName))
                    .whereNot(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
                    .stream()
                    .toList()
            if (domain.isEmpty()) {
                val create = DataDomain.creator(domainName).build()
                val response = create.save(client)
                response.getResult(create)
            } else {
                domain.first()
            }
        } catch (e: NotFoundException) {
            val create = DataDomain.creator(domainName).build()
            val response = create.save(client)
            response.getResult(create)
        }

    private fun createSubDomainsIdempotent(
        client: AtlanClient,
        domain: Asset?,
    ): Map<String, String> {
        if (domain == null) return emptyMap()
        val nameToResolved = mutableMapOf<String, String>()
        AssetBatch(client, 20).use { batch ->
            SUBDOMAINS.forEach { (name, description) ->
                val builder =
                    try {
                        val found =
                            DataDomain
                                .select(client)
                                .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.eq(domain.qualifiedName))
                                .where(DataDomain.NAME.eq(name))
                                .stream()
                                .toList()
                                .firstOrNull()
                        if (found != null) {
                            found.trimToRequired().guid(found.guid)
                        } else {
                            DataDomain.creator(name, domain.qualifiedName)
                        }
                    } catch (e: NotFoundException) {
                        DataDomain.creator(name, domain.qualifiedName)
                    }
                val subdomain = builder.description(description).build()
                batch.add(subdomain)
            }
            batch.flush()
            while (nameToResolved.size < SUBDOMAINS.size) {
                DataDomain
                    .select(client)
                    .where(DataDomain.GUID.`in`(batch.resolvedGuids.values.toList()))
                    .stream()
                    .forEach {
                        nameToResolved[it.name] = it.qualifiedName
                    }
            }
        }
        return nameToResolved
    }

    private fun runReports(
        ctx: PackageContext<MetadataImpactReportCfg>,
        outputDirectory: Path,
        batchSize: Int = 300,
        subdomainNameToQualifiedName: Map<String, String>,
    ): List<Path> {
        if (ctx.config.fileFormat == "XLSX") {
            val outputFile = validatePathIsSafe(outputDirectory, "mdir.xlsx")
            ExcelWriter(outputFile.toString()).use { xlsx ->
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
                    outputReportDomain(ctx, metric, overview, xlsx.createSheet(metric.getShortName()), subdomainNameToQualifiedName)
                }
            }
            return listOf(outputFile)
        } else {
            val overviewFile = validatePathIsSafe(outputDirectory, CSV_FILES["overview"] ?: "overview.csv")
            val outputFiles = mutableListOf<Path>()
            CSVWriter(overviewFile.toString()).use { overview ->
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
                    val metricFile = validatePathIsSafe(outputDirectory, CSV_FILES[metric.getShortName()] ?: "")
                    CSVWriter(metricFile.toString()).use { details ->
                        outputReportDomain(ctx, metric, overview, details, subdomainNameToQualifiedName)
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
        subdomainNameToQualifiedName: Map<String, String>,
    ) {
        logger.info { "Quantifying metric: ${metric.name} ..." }
        val quantified = metric.quantify()
        if (ctx.config.includeDataProducts == "TRUE") {
            writeMetricToDomain(ctx.client, metric, quantified, subdomainNameToQualifiedName)
        }
        writeMetricToFile(metric, quantified, overview, details, ctx.config.includeDetails)
    }

    private fun writeMetricToDomain(
        client: AtlanClient,
        metric: Metric,
        quantified: Long,
        subdomainNameToQualifiedName: Map<String, String>,
    ): Asset {
        val builder =
            try {
                DataProduct
                    .select(client)
                    .where(DataProduct.PARENT_DOMAIN_QUALIFIED_NAME.eq(subdomainNameToQualifiedName[metric.category]))
                    .where(DataProduct.NAME.eq(metric.name))
                    .stream()
                    .toList()
                    .firstOrNull()
                    ?.trimToRequired() ?: DataProduct.creator(
                    client,
                    metric.name,
                    subdomainNameToQualifiedName[metric.category],
                    metric.query().build(),
                )
            } catch (e: NotFoundException) {
                DataProduct.creator(
                    client,
                    metric.name,
                    subdomainNameToQualifiedName[metric.category],
                    metric.query().build(),
                )
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
        metric: Metric,
        quantified: Long,
        overview: TabularWriter,
        details: TabularWriter,
        includeDetails: Boolean,
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
            metric.outputDetailedRecords(details)
        }
    }
}
