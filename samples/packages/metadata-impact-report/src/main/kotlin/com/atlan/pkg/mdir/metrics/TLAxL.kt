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

class TLAxL(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "TLAxL - Table-Level Assets without Lineage",
        "TLAxL",
        "**Total active table-level assets in Atlan that do not have any lineage.** This is useful to:\n" +
            "- Check whether you expect lineage for these tables, views and materialized views\n" +
            "- If not, whether these tables, views or materialized views are actually used or could be removed to reduce storage and costs",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False positives could exist, when lineage is missing due to: not all data sources being loaded, improper sequence of crawling data tools, or due to bugs or lack of lineage support for the data sources involved.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TLA.TABLE_LEVEL))
            .withoutLineage()
            .pageSize(batchSize)
            .sort(Asset.SOURCE_TOTAL_COST.order(SortOrder.Desc))
            .sort(Table.SIZE_BYTES.order(SortOrder.Desc))
            .includesOnResults(TLA.ATTRIBUTES)
            .includeOnResults(Asset.HAS_LINEAGE)
            .includeOnResults(Asset.SOURCE_TOTAL_COST)

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Database" to "Name of the database for the table-level asset",
            "Schema" to "Name of the schema for the table-level asset",
            "Name" to "Name of the table-level asset itself",
            "Type" to "Type of the table-level asset",
            "Subtype" to "Subtype of this table-level asset (for example, external tables)",
            "Size (GB)" to "Size of the table-level asset's storage, in gigabytes",
            "Cost" to "Total compute cost for the asset, in credits",
            "Link" to "Link to the detailed asset within Atlan",
        )

    /** {@inheritDoc} */
    override fun getDetailedRecord(asset: Asset): List<Any> {
        val sql = asset as ISQL
        val size =
            when (sql) {
                is Table -> (sql.sizeBytes ?: 0) / BYTES_IN_GB
                is MaterializedView -> (sql.sizeBytes ?: 0) / BYTES_IN_GB
                else -> 0.0
            }
        val cost = sql.sourceTotalCost ?: 0.0
        return listOf(
            sql.connectorType?.value ?: "",
            sql.databaseName ?: "",
            sql.schemaName ?: "",
            sql.name ?: "",
            sql.typeName ?: "",
            sql.subType ?: "",
            size,
            cost,
            getAssetLink(sql.guid),
        )
    }
}
