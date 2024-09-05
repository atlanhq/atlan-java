/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import com.atlan.Atlan
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.AuthService
import com.atlan.model.search.AggregationBucketResult
import com.atlan.model.search.AuditSearch
import com.atlan.model.search.AuditSearchRequest
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.AdoptionExporter.getAssetDetails
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger

class AssetChanges(
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
    private val users: List<String>,
    private val actions: List<String>,
    private val start: Long,
    private val end: Long,
    private val maxAssets: Int,
) {
    private val excludeTypes =
        listOf(
            AuthService.TYPE_NAME,
            AuthPolicy.TYPE_NAME,
        )

    fun export() {
        logger.info { "Exporting changed assets..." }
        val sheet = xlsx.createSheet("Changes")
        xlsx.addHeader(
            sheet,
            mapOf(
                "Type" to "Type of asset",
                "Qualified name" to "Unique name of the asset",
                "Name" to "Simple name for the asset",
                "Total changes" to "Total number of changes made to the asset in the timeframe selected",
                "Link" to "Link to the asset's profile page in Atlan",
            ),
        )
        val client = Atlan.getDefaultClient()
        val builder =
            AuditSearch.builder(client)
                .whereNot(AuditSearchRequest.ENTITY_TYPE.`in`(excludeTypes))
                .aggregate("changes", AuditSearchRequest.ENTITY_ID.bucketBy(maxAssets))
        if (users.isNotEmpty()) {
            builder.where(AuditSearchRequest.USER.`in`(users))
        }
        if (actions.isNotEmpty()) {
            builder.where(AuditSearchRequest.ACTION.`in`(actions))
        }
        if (start > 0) {
            builder.where(AuditSearchRequest.CREATED.gte(start))
        }
        if (end > 0) {
            builder.where(AuditSearchRequest.CREATED.lt(end))
        }
        // First a request to get the aggregate details
        val request = builder.pageSize(1).toRequest()
        val response = request.search()
        val changes = response.aggregations["changes"] as AggregationBucketResult
        val changeCountMap = mutableMapOf<String, Long>()
        changes.buckets.forEach {
            val guid = it.key as String
            changeCountMap[guid] = it.docCount
        }

        // Then bulk-request details about the assets themselves
        val assetMap = getAssetDetails(changeCountMap)
        changeCountMap.forEach { (guid, changeCount) ->
            val asset = assetMap[guid]
            if (asset != null) {
                xlsx.appendRow(
                    sheet,
                    listOf(
                        asset.typeName,
                        asset.qualifiedName,
                        asset.name,
                        changeCount,
                        Utils.getAssetLink(guid),
                    ),
                )
            }
        }
    }
}
