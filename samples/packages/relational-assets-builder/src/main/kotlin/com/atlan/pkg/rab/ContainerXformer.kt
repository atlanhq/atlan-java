/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Table
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import mu.KLogger

abstract class ContainerXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    typeNameFilter: String,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : AssetXformer(
    ctx = ctx,
    typeNameFilter = typeNameFilter,
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
        val columnCount = preprocessedDetails.qualifiedNameToChildCount[details.uniqueQN]?.toLong()
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Table.SCHEMA_NAME) to details.parentName,
                RowSerde.getHeaderForField(Table.SCHEMA_QUALIFIED_NAME) to parentQN,
                RowSerde.getHeaderForField(Table.SCHEMA) to "Schema@$parentQN",
                RowSerde.getHeaderForField(Table.COLUMN_COUNT) to (columnCount?.toString() ?: ""),
            )
        } else {
            mapOf()
        }
    }
}
