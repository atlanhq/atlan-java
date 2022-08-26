package com.atlan.cache;

import com.atlan.api.RolesEndpointJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanRoleJ;
import com.atlan.model.responses.RoleResponseJ;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal roles into their various IDs.
 */
@Slf4j
public class RoleCacheJ {

    private static Map<String, AtlanRoleJ> cacheById = new ConcurrentHashMap<>();
    private static Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private static Map<String, String> mapNameToId = new ConcurrentHashMap<>();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of roles...");
        RoleResponseJ response = RolesEndpointJ.getAllRoles();
        List<AtlanRoleJ> roles;
        if (response != null) {
            roles = response.getRecords();
        } else {
            roles = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        for (AtlanRoleJ role : roles) {
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
