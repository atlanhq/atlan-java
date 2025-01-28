/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.MaterializedView
import com.atlan.pkg.PackageContext
import mu.KLogger

class MaterializedViewXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : ContainerXformer(
    ctx = ctx,
    typeNameFilter = MaterializedView.TYPE_NAME,
    preprocessedDetails = preprocessedDetails,
    logger = logger,
)
