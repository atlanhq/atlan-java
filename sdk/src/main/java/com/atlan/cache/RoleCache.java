/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.RolesEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanRole;
import com.atlan.model.admin.RoleResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal roles into their various IDs.
 */
@Slf4j
public class RoleCache {

    private Map<String, AtlanRole> cacheById = new ConcurrentHashMap<>();
    private Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private Map<String, String> mapNameToId = new ConcurrentHashMap<>();

    private final RolesEndpoint rolesEndpoint;

    public RoleCache(RolesEndpoint rolesEndpoint) {
        this.rolesEndpoint = rolesEndpoint;
    }

    public synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of roles...");
        // Note: we will only retrieve and cache the workspace-level roles, which all
        // start with '$'
        RoleResponse response = rolesEndpoint.list("{\"name\":{\"$ilike\":\"$%\"}}");
        List<AtlanRole> roles;
        if (response != null) {
            roles = response.getRecords();
        } else {
            roles = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        for (AtlanRole role : roles) {
            String roleId = role.getId();
            String roleName = role.getName();
            cacheById.put(roleId, role);
            mapIdToName.put(roleId, roleName);
            mapNameToId.put(roleName, roleId);
        }
    }

    /**
     * Translate the provided human-readable role name to its GUID.
     *
     * @param name human-readable name of the role
     * @return unique identifier (GUID) of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public String getIdForName(String name) throws AtlanException {
        return getIdForName(name, true);
    }

    /**
     * Translate the provided human-readable role name to its GUID.
     *
     * @param name human-readable name of the role
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique identifier (GUID) of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public String getIdForName(String name, boolean allowRefresh) throws AtlanException {
        if (name != null && !name.isEmpty()) {
            String roleId = mapNameToId.get(name);
            if (roleId == null) {
                // If not found, refresh the cache and look again (could be stale)
                if (allowRefresh) {
                    refreshCache();
                    roleId = mapNameToId.get(name);
                }
                if (roleId == null) {
                    throw new NotFoundException(ErrorCode.ROLE_NOT_FOUND_BY_NAME, name);
                }
            }
            return roleId;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ROLE_NAME);
        }
    }

    /**
     * Translate the provided role GUID to the human-readable role name.
     *
     * @param id unique identifier (GUID) of the role
     * @return human-readable name of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public String getNameForId(String id) throws AtlanException {
        return getNameForId(id, true);
    }

    /**
     * Translate the provided role GUID to the human-readable role name.
     *
     * @param id unique identifier (GUID) of the role
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return human-readable name of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public String getNameForId(String id, boolean allowRefresh) throws AtlanException {
        if (id != null && !id.isEmpty()) {
            String roleName = mapIdToName.get(id);
            if (roleName == null) {
                // If not found, refresh the cache and look again (could be stale)
                if (allowRefresh) {
                    refreshCache();
                    roleName = mapIdToName.get(id);
                }
                if (roleName == null) {
                    throw new NotFoundException(ErrorCode.ROLE_NOT_FOUND_BY_ID, id);
                }
            }
            return roleName;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ROLE_ID);
        }
    }
}
