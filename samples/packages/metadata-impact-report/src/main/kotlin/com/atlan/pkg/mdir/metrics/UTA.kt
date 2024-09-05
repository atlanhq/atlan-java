/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.TLA.Companion.TABLE_LEVEL
import mu.KLogger

class UTA(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "UTA - Usage-Tracked Assets",
        "UTA",
        "**Total active assets in Atlan for which queries are tracked through usage metadata.** This is useful to:\n" +
            "- Identity the relative footprint of your data store ecosystem for which you can monitor the usage-based metrics",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> {
        return client.assets.select()
            .where(Asset.TYPE_NAME.`in`(TABLE_LEVEL))
            .where(Asset.SOURCE_READ_COUNT.gt(0))
            .pageSize(batchSize)
            .aggregate("total", Asset.GUID.distinct())
            .aggregate("breakdown", Asset.TYPE_NAME.bucketBy(10))
    }
}
