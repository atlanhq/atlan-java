/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.model.search.SearchLogRequest
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class AssetViews(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
    private val by: String,
    private val maxAssets: Int,
) {
    fun export() {
        logger.info { "Exporting top $maxAssets most-viewed assets..." }
        val sheet = xlsx.createSheet("Views")
        when (by) {
            "BY_VIEWS" -> {
                xlsx.addHeader(
                    sheet,
                    mapOf(
                        "GUID" to "Unique identifier (GUID) of the asset",
                        "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                        "Distinct users" to "Total number of unique users that have viewed the asset",
                    ),
                )
                SearchLogRequest.mostViewedAssets(maxAssets, false).forEach {
                    xlsx.appendRow(
                        sheet,
                        listOf(
                            it.guid,
                            it.totalViews,
                            it.distinctUsers,
                        ),
                    )
                }
            }
            "BY_USERS" -> {
                xlsx.addHeader(
                    sheet,
                    mapOf(
                        "GUID" to "Unique identifier (GUID) of the asset",
                        "Distinct users" to "Total number of unique users that have viewed the asset",
                        "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                    ),
                )
                SearchLogRequest.mostViewedAssets(maxAssets, true).forEach {
                    xlsx.appendRow(
                        sheet,
                        listOf(
                            it.guid,
                            it.distinctUsers,
                            it.totalViews,
                        ),
                    )
                }
            }
        }
    }
}
