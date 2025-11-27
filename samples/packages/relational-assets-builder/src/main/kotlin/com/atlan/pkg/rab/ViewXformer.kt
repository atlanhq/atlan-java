/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.View
import com.atlan.pkg.PackageContext
import mu.KLogger

class ViewXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    preprocessedDetails: ColumnXformer.Results,
    private val logger: KLogger,
) : ContainerXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = View.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        logger = logger,
    )
