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

class SUT(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "SUT - Single-User Tables",
        "SUT",
        "**Tables that are only queried by a single user.** This is useful to:\n" +
            "- Identity opportunities to consolidate data needs for users into shared tables, reducing storage costs",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False positives could exist, when the single user listed is a non-personal (system-level) account that may be used by an application or process, rather than an individual user.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TLA.TABLE_LEVEL))
            .where(Asset.SOURCE_READ_COUNT.gt(0))
            .where(Asset.SOURCE_READ_USER_COUNT.between(0, 1))
            .pageSize(batchSize)
            .sort(Asset.SOURCE_TOTAL_COST.order(SortOrder.Desc))
            .sort(Table.SIZE_BYTES.order(SortOrder.Desc))
            .includesOnResults(TLA.ATTRIBUTES)
            .includeOnResults(Asset.SOURCE_READ_COUNT)
            .includeOnResults(Asset.SOURCE_READ_USER_COUNT)
            .includeOnResults(Asset.SOURCE_TOTAL_COST)
            .includeOnResults(Asset.SOURCE_READ_TOP_USERS)

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Database" to "Name of the database for the table-level asset",
            "Schema" to "Name of the schema for the table-level asset",
            "Name" to "Name of the table-level asset itself",
            "Type" to "Type of the table-level asset",
            "User" to "Username of the single user of this table, if any",
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
        val users = sql.sourceReadTopUsers ?: setOf()
        val user = if (users.isNotEmpty()) users.first() else ""
        val cost = sql.sourceTotalCost ?: 0.0
        return listOf(
            sql.connectorType?.value ?: "",
            sql.databaseName ?: "",
            sql.schemaName ?: "",
            sql.name ?: "",
            sql.typeName ?: "",
            user,
            queryCount,
            cost,
            size,
            rows,
            getAssetLink(sql.guid),
        )
    }
}
