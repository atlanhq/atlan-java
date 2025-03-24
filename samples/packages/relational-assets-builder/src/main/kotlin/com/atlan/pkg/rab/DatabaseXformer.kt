/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Database
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import mu.KLogger

class DatabaseXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    preprocessedDetails: Importer.Results,
    deferredConnectionCache: MutableMap<ConnectionIdentity, String>,
    private val logger: KLogger,
) : AssetXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = Database.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        deferredConnectionCache = deferredConnectionCache,
        logger = logger,
    ) {
    override fun mapAsset(inputRow: Map<String, String>): Map<String, String> {
        val connectionQN = getConnectionQN(ctx, inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter, preprocessedDetails.entityQualifiedNameToType)
        val assetQN = "$connectionQN/${details.partialQN}"
        val schemaCount = preprocessedDetails.qualifiedNameToChildCount[details.uniqueQN]?.toInt()
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Database.SCHEMA_COUNT, Database::class.java) to (schemaCount?.toString() ?: ""),
            )
        } else {
            mapOf()
        }
    }
}
