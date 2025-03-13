/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AtlanObject;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for reusable components that are common to all caches, where
 * a cache is populated en-masse through batch refreshing and furthermore
 * where any cache misses are also tracked (to avoid attempting to refresh
 * the cache unnecessarily).
 */
public abstract class AbstractMassTrackingCache<T extends AtlanObject> extends AbstractMassCache<T> {

    private volatile Set<String> deletedSids = ConcurrentHashMap.newKeySet();
    private volatile Set<String> deletedNames = ConcurrentHashMap.newKeySet();

    public AbstractMassTrackingCache(AtlanClient client, String name) {
        super(client, name);
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        deletedSids.clear();
        deletedNames.clear();
    }

    /**
     * Consider whether to refresh the cache, based on when it was last refreshed.
     *
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    protected void refresh(long minimumTime) throws AtlanException {
        if (minimumTime == Long.MAX_VALUE || minimumTime < lastRefresh.get()) {
            forceRefresh();
        }
    }

    /**
     * Check whether the provided name has already been tracked as deleted.
     *
     * @param name to check is known to be deleted
     * @return true if the name is known to be deleted
     */
    protected boolean isDeletedName(String name) {
        lock.readLock().lock();
        try {
            return deletedNames.contains(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Add the provided name for tracking as deleted.
     *
     * @param name to mark as known-deleted
     */
    protected void addDeletedName(String name) {
        lock.writeLock().lock();
        try {
            deletedNames.add(name);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Check whether the provided ID has already been tracked as deleted.
     *
     * @param id to check is known to be deleted
     * @return true if the ID is known to be deleted
     */
    protected boolean isDeletedId(String id) {
        lock.readLock().lock();
        try {
            return deletedSids.contains(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Add the provided ID for tracking as deleted.
     *
     * @param id to mark as known-deleted
     */
    protected void addDeletedId(String id) {
        lock.writeLock().lock();
        try {
            deletedSids.add(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal ID string.
     *
     * @param name human-readable name of the object
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getIdForName(String name, long minimumTime) throws AtlanException {
        if (name != null && isDeletedName(name)) {
            return null;
        }
        try {
            return super.getIdForName(name, minimumTime == Long.MAX_VALUE || minimumTime < lastRefresh.get());
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedName(name);
            throw e;
        }
    }

    /**
     * Translate the provided Atlan-internal ID string to the human-readable name for the object.
     *
     * @param id Atlan-internal ID string
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForId(String id, long minimumTime) throws AtlanException {
        if (id != null && isDeletedId(id)) {
            return null;
        }
        try {
            return super.getNameForId(id, minimumTime == Long.MAX_VALUE || minimumTime < lastRefresh.get());
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedId(id);
            throw e;
        }
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal secondary ID string.
     *
     * @param name human-readable name of the object
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return unique Atlan-internal secondary ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getSidForName(String name, long minimumTime) throws AtlanException {
        if (name != null && isDeletedName(name)) {
            return null;
        }
        try {
            return super.getSidForName(name, minimumTime == Long.MAX_VALUE || minimumTime < lastRefresh.get());
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedName(name);
            throw e;
        }
    }

    /**
     * Translate the provided Atlan-internal secondary ID string to the human-readable name for the object.
     *
     * @param sid Atlan-internal secondary ID string
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return human-readable name of the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForSid(String sid, long minimumTime) throws AtlanException {
        if (sid != null && isDeletedId(sid)) {
            return null;
        }
        try {
            return super.getNameForSid(sid, minimumTime == Long.MAX_VALUE || minimumTime < lastRefresh.get());
        } catch (NotFoundException e) {
            // If it's not already marked deleted, mark it as deleted
            addDeletedId(sid);
            throw e;
        }
    }
}
