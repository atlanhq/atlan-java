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
    completeHeaders: List<String>,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : AssetXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = Schema.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        logger = logger,
    ) {
    override fun mapAsset(inputRow: Map<String, String>): Map<String, String> {
        val connectionQN = getConnectionQN(inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter, preprocessedDetails.entityQualifiedNameToType)
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
                RowSerde.getHeaderForField(Schema.DATABASE_NAME, Schema::class.java) to details.parentName,
                RowSerde.getHeaderForField(Schema.DATABASE_QUALIFIED_NAME, Schema::class.java) to parentQN,
                RowSerde.getHeaderForField(Schema.DATABASE, Schema::class.java) to "Database@$parentQN",
                RowSerde.getHeaderForField(Schema.TABLE_COUNT, Schema::class.java) to (tableCount?.toString() ?: "0"),
                RowSerde.getHeaderForField(Schema.VIEW_COUNT, Schema::class.java) to (viewCount?.toString() ?: "0"),
            )
        } else {
            mapOf()
        }
    }
}
