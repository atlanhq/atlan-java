/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
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
 * @param preprocessed details of the preprocessed CSV file
 * @param logger through which to record logging
 */
class ConnectionImporter(
    ctx: PackageContext<RelationalAssetsBuilderCfg>,
    private val preprocessed: Importer.Results,
    logger: KLogger,
) : AssetImporter(
        ctx,
        null,
        preprocessed.preprocessedFile,
        // Only allow full or updates to connections, as partial connections would be hidden
        // and impossible to delete via utilities like the Connection Delete workflow
        Connection.TYPE_NAME,
        logger,
        creationHandling = if (ctx.config.assetsUpsertSemantic == "update") AssetCreationHandling.NONE else AssetCreationHandling.FULL,
        batchSize = 1,
        trackBatches = true,
    ) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
    }

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Connection.CONNECTION_NAME.atlanFieldName)?.let { it as String } ?: ""
        val type =
            deserializer.getValue(CONNECTOR_TYPE)?.let { it as AtlanConnectorType }
                ?: throw NoSuchElementException("No typeName provided for the connection, cannot be processed.")
        val identity = ctx.connectionCache.getIdentityForAsset(name, type)
        val existing = ctx.connectionCache.getByIdentity(identity)
        return if (existing != null) {
            existing.trimToRequired()
        } else {
            val users = deserializer.getValue(Connection.ADMIN_USERS.atlanFieldName)?.let { it as List<String> }
            val groups = deserializer.getValue(Connection.ADMIN_GROUPS.atlanFieldName)?.let { it as List<String> }
            val roles = deserializer.getValue(Connection.ADMIN_ROLES.atlanFieldName)?.let { it as List<String> }
            Connection.creator(name, type, roles, groups, users)
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
