/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole;

import com.atlan.Atlan;

public abstract class ExtendedModelGenerator {
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    protected static final String SERVICE_TYPE = "guacamole";
    protected static final String TYPE_PREFIX = "Guacamole";
    protected static final String PACKAGE_ROOT = "com.probable.guacamole.model";
}
