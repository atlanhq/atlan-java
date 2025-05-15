/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import java.util.Map;

import com.atlan.model.admin.AtlanRequest;
import com.atlan.model.admin.AtlanTagRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Capture the attributes and values for a given set of custom metadata.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class CustomMetadataAttributes extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Mapping of custom metadata attributes to values, all by human-readable names.
     */
    @Singular
    private final Map<String, Object> attributes;

    /**
     * Mapping of archived custom metadata attributes to values, by human-readable names.
     * Note that the names here will all be of the form {@code <name>-archived-123456789}
     */
    @Singular
    private final Map<String, Object> archivedAttributes;

    /**
     * Quickly check if there are any custom metadata attributes defined.
     *
     * @return true if there are no custom metadata attributes defined, false if there are custom metadata attributes defined
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public abstract static class CustomMetadataAttributesBuilder<C extends CustomMetadataAttributes, B extends CustomMetadataAttributesBuilder<C, B>>
        extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
