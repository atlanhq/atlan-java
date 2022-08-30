/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.RolesEndpoint;
import com.atlan.exception.AtlanException;
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

    private static Map<String, AtlanRole> cacheById = new ConcurrentHashMap<>();
    private static Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private static Map<String, String> mapNameToId = new ConcurrentHashMap<>();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of roles...");
        RoleResponse response = RolesEndpoint.getAllRoles();
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
     * @param name human-readable name of the role
     * @return GUID of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getIdForName(String name) throws AtlanException {
        String roleId = mapNameToId.get(name);
        if (roleId != null) {
            // If found, return straight away
            return roleId;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapNameToId.get(name);
        }
    }

    /**
     * Translate the provided role GUID to the human-readable role name.
     * @param id GUID of the role
     * @return human-readable name of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getNameForId(String id) throws AtlanException {
        String roleName = mapIdToName.get(id);
        if (roleName != null) {
            // If found, return straight away
            return roleName;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapIdToName.get(id);
        }
    }
}
