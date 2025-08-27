/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ISQL
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.serde.cell.TimestampXformer
import mu.KLogger
import java.time.Instant
import java.time.temporal.ChronoUnit

class TLAxU(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "TLAxU - Table-Level Assets without Updates",
        "TLAxU",
        "**Total active table-level assets in Atlan that have *not* been updated (at source) in the last 90 days or more.** This is useful to:\n" +
            "- Identify tables that may be unused, and could be removed to save storage costs",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False positives could exist, when the metadata crawled from source has not been refreshed in the last 90 days or more.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> {
        val stale = Instant.now().minus(90, ChronoUnit.DAYS).toEpochMilli()
        return client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TLA.TABLE_LEVEL))
            .where(Asset.SOURCE_UPDATED_AT.gt(0))
            .where(Asset.SOURCE_UPDATED_AT.lt(stale))
            .pageSize(batchSize)
            .sort(Asset.SOURCE_UPDATED_AT.order(SortOrder.Asc))
            .includesOnResults(TLA.ATTRIBUTES)
            .includeOnResults(Asset.SOURCE_UPDATED_AT)
            .includeOnResults(Asset.SOURCE_READ_COUNT)
            .includeOnResults(Asset.SOURCE_TOTAL_COST)
    }

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Database" to "Name of the database for the table-level asset",
            "Schema" to "Name of the schema for the table-level asset",
            "Name" to "Name of the table-level asset itself",
            "Type" to "Type of the table-level asset",
            "Updated" to "Time and date at which the source was last updated",
            "Queries" to "Number of queries made against the asset in the last 30 days",
            "Cost" to "Total compute cost for the asset, in credits",
            "Size (GB)" to "Size of the table-level asset's storage, in gigabytes",
            "Rows" to "Total number of rows of data in the asset",
            "Link" to "Link to the detailed asset within Atlan",
        )

    /** {@inheritDoc} */
    override fun getDetailedRecord(asset: Asset): List<Any> {
        val sql = asset as ISQL
        val size: Double
        val rows: Long
        when (sql) {
            is Table -> {
                size = (sql.sizeBytes ?: 0) / BYTES_IN_GB
                rows = sql.rowCount ?: 0
            }
            is MaterializedView -> {
                size = (sql.sizeBytes ?: 0) / BYTES_IN_GB
                rows = sql.rowCount ?: 0
            }
            else -> {
                size = 0.0
                rows = 0
            }
        }
        val queryCount = sql.sourceReadCount ?: 0
        val updated = TimestampXformer.encode(sql.sourceUpdatedAt)
        val cost = sql.sourceTotalCost ?: 0.0
        return listOf(
            sql.connectorType?.value ?: "",
            sql.databaseName ?: "",
            sql.schemaName ?: "",
            sql.name ?: "",
            sql.typeName ?: "",
            updated,
            queryCount,
            cost,
            size,
            rows,
            getAssetLink(sql.guid),
        )
    }
}
