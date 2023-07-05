/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;

/**
 * Base class for all API endpoints that ultimately access Heka-surfaced services.
 */
public abstract class HekaEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/api/sql";
    private static final String SERVICE = "http://heka-service.heka";

    protected static String getBaseUrl(AtlanClient client) throws ApiConnectionException {
        return getBaseUrl(client, SERVICE, PREFIX);
    }
}
