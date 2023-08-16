/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal groups into their various IDs.
 */
@Slf4j
public class GroupCache {

    private Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private Map<String, String> mapAliasToId = new ConcurrentHashMap<>();

    private synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of groups...");
        List<AtlanGroup> groups = AtlanGroup.list();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        mapAliasToId = new ConcurrentHashMap<>();
        for (AtlanGroup group : groups) {
            String groupId = group.getId();
            String groupName = group.getName();
            String groupAlias = group.getAlias();
            mapIdToName.put(groupId, groupName);
            mapNameToId.put(groupName, groupId);
            mapAliasToId.put(groupAlias, groupId);
        }
    }

    /**
     * Translate the provided human-readable group name to its GUID.
     *
     * @param name human-readable name of the group
     * @return unique identifier (GUID) of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getIdForName(String name) throws AtlanException {
        if (name != null && !name.isEmpty()) {
            String groupId = mapNameToId.get(name);
            if (groupId == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                groupId = mapNameToId.get(name);
                if (groupId == null) {
                    throw new NotFoundException(ErrorCode.GROUP_NOT_FOUND_BY_NAME, name);
                }
            }
            return groupId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_NAME);
        }
    }

    /**
     * Translate the provided alias to its GUID.
     *
     * @param alias name of the group as it appears in the UI
     * @return unique identifier (GUID) of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getIdForAlias(String alias) throws AtlanException {
        if (alias != null && !alias.isEmpty()) {
            String groupId = mapAliasToId.get(alias);
            if (groupId == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                groupId = mapAliasToId.get(alias);
                if (groupId == null) {
                    throw new NotFoundException(ErrorCode.GROUP_NOT_FOUND_BY_ALIAS, alias);
                }
            }
            return groupId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ALIAS);
        }
    }

    /**
     * Translate the provided group GUID to the human-readable group name.
     *
     * @param id unique identifier (GUID) of the group
     * @return human-readable name of the group
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the group cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the group to retrieve
     */
    public String getNameForId(String id) throws AtlanException {
        if (id != null && !id.isEmpty()) {
            String groupName = mapIdToName.get(id);
            if (groupName == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                groupName = mapIdToName.get(id);
                if (groupName == null) {
                    throw new NotFoundException(ErrorCode.GROUP_NOT_FOUND_BY_ID, id);
                }
            }
            return groupName;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_GROUP_ID);
        }
    }
}
