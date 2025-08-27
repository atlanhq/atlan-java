/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.AUM.Companion.EXCLUDE_ASSETS
import mu.KLogger

class AwDC(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "AwDC - Assets with Descriptions Crawled",
        "AwDC",
        "**Total active assets in Atlan with descriptions crawled from some source.** This is distinct from assets whose description has been provided solely through the Atlan UI.",
        Reporter.CAT_ADOPTION,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .whereNot(Asset.TYPE_NAME.`in`(EXCLUDE_ASSETS))
            .where(Asset.DESCRIPTION.hasAnyValue())
            .whereNot(Asset.DESCRIPTION.eq(""))
            .pageSize(batchSize)
}
