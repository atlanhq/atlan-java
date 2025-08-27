/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.AUM.Companion.EXCLUDE_ASSETS
import mu.KLogger

class AwD(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "AwD - Assets with Descriptions",
        "AwD",
        "**Total active assets in Atlan with descriptions.** This is useful to monitor the level of basic enrichment of assets across your data ecosystem.",
        Reporter.CAT_ADOPTION,
        client,
        batchSize,
        logger,
        notes = "The value for sub-metrics AwDC and AwDU may not add up to this number, as this metric will only count an asset once even if it has both a system-crawled and user-entered description.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> {
        val hasSystemDesc = AwDC(client, batchSize, logger).query().build().toQuery()
        val hasUserDesc = AwDU(client, batchSize, logger).query().build().toQuery()
        return client.assets
            .select()
            .whereNot(Asset.TYPE_NAME.`in`(EXCLUDE_ASSETS))
            .whereSome(hasSystemDesc)
            .whereSome(hasUserDesc)
            .minSomes(1)
            .pageSize(batchSize)
    }
}
