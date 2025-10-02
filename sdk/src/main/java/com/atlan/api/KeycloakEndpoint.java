/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;

/**
 * Base class for all API endpoints that ultimately access Keycloak-backed services.
 */
public abstract class KeycloakEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/auth";
    private static final String SERVICE = "http://keycloak-http.keycloak.svc.cluster.local";

    protected KeycloakEndpoint(AtlanClient client) {
        super(client);
    }

    protected String getBaseUrl() throws ApiConnectionException {
        return getBaseUrl(SERVICE, PREFIX);
    }
}
