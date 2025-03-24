/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import mu.KLogger
import java.util.stream.Stream

/**
 * Import connections into Atlan from a provided CSV file.
 *
 * Only the connections and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context through which this package is running
 * @param inputFile the input file containing connection details
 * @param logger through which to record logging
 */
class ConnectionImporter(
    ctx: PackageContext<AssetImportCfg>,
    private val inputFile: String,
    logger: KLogger,
) : CSVImporter(
        ctx = ctx,
        filename = inputFile,
        logger = logger,
        typeNameFilter = Connection.TYPE_NAME,
        attrsToOverwrite = attributesToClear(ctx.config.assetsAttrToOverwrite.toMutableList(), "assets", logger),
        creationHandling = if (ctx.config.assetsUpsertSemantic == "update") AssetCreationHandling.NONE else AssetCreationHandling.FULL,
        batchSize = 1,
        trackBatches = true,
        fieldSeparator = ctx.config.assetsFieldSeparator[0],
    ) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
    }

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Connection.NAME.atlanFieldName)?.let { it as String } ?: ""
        val qualifiedName = deserializer.getValue(Connection.QUALIFIED_NAME.atlanFieldName)?.let { it as String } ?: ""
        val connectorType = Connection.getConnectorTypeFromQualifiedName(qualifiedName)
        if (connectorType == null || connectorType == AtlanConnectorType.UNKNOWN_CUSTOM) {
            throw NoSuchElementException("Invalid connectorType provided for the connection, cannot be processed: $connectorType")
        }
        val identity = ctx.connectionCache.getIdentityForAsset(name, connectorType)
        val existing = ctx.connectionCache.getByIdentity(identity)
        return if (existing != null) {
            existing.trimToRequired()
        } else {
            val users = deserializer.getValue(Connection.ADMIN_USERS.atlanFieldName)?.let { it as List<String> }
            val groups = deserializer.getValue(Connection.ADMIN_GROUPS.atlanFieldName)?.let { it as List<String> }
            val roles = deserializer.getValue(Connection.ADMIN_ROLES.atlanFieldName)?.let { it as List<String> }
            Connection.creator(ctx.client, name, connectorType, roles, groups, users)
        }
    }

    /** {@inheritDoc} */
    override fun cacheCreated(list: Stream<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            ctx.connectionCache.cacheById(asset.guid)
        }
    }
}
