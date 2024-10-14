/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.model.search.SearchLogRequest
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class DetailedUserViews(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
    private val start: Long,
    private val end: Long,
) {
    fun export() {
        logger.info { "Exporting details of all asset views between [$start, $end]..." }
        val sheet = xlsx.createSheet("User views")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Time" to "Time at which the view / search occurred",
                "Username" to "User who viewed / searched",
                "Total" to "Total number of assets included",
                "GUIDs" to "Globally unique identifiers for each asset that was included",
            ),
        )
        SearchLogRequest.views(start, end)
            .stream()
            .forEach {
                xlsx.appendRow(
                    sheet,
                    listOf(
                        it.createdAt ?: it.timestamp ?: "",
                        it.userName ?: "",
                        it.resultsCount ?: "0",
                        it.resultGuidsAllowed ?: "",
                    ),
                )
            }
    }
}
