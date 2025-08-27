/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class GTM(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "GTM - Glossary Terms Managed",
        "GTM",
        "**Total active terms across glossaries in Atlan.** This is useful to:\n" +
            "- Monitor how extensively key data concepts are defined in Atlan",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        GlossaryTerm
            .select(client)
            .pageSize(batchSize)
}
