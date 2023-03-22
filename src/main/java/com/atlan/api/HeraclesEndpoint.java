/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.ApiConnectionException;

/**
 * Base class for all API endpoints that ultimately access Heracles-surfaced services.
 */
public abstract class HeraclesEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/api/service";
    private static final String SERVICE = "http://heracles-service.heracles";

    protected static String getBaseUrl() throws ApiConnectionException {
        return getBaseUrl(SERVICE, PREFIX);
    }
}
