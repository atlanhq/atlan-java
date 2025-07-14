/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.pkg.PackageContext

/**
 * Static object to transform connection references.
 */
object ConnectionXformer {
    const val CONNECTION_DELIMITER = "@@@"

    /**
     * Encodes (serializes) a connection reference into a string form.
     *
     * @param ctx context in which the package is running
     * @param asset to be encoded
     * @return the string-encoded form for that asset
     */
    fun encode(
        ctx: PackageContext<*>,
        asset: Asset,
    ): String {
        return when (asset) {
            is Connection -> {
                val connection = ctx.connectionCache.getByGuid(asset.guid)
                if (connection is Connection) {
                    return encode(connection.name ?: "", connection.connectorName ?: "")
                } else {
                    ""
                }
            }
            else -> AssetRefXformer.encode(ctx, asset)
        }
    }

    /**
     * Encodes (serializes) a connection reference into a string form.
     *
     * @param name of the connection
     * @param type of the connector for the connection (string value)
     * @return the string-encoded form for that asset
     */
    fun encode(
        name: String,
        type: String,
    ): String = "$name$CONNECTION_DELIMITER$type"

    /**
     * Decodes (deserializes) a string form into a connection reference object.
     *
     * @param ctx context in which the package is running
     * @param assetRef the string form to be decoded
     * @param fieldName the name of the field containing the string-encoded value
     * @return the connection reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        assetRef: String,
        fieldName: String,
    ): Asset =
        when (fieldName) {
            "connection" -> {
                ctx.connectionCache.getByIdentity(assetRef)
                    ?: throw NoSuchElementException("Connection not found (in $fieldName): $assetRef")
            }
            else -> AssetRefXformer.decode(ctx, assetRef, fieldName)
        }
}
