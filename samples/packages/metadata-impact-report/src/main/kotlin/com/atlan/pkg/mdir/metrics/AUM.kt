/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.mdir.metrics

import com.atlan.AtlanClient
import com.atlan.model.assets.Asset
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.AuthService
import com.atlan.model.assets.BIProcess
import com.atlan.model.assets.Badge
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.DbtColumnProcess
import com.atlan.model.assets.DbtProcess
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.Link
import com.atlan.model.assets.Persona
import com.atlan.model.assets.Purpose
import com.atlan.model.assets.Readme
import com.atlan.model.assets.ReadmeTemplate
import com.atlan.model.search.FluentSearch.FluentSearchBuilder
import com.atlan.pkg.mdir.Reporter
import mu.KLogger

class AUM(
    client: AtlanClient,
    batchSize: Int,
    logger: KLogger,
) : Metric(
        "AUM - Assets Under Management",
        "AUM",
        "**Total active assets in Atlan.** This is useful to:\n" +
            "- Monitor the footprint of your data ecosystem\n" +
            "- Monitor Atlan's rollout across your complete data ecosystem\n" +
            "- Provide the basis for calculating a percentage of assets from other metrics relative to the overall footprint of your data ecosystem",
        Reporter.CAT_HEADLINES,
        client,
        batchSize,
        logger,
    ) {
    companion object {
        val EXCLUDE_ASSETS =
            setOf(
                Link.TYPE_NAME,
                Readme.TYPE_NAME,
                ReadmeTemplate.TYPE_NAME,
                Badge.TYPE_NAME,
                LineageProcess.TYPE_NAME,
                ColumnProcess.TYPE_NAME,
                DbtProcess.TYPE_NAME,
                DbtColumnProcess.TYPE_NAME,
                BIProcess.TYPE_NAME,
                Persona.TYPE_NAME,
                Purpose.TYPE_NAME,
                AuthPolicy.TYPE_NAME,
                AuthService.TYPE_NAME,
            )
    }

    /** {@inheritDoc} */
    override fun query(): FluentSearchBuilder<*, *> =
        client.assets
            .select()
            .whereNot(Asset.TYPE_NAME.`in`(EXCLUDE_ASSETS))
            .pageSize(batchSize)
}
