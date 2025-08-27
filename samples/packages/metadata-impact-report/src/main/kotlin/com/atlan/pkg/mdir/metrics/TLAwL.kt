/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class TLAwL(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "TLAwL - Table-Level Assets with Lineage",
        "TLAwL",
        "**Total active table-level assets in Atlan (tables, views, materialized views) *with* lineage.** This is useful to:\n" +
            "- Monitor the footprint of data stores in your data ecosystem for which you can do root-cause and impact analysis",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TLA.TABLE_LEVEL))
            .withLineage()
            .pageSize(batchSize)
}
