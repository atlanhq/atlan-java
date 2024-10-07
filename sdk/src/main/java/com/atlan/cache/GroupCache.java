/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.GroupsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.admin.GroupResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal groups into their various IDs.
 */
@Slf4j
public class GroupCache extends AbstractMassCache<AtlanGroup> {

    private volatile Map<String, String> mapAliasToId = new ConcurrentHashMap<>();

    private final GroupsEndpoint groupsEndpoint;

    public GroupCache(GroupsEndpoint groupsEndpoint) {
        this.groupsEndpoint = groupsEndpoint;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of groups...");
        List<AtlanGroup> groups = groupsEndpoint.list();
        mapAliasToId.clear();
        for (AtlanGroup group : groups) {
            String groupId = group.getId();
            String groupName = group.getName();
            cache(groupId, groupName, group);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void cache(String id, String name, AtlanGroup object) {
        super.cache(id, name, object);
        if (object != null) {
            String alias = object.getAlias();
            if (alias != null) {
                mapAliasToId.put(alias, id);
            }
        }
    }

    private String getIdFromAlias(String alias) {
        lock.readLock().lock();
        try {
            return mapAliasToId.get(alias);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Translate the provided human-readable group name to its GUID.
     *
     * @param alias name of the group as it appears in the UI
     * @return unique identifier (GUID) of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getIdForAlias(String alias) throws AtlanException {
        return getIdForAlias(alias, true);
    }

    /**
     * Translate the provided human-readable group name to its GUID.
     *
     * @param alias name of the group as it appears in the UI
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique identifier (GUID) of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getIdForAlias(String alias, boolean allowRefresh) throws AtlanException {
        if (alias != null && !alias.isEmpty()) {
            String groupId = getIdFromAlias(alias);
            if (groupId == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByAlias(alias);
                groupId = getIdFromAlias(alias);
            }
            if (groupId == null) {
                throw new NotFoundException(ErrorCode.GROUP_NOT_FOUND_BY_ALIAS, alias);
            }
            return groupId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ALIAS);
        }
    }

    /**
     * Translate the provided human-readable group name to the internal group name.
     *
     * @param alias name of the group as it appears in the UI
     * @return internal name of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getNameForAlias(String alias) throws AtlanException {
        return getNameForAlias(alias, true);
    }

    /**
     * Translate the provided human-readable group name to the internal group name.
     *
     * @param alias name of the group as it appears in the UI
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return internal name of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getNameForAlias(String alias, boolean allowRefresh) throws AtlanException {
        String guid = getIdForAlias(alias, allowRefresh);
        return getNameForId(guid, false);
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupByName(String name) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"name\":\"" + name + "\"}");
        cacheResponse(response);
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupById(String id) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"id\":\"" + id + "\"}");
        cacheResponse(response);
    }

    /**
     * Wraps a single object lookup for the cache with necessary concurrency controls.
     *
     * @param alias name of the group as it appears in the UI
     * @throws AtlanException on any error communicating with Atlan
     */
    public void cacheByAlias(String alias) throws AtlanException {
        if (bulkRefresh.get()) {
            refresh();
        } else {
            lock.writeLock().lock();
            try {
                lookupByAlias(alias);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Logic to look up a single object for the cache.
     *
     * @param alias name of the group as it appears in the UI
     * @throws AtlanException on any error communicating with Atlan
     */
    protected void lookupByAlias(String alias) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"alias\":\"" + alias + "\"}");
        cacheResponse(response);
    }

    private void cacheResponse(GroupResponse response) {
        if (response != null && response.getRecords() != null) {
            List<AtlanGroup> groups = response.getRecords();
            for (AtlanGroup group : groups) {
                String groupId = group.getId();
                cache(groupId, group.getName(), group);
            }
        }
    }
}
