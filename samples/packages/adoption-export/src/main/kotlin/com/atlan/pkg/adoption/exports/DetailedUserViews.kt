/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import AdoptionExportCfg
import com.atlan.model.search.SearchLogRequest
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class DetailedUserViews(
    private val ctx: PackageContext<AdoptionExportCfg>,
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
) {
    fun export() {
        val from = ctx.config.viewsFrom!!.toLong() * 1000
        val to = ctx.config.viewsTo!!.toLong() * 1000
        logger.info { "Exporting details of all asset views between [$from, $to]..." }
        val sheet = xlsx.createSheet("User views")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Time" to "Time at which the view / search occurred",
                "Username" to "User who viewed / searched",
                "Total" to "Total number of assets included",
                "Type" to "Type(s) of asset that were viewed",
                "Qualified name" to "Unique name(s) of the asset(s)",
                "Link" to "Link to the asset's profile page in Atlan",
            ),
        )
        SearchLogRequest.views(from, to)
            .stream(ctx.client)
            .forEach {
                val guid = it.resultGuidsAllowed?.get(0) ?: ""
                xlsx.appendRow(
                    sheet,
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
