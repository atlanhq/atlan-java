/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.AggregationBucketResult
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

abstract class Metric(
    val name: String,
    val displayName: String,
    val description: String,
    val category: String,
    protected val client: AtlanClient,
    protected val batchSize: Int,
    protected val logger: KLogger,
    val caveats: String = "",
    val notes: String = "",
) {

    companion object {
        const val BYTES_IN_GB = 1073741824.0

        /**
         * Factory method to build a report, given the class.
         *
         * @param report class for the report to build
         * @param client Atlan tenant to run the report against
         * @param batchSize maximum number of objects to request in any given API call
         * @param logger through which to record information
         */
        fun get(report: Class<*>, client: AtlanClient, batchSize: Int, logger: KLogger): Metric {
            return report.getDeclaredConstructor(
                AtlanClient::class.java,
                Int::class.java,
                KLogger::class.java,
            ).newInstance(
                client,
                batchSize,
                logger,
            ) as Metric
        }

        /**
         * Convert the provided epoch-based timestamp into a human-readable date and time.
         *
         * @param ts epoch-based numeric timestamp
         * @return a human-readable date and time
         */
        fun formatTimestamp(ts: Long?): String {
            return if (ts == null) {
                ""
            } else {
                ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
        }
    }

    /**
     * Construct a link that can be used to jump straight to an asset in Atlan.
     *
     * @param guid of the asset
     * @return the full URL to view the asset in Atlan
     */
    fun getAssetLink(guid: String): String {
        return "${client.baseUrl}/assets/$guid/overview"
    }

    /**
     * Output the detailed records for this report.
     *
     * @param xlsx the Excel writer in which to create a sheet and dump out the detailed result records
     */
    fun outputDetailedRecords(xlsx: ExcelWriter) {
        val header = getDetailedHeader()
        if (header.isNotEmpty()) {
            val sheet = xlsx.createSheet(name)
            xlsx.addHeader(sheet, header)
            query().stream().forEach { asset ->
                val row = getDetailedRecord(asset)
                xlsx.appendRow(sheet, row)
            }
        }
    }

    /**
     * Query that defines the results for this particular report.
     *
     * @return a fluent search query builder for the report's results
     */
    abstract fun query(): FluentSearchBuilder<*, *>

    /**
     * Calculate a single overall headline metric for this report.
     * Note: by default, this requires the query to define a metric aggregation keyed by "total".
     *
     * @return a single numeric metric representing this report
     */
    open fun quantify(): Double {
        val response = query().pageSize(1).toRequest().search(client)
        val overall = response.aggregations["total"]?.metric ?: 0.0
        logger.info {
            " ... overall metric for $name: $overall"
        }
        if (response.aggregations.containsKey("breakdown")) {
            val agg = response.aggregations["breakdown"]
            agg as AggregationBucketResult
            val map = mutableMapOf<String, Number>()
            agg.buckets.forEach {
                map[it.key.toString()] = it.docCount
            }
            logger.info {
                " ... metric breakdown for $name: $map"
            }
        }
        return overall
    }

    /**
     * Produce a header row for detailed records of every result in this report.
     *
     * @return a mapping from column name to a description of the column's use
     */
    open fun getDetailedHeader(): Map<String, String> {
        return mapOf()
    }

    /**
     * Produce a single row of detailed output for a single result in this report.
     *
     * @param asset the asset from which to draw the details for the row
     * @return a list of values for the row
     */
    open fun getDetailedRecord(asset: Asset): List<Any> {
        return listOf()
    }
}
