/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.RolesEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanRole;
import com.atlan.model.admin.RoleResponse;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal roles into their various IDs.
 */
@Slf4j
public class RoleCache extends AbstractCache {

    private final RolesEndpoint rolesEndpoint;

    public RoleCache(RolesEndpoint rolesEndpoint) {
        this.rolesEndpoint = rolesEndpoint;
    }

    /** {@inheritDoc} */
    @Override
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
        for (AtlanRole role : roles) {
            String roleId = role.getId();
            String roleName = role.getName();
            cache(roleId, roleName);
        }
    }
}
