/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class GCM(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "GCM - Glossary Categories Managed",
        "GCM",
        "**Total active categories across glossaries in Atlan.** This is useful to:\n" +
            "- Monitor how extensively organized concepts are in Atlan",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        GlossaryCategory
            .select(client)
            .pageSize(batchSize)
}
