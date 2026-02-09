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
import mu.KLogger

class TLAxQ(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "TLAxQ - Table-Level Assets without Queries",
        "TLAxQ",
        "**Total active table-level assets in Atlan that do *not* have any queries.** This is useful to:\n" +
            "- Identify tables that may be unused, and could be removed to save storage costs",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False positives could exist, when queries are missing due to: a source where usage is not tracked, a lack of freshly-mined usage data, or where the only queries have happened more than 30 days ago.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TLA.TABLE_LEVEL))
            // .withoutLineage()
            .where(Asset.SOURCE_READ_COUNT.eq(0))
            .pageSize(batchSize)
            .sort(Table.SIZE_BYTES.order(SortOrder.Desc))
            .sort(Asset.SOURCE_TOTAL_COST.order(SortOrder.Desc))
            .includesOnResults(TLA.ATTRIBUTES)
            .includeOnResults(Asset.SOURCE_READ_COUNT)
            .includeOnResults(Asset.SOURCE_TOTAL_COST)
            .includeOnResults(Asset.HAS_LINEAGE)

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Database" to "Name of the database for the table-level asset",
            "Schema" to "Name of the schema for the table-level asset",
            "Name" to "Name of the table-level asset itself",
            "Type" to "Type of the table-level asset",
            "Has lineage?" to "Whether the asset is involved in any lineage",
            "Size (GB)" to "Size of the table-level asset's storage, in gigabytes",
            "Rows" to "Total number of rows of data in the asset",
            "Cost" to "Total compute cost for the asset, in credits",
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
        val cost = sql.sourceTotalCost ?: 0.0
        return listOf(
            sql.connectorType?.value ?: "",
            sql.databaseName ?: "",
            sql.schemaName ?: "",
            sql.name ?: "",
            sql.typeName ?: "",
            sql.hasLineage ?: false,
            size,
            rows,
            cost,
            getAssetLink(sql.guid),
        )
    }
}
