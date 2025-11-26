/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Database
import com.atlan.model.assets.Table
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import mu.KLogger

abstract class ContainerXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    typeNameFilter: String,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : AssetXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = typeNameFilter,
        preprocessedDetails = preprocessedDetails,
        logger = logger,
    ) {
    /** {@inheritDoc} */
    override fun validateHeader(header: List<String>?): List<String> {
        val missing = super.validateHeader(header).toMutableList()
        if (header.isNullOrEmpty()) {
            missing.add(Database.DATABASE_NAME.atlanFieldName)
            missing.add(Database.SCHEMA_NAME.atlanFieldName)
            missing.add("entityName")
        } else {
            if (!header.contains(Database.DATABASE_NAME.atlanFieldName)) {
                missing.add(Database.DATABASE_NAME.atlanFieldName)
            }
            if (!header.contains(Database.SCHEMA_NAME.atlanFieldName)) {
                missing.add(Database.SCHEMA_NAME.atlanFieldName)
            }
            if (!header.contains("entityName")) {
                missing.add("entityName")
            }
        }
        return missing
    }

    /** {@inheritDoc} */
    override fun mapAsset(inputRow: Map<String, String>): Map<String, String> {
        val connectionQN = getConnectionQN(inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter, preprocessedDetails.entityQualifiedNameToType)
        val assetQN = "$connectionQN/${details.partialQN}"
        val parentQN = "$connectionQN/${details.parentPartialQN}"
        val columnCount = preprocessedDetails.qualifiedNameToChildCount[details.uniqueQN]?.toLong()
        val columns = if (ctx.config.deltaSemantic == "full") columnCount?.toString() ?: "0" else columnCount?.toString() ?: ""
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Table.DATABASE_NAME, Table::class.java) to details.databaseName,
                RowSerde.getHeaderForField(Table.DATABASE_QUALIFIED_NAME, Table::class.java) to "$connectionQN/${details.databasePQN}",
                RowSerde.getHeaderForField(Table.SCHEMA_NAME, Table::class.java) to details.parentName,
                RowSerde.getHeaderForField(Table.SCHEMA_QUALIFIED_NAME, Table::class.java) to parentQN,
                RowSerde.getHeaderForField(Table.SCHEMA, Table::class.java) to "Schema@$parentQN",
                RowSerde.getHeaderForField(Table.COLUMN_COUNT, Table::class.java) to columns,
            )
        } else {
            mapOf()
        }
    }
}
