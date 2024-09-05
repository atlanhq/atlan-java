/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.AUM.Companion.EXCLUDE_ASSETS
import mu.KLogger

class AwO(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "AwO - Assets with Owners",
        "AwO",
        "**Total active assets in Atlan with owners.** This is useful to monitor the level of basic enrichment of assets across your data ecosystem.",
        Reporter.CAT_ADOPTION,
        client,
        batchSize,
        logger,
        notes = "The value for sub-metrics AwOG and AwOU may not add up to this number, as this metric will only count an asset once even if it has both group and individual owners.",
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> {
        val hasIndividualOwner = AwOU(client, batchSize, logger).query().build().toQuery()
        val hasGroupOwner = AwOG(client, batchSize, logger).query().build().toQuery()
        return client.assets.select()
            .whereNot(Asset.TYPE_NAME.`in`(EXCLUDE_ASSETS))
            .whereSome(hasIndividualOwner)
            .whereSome(hasGroupOwner)
            .minSomes(1)
            .pageSize(batchSize)
            .aggregate("total", Asset.GUID.distinct())
            .aggregate("breakdown", Asset.TYPE_NAME.bucketBy(100))
    }
}
