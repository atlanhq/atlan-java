/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.TLA.Companion.TABLE_LEVEL
import mu.KLogger

class UTQ(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "UTQ - Usage-Tracked Queries",
        "UTQ",
        "**Total active queries Atlan has mined as part of tracking usage metadata.** This is useful to:\n" +
            "- Identity the relative footprint of queries for which you can monitor the usage-based metrics",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .where(Asset.TYPE_NAME.`in`(TABLE_LEVEL))
            .where(Asset.SOURCE_READ_COUNT.gt(0))
            .pageSize(batchSize)
}
