/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.GroupsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.atlan.model.admin.AtlanUser;
import com.atlan.model.admin.GroupResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal groups into their various IDs.
 */
@Slf4j
public class GroupCache extends AbstractMassCache<AtlanGroup> {

    private Map<String, String> mapAliasToId = new ConcurrentHashMap<>();

    private final GroupsEndpoint groupsEndpoint;

    public GroupCache(GroupsEndpoint groupsEndpoint) {
        this.groupsEndpoint = groupsEndpoint;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void refreshCache() throws AtlanException {
        super.refreshCache();
        log.debug("Refreshing cache of groups...");
        List<AtlanGroup> groups = groupsEndpoint.list();
        mapAliasToId = new ConcurrentHashMap<>();
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
            String groupId = mapAliasToId.get(alias);
            if (groupId == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                if (bulkRefresh.get()) {
                    refreshCache();
                } else {
                    lookupByAlias(alias);
                }
                groupId = mapAliasToId.get(alias);
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
    public void lookupByName(String name) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"name\":\"" + name + "\"}");
        cacheResponse(response);
    }

    /** {@inheritDoc} */
    @Override
    public void lookupById(String id) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"id\":\"" + id + "\"}");
        cacheResponse(response);
    }

    /**
     * Logic to look up a single object for the cache.
     *
     * @param alias name of the group as it appears in the UI
     * @throws AtlanException on any error communicating with Atlan
     */
    public void lookupByAlias(String alias) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"alias\":\"" + alias + "\"}");
        cacheResponse(response);
    }

    private void cacheResponse(GroupResponse response) {
        if (response != null && response.getRecords() != null && !response.getRecords().isEmpty()) {
            List<AtlanGroup> groups = response.getRecords();
            for (AtlanGroup group : groups) {
                String groupId = group.getId();
                cache(groupId, group.getName(), group);
            }
        }
    }
}
