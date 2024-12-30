/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import AdoptionExportCfg
import com.atlan.model.assets.AuthPolicy
import com.atlan.model.assets.AuthService
import com.atlan.model.search.AggregationBucketResult
import com.atlan.model.search.AuditSearch
import com.atlan.model.search.AuditSearchRequest
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.AdoptionExporter.getAssetDetails
import com.atlan.pkg.serde.TabularWriter
import mu.KLogger

class AssetChanges(
    private val ctx: PackageContext<AdoptionExportCfg>,
    private val writer: TabularWriter,
    private val logger: KLogger,
) {
    companion object {
        val EXCLUDE_TYPES =
            listOf(
                AuthService.TYPE_NAME,
                AuthPolicy.TYPE_NAME,
            )
    }

    fun export() {
        logger.info { "Exporting changed assets..." }
        writer.writeHeader(
            mapOf(
                "Type" to "Type of asset",
                "Qualified name" to "Unique name of the asset",
                "Name" to "Simple name for the asset",
                "Total changes" to "Total number of changes made to the asset in the timeframe selected",
                "Link" to "Link to the asset's profile page in Atlan",
            ),
        )
        val builder =
            AuditSearch.builder(ctx.client)
                .whereNot(AuditSearchRequest.ENTITY_TYPE.`in`(EXCLUDE_TYPES))
                .aggregate("changes", AuditSearchRequest.ENTITY_ID.bucketBy(ctx.config.changesMax.toInt()))
        if (ctx.config.changesByUser.isNotEmpty()) {
            builder.where(AuditSearchRequest.USER.`in`(ctx.config.changesByUser))
        }
        if (ctx.config.changesTypes.isNotEmpty()) {
            builder.where(AuditSearchRequest.ACTION.`in`(ctx.config.changesTypes))
        }
        if (ctx.config.changesFrom > 0) {
            builder.where(AuditSearchRequest.CREATED.gte(ctx.config.changesFrom * 1000))
        }
        if (ctx.config.changesTo > 0) {
            builder.where(AuditSearchRequest.CREATED.lt(ctx.config.changesTo * 1000))
        }
        // First a request to get the aggregate details
        val request = builder.pageSize(1).toRequest()
        val response = request.search(ctx.client)
        val changes = response.aggregations["changes"] as AggregationBucketResult
        val changeCountMap = mutableMapOf<String, Long>()
        changes.buckets.forEach {
            val guid = it.key as String
            changeCountMap[guid] = it.docCount
        }

        // Then bulk-request details about the assets themselves
        val assetMap = getAssetDetails(ctx, changeCountMap)
        changeCountMap.forEach { (guid, changeCount) ->
            val asset = assetMap[guid]
            writer.writeRecord(
                listOf(
                    asset?.typeName ?: "(deleted)",
                    asset?.qualifiedName ?: guid,
                    asset?.name ?: "(deleted)",
                    changeCount,
                    Utils.getAssetLink(ctx.client, guid),
                ),
            )
        }
    }
}
