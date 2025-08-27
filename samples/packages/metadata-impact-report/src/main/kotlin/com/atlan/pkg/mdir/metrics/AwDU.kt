/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import com.atlan.pkg.mdir.metrics.AUM.Companion.EXCLUDE_ASSETS
import mu.KLogger

class AwDU(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "AwDU - Assets with Descriptions User-Entered",
        "AwDU",
        "**Total active assets in Atlan with descriptions entered by a user in Atlan.** This is distinct from assets whose description have been crawled from source.",
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
            .where(Asset.USER_DESCRIPTION.hasAnyValue())
            .whereNot(Asset.USER_DESCRIPTION.eq(""))
            .pageSize(batchSize)
}
