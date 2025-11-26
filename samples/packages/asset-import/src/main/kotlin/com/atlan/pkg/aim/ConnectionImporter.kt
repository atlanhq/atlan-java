/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanConnectionCategory
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.AssetRefXformer.getDeferredIdentity
import com.atlan.pkg.serde.csv.ImportResults
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
) : AbstractBaseImporter(
        ctx = ctx,
        filename = inputFile,
        logger = logger,
        typeNameFilter = Connection.TYPE_NAME,
        attrsToOverwrite =
            attributesToClear(
                ctx.config
                    .getEffectiveValue(
                        AssetImportCfg::assetsAttrToOverwrite,
                        AssetImportCfg::assetsConfig,
                    ).toMutableList(),
                "assets",
                logger,
            ),
        creationHandling = if (ctx.config.assetsUpsertSemantic == "update") AssetCreationHandling.NONE else AssetCreationHandling.FULL,
        batchSize = 1,
        trackBatches = true,
        fieldSeparator =
            ctx.config.getEffectiveValue(
                AssetImportCfg::assetsFieldSeparator,
                AssetImportCfg::assetsConfig,
            )[0],
    ) {
    companion object {
        const val CONNECTOR_TYPE = "connectorType"
        const val CUSTOM_CONNECTOR_TYPE = "customConnectorType"
    }

    private val secondPassRemain =
        setOf(
            Asset.QUALIFIED_NAME.atlanFieldName,
            Asset.NAME.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun validateHeader(header: List<String>?): List<String> {
        val missing = super.validateHeader(header).toMutableList()
        if (header.isNullOrEmpty()) {
            missing.add(Asset.QUALIFIED_NAME.atlanFieldName)
        } else {
            if (!header.contains(Asset.QUALIFIED_NAME.atlanFieldName)) {
                missing.add(Asset.QUALIFIED_NAME.atlanFieldName)
            }
        }
        return missing
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(Connection.QUALIFIED_NAME.atlanFieldName)
        return super.import(typeNameFilter, colsToSkip, secondPassRemain)
    }

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Connection.NAME.atlanFieldName)?.let { it as String } ?: ""
        // Note: we need the UNPROCESSED qualifiedName here (otherwise it'll try to resolve a potentially-deferred
        //  QN before we've had any chance to see whether it exists or not)
        val qualifiedName = deserializer.qualifiedName
        val cName = deserializer.getValue(Connection.CONNECTOR_NAME.atlanFieldName)?.let { it as String }
        val cType = deserializer.getValue(CONNECTOR_TYPE)?.let { it as AtlanConnectorType }
        val customType = deserializer.getValue(CUSTOM_CONNECTOR_TYPE)?.let { it as String }
        val deferredIdentity = getDeferredIdentity(qualifiedName)
        val resolvedType =
            if (qualifiedName.isEmpty()) {
                customType ?: if (cType == null || cType == AtlanConnectorType.UNKNOWN_CUSTOM) cName else cType.value
            } else {
                deferredIdentity?.type ?: Connection.getConnectorFromQualifiedName(qualifiedName)
            }
        if (resolvedType == null || resolvedType.isEmpty()) {
            throw NoSuchElementException("Invalid connectorType provided for the connection, cannot be processed: $qualifiedName (name: $name)")
        }
        val resolvedName = deferredIdentity?.name ?: name
        val identity = ctx.connectionCache.getIdentityForAsset(resolvedName, resolvedType)
        val existing = ctx.connectionCache.getByIdentity(identity)
        return if (existing != null) {
            existing.trimToRequired()
        } else {
            val users = deserializer.getValue(Connection.ADMIN_USERS.atlanFieldName)?.let { it as List<String> }
            val groups = deserializer.getValue(Connection.ADMIN_GROUPS.atlanFieldName)?.let { it as List<String> }
            val roles = deserializer.getValue(Connection.ADMIN_ROLES.atlanFieldName)?.let { it as List<String> }
            val ct = AtlanConnectorType.fromValue(resolvedType)
            if (ct != null && ct != AtlanConnectorType.UNKNOWN_CUSTOM) {
                Connection.creator(ctx.client, resolvedName, ct, roles, groups, users)
            } else {
                val category = deserializer.getValue(Connection.CATEGORY.atlanFieldName)?.let { it as AtlanConnectionCategory }
                Connection.creator(ctx.client, resolvedName, resolvedType, category, roles, groups, users)
            }
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
