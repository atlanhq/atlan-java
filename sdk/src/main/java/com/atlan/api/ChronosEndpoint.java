/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;

/**
 * Base class for all API endpoints that ultimately access Chronos-surfaced services.
 */
public abstract class ChronosEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/events/openlineage";
    private static final String SERVICE = "http://chronos-service.kong.svc.cluster.local";

    protected ChronosEndpoint(AtlanClient client) {
        super(client);
    }

    protected String getBaseUrl() throws ApiConnectionException {
        return getBaseUrl(SERVICE, PREFIX);
    }
}
