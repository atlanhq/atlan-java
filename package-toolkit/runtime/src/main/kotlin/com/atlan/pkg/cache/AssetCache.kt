/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import java.util.concurrent.ConcurrentHashMap

/**
 * Utility class for lazy-loading a cache of assets based on some human-constructable identity.
 */
abstract class AssetCache {
    private val byIdentity: MutableMap<String, String?> = ConcurrentHashMap()
    private val byGuid: MutableMap<String, Asset?> = ConcurrentHashMap()

    /**
     * Retrieve an asset from the cache by its human-readable identity, lazily-loading it on any cache misses.
     *
     * @param identity of the asset to retrieve
     * @return the asset with the specified identity
     */
    fun getByIdentity(identity: String): Asset? {
        if (!this.containsIdentity(identity)) {
            val asset = lookupAssetByIdentity(identity)!!
            byIdentity[identity] = asset.guid
            byGuid[asset.guid] = asset
        }
        return byGuid[byIdentity[identity]]
    }

    /**
     * Retrieve an asset from the cache by its globally-unique identifier, lazily-loading it on any cache misses.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @return the asset with the specified GUID
     */
    fun getByGuid(guid: String): Asset? {
        if (!this.containsGuid(guid)) {
            val asset = lookupAssetByGuid(guid)
            if (asset != null) {
                byIdentity[getIdentityForAsset(asset)] = guid
                byGuid[guid] = asset
            }
        }
        return byGuid.getOrDefault(guid, null)
    }

    /**
     * Indicates whether the cache already contains an asset with a given identity.
     *
     * @param identity of the asset to check for presence in the cache
     * @return true if this identity is already in the cache, false otherwise
     */
    fun containsIdentity(identity: String): Boolean {
        return byIdentity.containsKey(identity)
    }

    /**
     * Indicates whether the cache already contains an asset with a given GUID.
     *
     * @param guid unique identifier (GUID) of the asset to check for presence in the cache
     * @return true if this GUID is already in the cache, false otherwise
     */
    fun containsGuid(guid: String): Boolean {
        return byGuid.containsKey(guid)
    }

    /**
     * Actually go to Atlan and find the asset with the provided identity.
     * Note: this should also populate the byGuid cache
     *
     * @param identity of the asset to lookup
     * @return the asset, from Atlan
     */
    protected abstract fun lookupAssetByIdentity(identity: String?): Asset?

    /**
     * Actually go to Atlan and find the asset with the provided GUID.
     * Note: this should also populate the byIdentity cache
     *
     * @param guid unique identifier (GUID) of the asset to lookup
     * @return the asset, from Atlan
     */
    protected abstract fun lookupAssetByGuid(guid: String?): Asset?

    /**
     * Create a unique, reconstructable identity for the provided asset.
     *
     * @param asset for which to construct the identity
     * @return the identity of the asset
     */
    protected abstract fun getIdentityForAsset(asset: Asset): String
}
