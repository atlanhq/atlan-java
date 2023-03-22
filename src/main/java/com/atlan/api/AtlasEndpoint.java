/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.ApiConnectionException;

/**
 * Base class for all API endpoints that ultimately access the underlying metastore.
 */
public abstract class AtlasEndpoint extends AbstractEndpoint {
    private static final String PREFIX = "/api/meta";
    private static final String SERVICE = "http://atlas-service-atlas.atlas/api/atlas/v2";

    protected static String getBaseUrl() throws ApiConnectionException {
        return getBaseUrl(SERVICE, PREFIX);
    }
}
