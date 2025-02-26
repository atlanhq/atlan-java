/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for reusable components that are common to all caches, where
 * a cache is populated entry-by-entry.
 */
@Slf4j
public abstract class AbstractAssetCache {

    private final Map<String, String> qualifiedName2Guid = new ConcurrentHashMap<>();
    private final Map<ObjectName, String> name2Guid = new ConcurrentHashMap<>();
    private final Map<String, Asset> guid2Asset = new ConcurrentHashMap<>();

    protected final AtlanClient client;

    protected AbstractAssetCache(AtlanClient client) {
        this.client = client;
    }

    /**
     * Logic to refresh the cache for a single object from Atlan.
     *
     * @param guid the unique UUID of a single item to lookup
     * @throws AtlanException on any error communicating with Atlan to lookup the object
     */
    public abstract void lookupByGuid(String guid) throws AtlanException;

    /**
     * Logic to refresh the cache for a single object from Atlan.
     *
     * @param qualifiedName the identity of a single item to lookup
     * @throws AtlanException on any error communicating with Atlan to lookup the object
     */
    public abstract void lookupByQualifiedName(String qualifiedName) throws AtlanException;

    /**
     * Logic to refresh the cache for a single object from Atlan.
     *
     * @param name the name of a single item to lookup
     * @throws AtlanException on any error communicating with Atlan to lookup the object
     */
    public abstract void lookupByName(ObjectName name) throws AtlanException;

    /**
     * Logic to construct a unique identity for the asset.
     *
     * @param asset for which to construct the unique identity
     * @return a unique identity for the asset, or null if there is no asset
     */
    public abstract ObjectName getName(Asset asset);

    /**
     * Add an entry to the cache.
     *
     * @param asset to be cached
     * @return the guid of the asset that was cached, or null if none was provided
     */
    protected String cache(Asset asset) {
        if (asset != null) {
            ObjectName name = getName(asset);
            if (name != null) {
                String guid = asset.getGuid();
                name2Guid.put(name, asset.getGuid());
                guid2Asset.put(guid, asset);
                qualifiedName2Guid.put(asset.getQualifiedName(), guid);
                name2Guid.put(name, guid);
                return guid;
            }
        }
        return null;
    }

    /**
     * Checks whether the provided Atlan-internal UUID is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param guid Atlan-internal UUID of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isGuidKnown(String guid) {
        return guid2Asset.containsKey(guid);
    }

    /**
     * Checks whether the provided Atlan-internal ID string is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param qualifiedName Atlan-internal ID string of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isQualifiedNameKnown(String qualifiedName) {
        return qualifiedName2Guid.containsKey(qualifiedName);
    }

    /**
     * Checks whether the provided human-readable name is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param name human-constructable name of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isNameKnown(ObjectName name) {
        return name2Guid.containsKey(name);
    }

    /**
     * Retrieve an asset from the cache by its UUID, looking it up and
     * adding it to the cache if it is not found there.
     *
     * @param guid UUID of the asset in Atlan
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no UUID was provided for the object to retrieve
     */
    public Asset getByGuid(String guid) throws AtlanException {
        return getByGuid(guid, true);
    }

    /**
     * Retrieve an asset from the cache by its UUID.
     *
     * @param guid UUID of the asset in Atlan
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no UUID was provided for the object to retrieve
     */
    public Asset getByGuid(String guid, boolean allowRefresh) throws AtlanException {
        if (guid != null && !guid.isEmpty()) {
            Asset found = guid2Asset.get(guid);
            if (found == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                lookupByGuid(guid);
                found = guid2Asset.get(guid);
            }
            if (found == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
            }
            return found;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
    }

    /**
     * Retrieve an asset from the cache by its unique Atlan-internal name, looking it up and
     * adding it to the cache if it is not found there.
     *
     * @param qualifiedName unique Atlan-internal name of the asset
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no qualifiedName was provided for the object to retrieve
     */
    public Asset getByQualifiedName(String qualifiedName) throws AtlanException {
        return getByQualifiedName(qualifiedName, true);
    }

    /**
     * Retrieve an asset from the cache by its unique Atlan-internal name.
     *
     * @param qualifiedName unique Atlan-internal name of the asset
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no qualifiedName was provided for the object to retrieve
     */
    public Asset getByQualifiedName(String qualifiedName, boolean allowRefresh) throws AtlanException {
        if (qualifiedName != null && !qualifiedName.isEmpty()) {
            String guid = qualifiedName2Guid.get(qualifiedName);
            if (guid == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                lookupByQualifiedName(qualifiedName);
                guid = qualifiedName2Guid.get(qualifiedName);
            }
            if (guid == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName);
            }
            return getByGuid(guid, false);
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
    }

    /**
     * Retrieve an asset from the cache by its uniquely-identifiable name, looking it up and
     * adding it to the cache if it is not found there.
     *
     * @param name uniquely-identifiable name of the asset in Atlan
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public Asset getByName(ObjectName name) throws AtlanException {
        return getByName(name, true);
    }

    /**
     * Retrieve an asset from the cache by its uniquely-identifiable name.
     *
     * @param name uniquely-identifiable name of the asset in Atlan
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the asset (if found)
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public Asset getByName(ObjectName name, boolean allowRefresh) throws AtlanException {
        if (name != null) {
            String guid = name2Guid.get(name);
            if (guid == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                lookupByName(name);
                guid = name2Guid.get(name);
            }
            if (guid == null) {
                throw new NotFoundException(
                        ErrorCode.ASSET_NOT_FOUND_BY_NAME, this.getClass().getName(), name.toString());
            }
            return getByGuid(guid, false);
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        }
    }
}
