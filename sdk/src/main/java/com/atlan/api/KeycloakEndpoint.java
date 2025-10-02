/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.ErrorCode;

/**
 * Base class for all API endpoints that ultimately access Keycloak-backed services.
 */
public abstract class KeycloakEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/auth/admin";
    private static final String INTERNAL_PREFIX = "/auth";
    private static final String SERVICE = "http://keycloak-http.keycloak.svc.cluster.local";

    protected KeycloakEndpoint(AtlanClient client) {
        super(client);
    }

    protected String getBaseUrl() throws ApiConnectionException {
        if (client.isInternal()) {
            return getBaseUrl(SERVICE, "") + INTERNAL_PREFIX;
        }
        return getBaseUrl(SERVICE, PREFIX);
    }
}
