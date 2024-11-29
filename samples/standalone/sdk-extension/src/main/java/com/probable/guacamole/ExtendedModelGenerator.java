/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole;

import com.atlan.AtlanClient;

public abstract class ExtendedModelGenerator {
    protected static final String SERVICE_TYPE = "guacamole";
    protected static final String TYPE_PREFIX = "Guacamole";
    protected static final String PACKAGE_ROOT = "com.probable.guacamole.model";

    protected final AtlanClient client;

    public ExtendedModelGenerator(AtlanClient client) {
        this.client = client;
    }
}
