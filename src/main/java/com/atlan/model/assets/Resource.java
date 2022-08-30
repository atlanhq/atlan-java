/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about resource-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Resource extends Asset {

    public static final String TYPE_NAME = "Resource";

    /**
     * TBC
     */
    @Attribute
    String link;

    /**
     * TBC
     */
    @Attribute
    Boolean isGlobal;

    /**
     * TBC
     */
    @Attribute
    String reference;
}
