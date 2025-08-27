/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ISQL
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.TablePartition
import com.atlan.model.assets.View
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class TLA(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "TLA - Table-Level Assets",
        "TLA",
        "**Total active table-level assets in Atlan (tables, views, materialized views).** This is useful to:\n" +
            "- Monitor the footprint of data stores in your data ecosystem\n" +
            "- Monitor Atlan's rollout across your data store ecosystem\n" +
            "- Provide the basis for calculating a percentage of assets from other metrics relative to the overall footprint of your data stores",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    companion object {
        val TABLE_LEVEL =
            setOf(
                Table.TYPE_NAME,
                View.TYPE_NAME,
                MaterializedView.TYPE_NAME,
                TablePartition.TYPE_NAME,
            )
        val ATTRIBUTES =
            setOf(
                Asset.CONNECTOR_TYPE,
                ISQL.DATABASE_NAME,
                ISQL.SCHEMA_NAME,
                Asset.NAME,
                Table.SUB_TYPE,
                Table.ROW_COUNT,
                Table.SIZE_BYTES,
            )
    }

    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TABLE_LEVEL))
            .pageSize(batchSize)
}
