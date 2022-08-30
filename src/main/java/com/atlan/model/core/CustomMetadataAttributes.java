/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Capture the attributes and values for a given set of custom metadata.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("cast")
public class CustomMetadataAttributes extends AtlanObject {

    /**
     * Mapping of custom metadata attributes to values, all by internal IDs.
     */
    @Singular
    private final Map<String, Object> attributes;

    /**
     * Quickly check if there are any custom metadata attributes defined.
     *
     * @return true if there is are no custom metadata attributes defined, false if there are custom metadata attributes defined
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }
}
