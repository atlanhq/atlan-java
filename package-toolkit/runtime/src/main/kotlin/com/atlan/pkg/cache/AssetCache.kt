/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanStatus
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

/**
 * Utility class for lazy-loading a cache of assets based on some human-constructable identity.
 */
abstract class AssetCache {
    private val logger = KotlinLogging.logger {}

    private val byIdentity: MutableMap<String, String?> = ConcurrentHashMap()
    private val toIdentity: MutableMap<String, String?> = ConcurrentHashMap()
    private val byGuid: MutableMap<String, Asset?> = ConcurrentHashMap()
    private val ignore: MutableMap<String, String?> = ConcurrentHashMap()

    /**
     * Retrieve an asset from the cache by its human-readable identity, lazily-loading it on any cache misses.
     *
     * @param identity of the asset to retrieve
     * @return the asset with the specified identity
     */
    fun getByIdentity(identity: String): Asset? {
        if (this.ignore.containsKey(identity)) {
            return null
        }
        if (!this.containsIdentity(identity)) {
            val asset = lookupAssetByIdentity(identity)
            asset?.let {
                addByIdentity(identity, asset)
            }
        }
        return if (byIdentity.containsKey(identity)) {
            byGuid.getOrDefault(byIdentity[identity], null)
        } else {
            null
        }
    }

    /**
     * Retrieve an asset from the cache by its globally-unique identifier, lazily-loading it on any cache misses.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @param maxRetries maximum number of times to retry the lookup if not found immediately
     * @return the asset with the specified GUID
     */
    fun getByGuid(guid: String, maxRetries: Int = 0): Asset? {
        if (this.ignore.containsKey(guid)) {
            return null
        }
        if (!this.containsGuid(guid)) {
            val asset = lookupAssetByGuid(guid, maxRetries)
            asset?.let {
                addByGuid(guid, asset)
            }
        }
        return byGuid.getOrDefault(guid, null)
    }

    /**
     * Retrieve the unique identity of an asset from the cache by its globally-unique identifier.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @return the identity of the specified GUID, or null if no identity could be found
     */
    fun getIdentity(guid: String): String? {
        return toIdentity.getOrDefault(guid, null)
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
     * Add an asset to the cache using its GUID.
     *
     * @param guid unique identifier (GUID) of the asset
     * @param asset to cache
     */
    fun addByGuid(guid: String, asset: Asset?) {
        if (asset != null && !isArchived(guid, asset)) {
            val identity = getIdentityForAsset(asset)
            byIdentity[identity] = guid
            toIdentity[guid] = identity
            byGuid[guid] = asset
            ignore.remove(guid)
            ignore.remove(identity)
        }
    }

    /**
     * Add an asset to the cache using its identity.
     *
     * @param identity of the asset
     * @param asset to cache
     */
    fun addByIdentity(identity: String, asset: Asset?) {
        if (asset != null && !isArchived(identity, asset)) {
            byIdentity[identity] = asset.guid
            toIdentity[asset.guid] = identity
            byGuid[asset.guid] = asset
            ignore.remove(identity)
            ignore.remove(asset.guid)
        }
    }

    /**
     * Check whether the asset is archived, and if so mark it to be ignored.
     *
     * @param id any identity for the asset, either GUID or string identity
     * @param asset the asset to check
     */
    private fun isArchived(id: String, asset: Asset): Boolean {
        return if (asset.status != AtlanStatus.ACTIVE) {
            logger.warn("Unable to cache archived asset: {}", id)
            ignore[id] = id
            true
        } else {
            false
        }
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
     * @param currentAttempt current retry attempt, if not found on first lookup
     * @param maxRetries maximum number of retries to attempt if not found on first lookup
     * @return the asset, from Atlan
     */
    abstract fun lookupAssetByGuid(guid: String?, currentAttempt: Int = 0, maxRetries: Int = 0): Asset?

    /**
     * Create a unique, reconstructable identity for the provided asset.
     *
     * @param asset for which to construct the identity
     * @return the identity of the asset
     */
    abstract fun getIdentityForAsset(asset: Asset): String

    /**
     * Preload the cache, by bulk-retrieving all assets up-front.
     */
    abstract fun preload()
}
