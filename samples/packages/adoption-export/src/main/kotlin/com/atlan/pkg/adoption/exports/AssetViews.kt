/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.search.SearchLogRequest
import com.atlan.pkg.Utils
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
                        "Type" to "Type of asset",
                        "Qualified name" to "Unique name of the asset",
                        "Name" to "Simple name for the asset",
                        "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                        "Distinct users" to "Total number of unique users that have viewed the asset",
                        "Link" to "Link to the asset's profile page in Atlan",
                    ),
                )
                SearchLogRequest.mostViewedAssets(maxAssets, false).forEach {
                    val asset = Asset.get(Atlan.getDefaultClient(), it.guid, false)
                    xlsx.appendRow(
                        sheet,
                        listOf(
                            asset.typeName,
                            asset.qualifiedName,
                            asset.name,
                            it.totalViews,
                            it.distinctUsers,
                            Utils.getAssetLink(it.guid),
                        ),
                    )
                }
            }
            "BY_USERS" -> {
                xlsx.addHeader(
                    sheet,
                    mapOf(
                        "Type" to "Type of asset",
                        "Qualified name" to "Unique name of the asset",
                        "Name" to "Simple name for the asset",
                        "Distinct users" to "Total number of unique users that have viewed the asset",
                        "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                        "Link" to "Link to the asset's profile page in Atlan",
                    ),
                )
                SearchLogRequest.mostViewedAssets(maxAssets, true).forEach {
                    val asset = Asset.get(Atlan.getDefaultClient(), it.guid, false)
                    xlsx.appendRow(
                        sheet,
                        listOf(
                            asset.typeName,
                            asset.qualifiedName,
                            asset.name,
                            it.distinctUsers,
                            it.totalViews,
                            Utils.getAssetLink(it.guid),
                        ),
                    )
                }
            }
        }
    }
}
