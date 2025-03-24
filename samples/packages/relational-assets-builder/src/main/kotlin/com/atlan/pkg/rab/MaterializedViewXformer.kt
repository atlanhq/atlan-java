/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.MaterializedView
import com.atlan.pkg.PackageContext
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import mu.KLogger

class MaterializedViewXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    preprocessedDetails: Importer.Results,
    deferredConnectionCache: MutableMap<ConnectionIdentity, String>,
    private val logger: KLogger,
) : ContainerXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = MaterializedView.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        deferredConnectionCache = deferredConnectionCache,
        logger = logger,
    )
