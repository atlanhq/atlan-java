/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.api.RolesEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanRole;
import com.atlan.model.admin.RoleResponse;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating Atlan-internal roles into their various IDs.
 */
@Slf4j
public class RoleCache extends AbstractMassCache<AtlanRole> {

    private final RolesEndpoint rolesEndpoint;

    public RoleCache(AtlanClient client) {
        super(client, "role");
        this.rolesEndpoint = client.roles;
    }

    /** {@inheritDoc} */
    @Override
    protected void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of roles...");
        // Note: we will only retrieve and cache the workspace-level roles, which all
        // start with '$'
        RoleResponse response = rolesEndpoint.list("{\"name\":{\"$ilike\":\"$%\"}}");
        if (response == null || response.getRecords().isEmpty()) {
            throw new NotFoundException(ErrorCode.ROLES_NOT_FOUND);
        }
        cacheResponse(response);
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupById(String id) throws AtlanException {
        RoleResponse response = rolesEndpoint.list("{\"id\":\"" + id + "\"}");
        cacheResponse(response);
    }

    /** {@inheritDoc} */
    @Override
    protected void lookupByName(String name) throws AtlanException {
        RoleResponse response = rolesEndpoint.list("{\"name\":\"" + name + "\"}");
        cacheResponse(response);
    }

    private void cacheResponse(RoleResponse response) {
        List<AtlanRole> roles;
        if (response != null) {
            roles = response.getRecords();
        } else {
            roles = Collections.emptyList();
        }
        for (AtlanRole role : roles) {
            cache(role.getId(), role.getName(), role.getDescription(), role);
        }
    }
}
