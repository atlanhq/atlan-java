/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.cache.AbstractMassCache
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.search.IndexSearchResponse
import mu.KotlinLogging
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Utility class for lazy-loading a cache of assets based on some human-constructable identity.
 */
abstract class AssetCache<T : Asset> : AbstractMassCache<T>() {
    private val logger = KotlinLogging.logger {}

    private var preloaded = AtomicBoolean(false)
    private val ignore: MutableMap<String, String?> = ConcurrentHashMap()

    init {
        this.bulkRefresh.set(false)
    }

    /**
     * Initialize the off-heap storage for this cache.
     *
     * @param name of the cache
     * @param response containing the first asset and total count of existing assets for the cache
     * @param exemplar representative asset to use for space estimates, in case there are currently no assets in the tenant
     */
    @Suppress("UNCHECKED_CAST")
    fun initializeOffHeap(
        name: String,
        response: IndexSearchResponse?,
        exemplar: T,
    ) {
        val first = response?.assets?.get(0)
        initializeOffHeap(
            name,
            response?.approximateCount?.toInt() ?: 0,
            if (first != null) first as T else exemplar,
            exemplar.javaClass,
        )
    }

    /**
     * Retrieve an asset from the cache by its human-readable identity, lazily-loading it on any cache misses.
     *
     * @param identity of the asset to retrieve
     * @return the asset with the specified identity, or null if no such asset is in the cache
     */
    fun getByIdentity(identity: String): T? {
        if (ignore.containsKey(identity)) return null
        if (!containsIdentity(identity)) lookupByName(identity)
        return getByName(identity, false) as T
    }

    /**
     * Retrieve an asset from the cache by its globally-unique identifier, lazily-loading it on any cache misses.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @return the asset with the specified GUID, or null if no such asset is in the cache
     */
    fun getByGuid(guid: String): T? {
        if (ignore.containsKey(guid)) return null
        if (!containsGuid(guid)) lookupById(guid)
        return getById(guid, false) as T
    }

    /**
     * Retrieve the unique identity of an asset from the cache by its globally-unique identifier.
     *
     * @param guid unique identifier (GUID) of the asset to retrieve
     * @return the identity of the specified GUID, or null if no identity could be found
     */
    fun getIdentity(guid: String): String? {
        return getNameFromId(guid)
    }

    /**
     * Indicates whether the cache already contains an asset with a given identity.
     *
     * @param identity of the asset to check for presence in the cache
     * @return true if this identity is already in the cache, false otherwise
     */
    fun containsIdentity(identity: String): Boolean {
        return isNameKnown(identity)
    }

    /**
     * Indicates whether the cache already contains an asset with a given GUID.
     *
     * @param guid unique identifier (GUID) of the asset to check for presence in the cache
     * @return true if this GUID is already in the cache, false otherwise
     */
    fun containsGuid(guid: String): Boolean {
        return isIdKnown(guid)
    }

    /**
     * Mark the provided asset identity as one to ignore.
     *
     * @param id any identity for the asset, either GUID or string identity
     */
    protected fun addToIgnore(id: String) {
        ignore[id] = id
    }

    /**
     * List all the assets held in the cache.
     *
     * @return the set of all assets in the cache
     */
    protected fun listAll(): Set<Map.Entry<UUID, T>> {
        return entrySet()
    }

    /**
     * Check whether the asset is archived, and if so mark it to be ignored.
     *
     * @param id any identity for the asset, either GUID or string identity
     * @param asset the asset to check
     * @return true if the asset is archived, false otherwise
     */
    private fun isArchived(
        id: String,
        asset: T,
    ): Boolean {
        return if (asset.status != AtlanStatus.ACTIVE) {
            logger.warn { "Unable to cache archived asset: $id" }
            addToIgnore(id)
            true
        } else {
            false
        }
    }

    /**
     * Create a unique, reconstructable identity for the provided asset.
     *
     * @param asset for which to construct the identity
     * @return the identity of the asset
     */
    abstract fun getIdentityForAsset(asset: T): String

    /** Preload the cache (will only act once, in case called multiple times on the same cache) */
    @Synchronized
    fun preload() {
        if (!preloaded.get()) {
            refreshCache()
            preloaded.set(true)
        }
    }
}
