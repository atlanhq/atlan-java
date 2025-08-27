/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class GUM(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "GUM - Glossaries Under Management",
        "GUM",
        "**Total active glossaries in Atlan.** This is useful to:\n" +
            "- Monitor how concepts are broadly organized in Atlan",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        Glossary
            .select(client)
            .pageSize(batchSize)
}
