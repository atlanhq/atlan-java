/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.api.GroupsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.admin.GroupResponse;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal groups into their various IDs.
 */
@Slf4j
public class GroupCache extends AbstractMassCache<AtlanGroup> {

    private final GroupsEndpoint groupsEndpoint;

    public GroupCache(AtlanClient client) {
        super(client, "group");
        this.groupsEndpoint = client.groups;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of groups...");
        List<AtlanGroup> groups = groupsEndpoint.list();
        for (AtlanGroup group : groups) {
            String groupId = group.getId();
            String groupName = group.getName();
            cache(groupId, group.getAlias(), groupName, group);
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
        return getIdForSid(alias, allowRefresh);
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
        return getNameForSid(alias, allowRefresh);
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

    /** {@inheritDoc} */
    @Override
    protected void lookupBySid(String alias) throws AtlanException {
        GroupResponse response = groupsEndpoint.list("{\"alias\":\"" + alias + "\"}");
        cacheResponse(response);
    }

    private void cacheResponse(GroupResponse response) {
        if (response != null && response.getRecords() != null) {
            List<AtlanGroup> groups = response.getRecords();
            for (AtlanGroup group : groups) {
                String groupId = group.getId();
                cache(groupId, group.getAlias(), group.getName(), group);
            }
        }
    }
}
