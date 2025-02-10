/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import AdoptionExportCfg
import com.atlan.model.search.SearchLog
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.TabularWriter
import mu.KLogger

class DetailedSearches(
    private val ctx: PackageContext<AdoptionExportCfg>,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    fun export() {
        val start = ctx.config.searchesFrom * 1000
        val end = ctx.config.searchesTo * 1000
        logger.info { "Exporting details of all UI-based searches between [$start, $end]..." }
        writer.writeHeader(
            mapOf(
                "Time" to "Time at which the search occurred",
                "Username" to "User who searched",
                "Query" to "Text the user entered for the search (if empty, the search was the result of filtering by some facet)",
                "Total" to "Total number of assets included",
                "Types" to "Type(s) of the first 20 assets that were found",
                "Qualified names" to "Unique name(s) of the first 20 assets that were found",
            ),
        )
        SearchLog
            .searches(ctx.client, start, end)
            .stream()
            .forEach {
                writer.writeRecord(
                    listOf(
                        it.createdAt ?: it.timestamp ?: "",
                        it.userName ?: "",
                        it.searchInput ?: "",
                        it.resultsCount ?: "0",
                        it.resultTypeNamesAllowed ?: "",
                        it.resultQualifiedNamesAllowed ?: "",
                    ),
                )
            }
    }
}
