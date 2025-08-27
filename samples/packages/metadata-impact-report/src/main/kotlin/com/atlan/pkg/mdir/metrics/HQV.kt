/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ISQL
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.View
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class HQV(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "HQV - Heavily-Queried Views",
        "HQV",
        "**Views that are frequently queried.** This is useful to:\n" +
            "- Identity opportunities to materialize the data in the views to potentially reduce compute costs",
        Reporter.CAT_SAVINGS,
        client,
        batchSize,
        logger,
        caveats = "False negatives could exist (views not in the list), when: usage data is not tracked for the data stores for those views, or the views are used heavily but only infrequently and not within the last 30 days.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(listOf(View.TYPE_NAME, MaterializedView.TYPE_NAME)))
            .where(Asset.SOURCE_READ_COUNT.gt(100))
            .pageSize(batchSize)
            .sort(Asset.SOURCE_READ_COUNT.order(SortOrder.Desc))
            .includesOnResults(TLA.ATTRIBUTES)
            .includeOnResults(Asset.SOURCE_READ_COUNT)

    /** {@inheritDoc} */
    override fun getDetailedHeader(): Map<String, String> =
        mapOf(
            "Connector" to "Type of the data source",
            "Database" to "Name of the database for the table-level asset",
            "Schema" to "Name of the schema for the table-level asset",
            "Name" to "Name of the table-level asset itself",
            "Queries" to "Number of queries in the last 30 days against this view",
            "Link" to "Link to the detailed asset within Atlan",
        )

    /** {@inheritDoc} */
    override fun getDetailedRecord(asset: Asset): List<Any> {
        val sql = asset as ISQL
        return listOf(
            sql.connectorType?.value ?: "",
            sql.databaseName ?: "",
            sql.schemaName ?: "",
            sql.name ?: "",
            sql.sourceReadCount ?: 0,
            getAssetLink(sql.guid),
        )
    }
}
