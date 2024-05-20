/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.serde.RowDeserializer
import mu.KotlinLogging

/**
 * Import connections into Atlan from a provided CSV file.
 *
 * Only the connections and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param preprocessed details of the preprocessed CSV file
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class ConnectionImporter(
    private val preprocessed: Importer.PreprocessedCsv,
    private val attrsToOverwrite: List<AtlanField>,
    private val creationHandling: AssetCreationHandling,
    private val batchSize: Int,
    trackBatches: Boolean,
    fieldSeparator: Char,
) : AssetImporter(
    preprocessed.preprocessedFile,
    attrsToOverwrite,
    // Only allow full or updates to connections, as partial connections would be hidden
    // and impossible to delete via utilities like the Connection Delete workflow
    when (creationHandling) {
        AssetCreationHandling.FULL, AssetCreationHandling.PARTIAL -> AssetCreationHandling.FULL
        else -> creationHandling
    },
    batchSize,
    Connection.TYPE_NAME,
    KotlinLogging.logger {},
    trackBatches,
    fieldSeparator,
) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
    }

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Connection.CONNECTION_NAME.atlanFieldName)?.let { it as String } ?: ""
        val type = deserializer.getValue(CONNECTOR_TYPE)?.let { it as AtlanConnectorType }
            ?: throw NoSuchElementException("No typeName provided for the connection, cannot be processed.")
        val identity = ConnectionCache.getIdentityForAsset(name, type)
        val existing = ConnectionCache.getByIdentity(identity)
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
    override fun cacheCreated(list: List<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            val result = ConnectionCache.lookupAssetByGuid(asset.guid, maxRetries = 5)
            result?.let {
                ConnectionCache.addByGuid(asset.guid, result)
            } ?: throw IllegalStateException("Result of searching by GUID for ${asset.guid} was null.")
        }
    }
}
