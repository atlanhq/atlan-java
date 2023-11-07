/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;

public abstract class AbstractEndpoint {
    protected final AtlanClient client;

    protected AbstractEndpoint(AtlanClient client) {
        this.client = client;
    }

    /**
     * Retrieve the base URL to use to access the endpoint.
     *
     * @param service the internal service used to access the endpoint
     * @param prefix the prefix used for routing external access to the endpoint
     * @return the base URL (including any prefix) to use to access the API endpoint
     * @throws ApiConnectionException if no base URL has been defined, and the SDK has not been configured for internal access
     */
    protected String getBaseUrl(String service, String prefix) throws ApiConnectionException {
        return client.isInternal() ? service : client.getBaseUrl() + prefix;
    }
}
