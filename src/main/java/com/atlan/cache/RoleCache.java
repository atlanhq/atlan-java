/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.RolesEndpoint;
import com.atlan.exception.AtlanException;
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
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public static String getIdForName(String name) throws AtlanException {
        if (name != null && name.length() > 0) {
            String roleId = mapNameToId.get(name);
            if (roleId == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                roleId = mapNameToId.get(name);
                if (roleId == null) {
                    throw new NotFoundException(
                            "Role with name '" + name + "' does not exist.", "ATLAN_JAVA_404_703", null);
                }
            }
            return roleId;
        } else {
            throw new InvalidRequestException(
                    "No name was provided when attempting to retrieve a role.", "name", "ATLAN_JAVA_400_703", null);
        }
    }

    /**
     * Translate the provided role GUID to the human-readable role name.
     * @param id GUID of the role
     * @return human-readable name of the role
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the role cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the role to retrieve
     */
    public static String getNameForId(String id) throws AtlanException {
        if (id != null && id.length() > 0) {
            String roleName = mapIdToName.get(id);
            if (roleName == null) {
                // If not found, refresh the cache and look again (could be stale)
                refreshCache();
                roleName = mapIdToName.get(id);
                if (roleName == null) {
                    throw new NotFoundException(
                            "Role with ID '" + id + "' does not exist.", "ATLAN_JAVA_404_704", null);
                }
            }
            return roleName;
        } else {
            throw new InvalidRequestException(
                    "No ID was provided when attempting to retrieve a role.", "id", "ATLAN_JAVA_400_704", null);
        }
    }
}
