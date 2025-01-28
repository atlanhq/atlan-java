/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Schema
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import mu.KLogger

class SchemaXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : AssetXformer(
    ctx = ctx,
    typeNameFilter = Schema.TYPE_NAME,
    preprocessedDetails = preprocessedDetails,
    logger = logger,
) {
    override fun mapAsset(
        inputRow: Map<String, String>,
    ): Map<String, String> {
        val connectionQN = getConnectionQN(ctx, inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter)
        val assetQN = "$connectionQN/${details.partialQN}"
        val parentQN = "$connectionQN/${details.parentPartialQN}"
        val tableCount = preprocessedDetails.qualifiedNameToTableCount[details.uniqueQN]?.toInt()
        val viewCount = preprocessedDetails.qualifiedNameToViewCount[details.uniqueQN]?.toInt()
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Schema.DATABASE_NAME) to details.parentName,
                RowSerde.getHeaderForField(Schema.DATABASE_QUALIFIED_NAME) to parentQN,
                RowSerde.getHeaderForField(Schema.DATABASE) to "Database@$parentQN",
                RowSerde.getHeaderForField(Schema.TABLE_COUNT) to (tableCount?.toString() ?: ""),
                RowSerde.getHeaderForField(Schema.VIEW_COUNT) to (viewCount?.toString() ?: ""),
            )
        } else {
            mapOf()
        }
    }
}
