/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import AdoptionExportCfg
import com.atlan.model.search.SearchLog
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.TabularWriter
import mu.KLogger

class DetailedUserViews(
    private val ctx: PackageContext<AdoptionExportCfg>,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    fun export() {
        val from = ctx.config.viewsFrom * 1000
        val to = ctx.config.viewsTo * 1000
        logger.info { "Exporting details of all asset views between [$from, $to]..." }
        writer.writeHeader(
            mapOf(
                "Time" to "Time at which the view / search occurred",
                "Username" to "User who viewed / searched",
                "Total" to "Total number of assets included",
                "Type" to "Type(s) of asset that were viewed",
                "Qualified name" to "Unique name(s) of the asset(s)",
                "Link" to "Link to the asset's profile page in Atlan",
            ),
        )
        SearchLog
            .views(ctx.client, from, to)
            .stream()
            .forEach {
                val guid = it.resultGuidsAllowed?.get(0) ?: ""
                writer.writeRecord(
                    listOf(
                        it.createdAt ?: it.timestamp ?: "",
                        it.userName ?: "",
                        it.resultsCount ?: "0",
                        it.resultTypeNamesAllowed ?: "",
                        it.resultQualifiedNamesAllowed ?: "",
                        if (guid.isNotBlank()) Utils.getAssetLink(ctx.client, guid) else "",
                    ),
                )
            }
    }
}
