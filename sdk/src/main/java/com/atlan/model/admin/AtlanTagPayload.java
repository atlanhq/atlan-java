/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Captures the details of an Atlan tag in a request.
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AtlanTagPayload extends AtlanRequestPayload {
    private static final long serialVersionUID = 2L;

    /** Name of the Atlan tag. */
    String typeName;

    /** Whether the Atlan tag should propagate. */
    @Builder.Default
    Boolean propagate = true;

    /** Whether removing the Atlan tag should also remove its propagations. */
    @Builder.Default
    Boolean removePropagationsOnEntityDelete = false;

    /** Unused. */
    List<Object> validityPeriods;

    /**
     * Create a Atlan tag payload with the specified Atlan tag and defaults for propagation.
     *
     * @param atlanTagName human-readable name of the Atlan tag
     * @return the Atlan tag payload with defaults for propagation
     */
    public static AtlanTagPayload of(String atlanTagName) {
        return AtlanTagPayload.builder().typeName(atlanTagName).build();
    }

    /**
     * Create an Atlan tag payload with the specified details.
     *
     * @param atlanTagName human-readable name of the Atlan tag
     * @param propagate whether the Atlan tag should propagate (true) or not (false)
     * @param removePropagationsOnDelete whether the propagated Atlan tags should be cascaded (true) or not (false)
     * @return the Atlan tag payload with the specified propagation settings
     */
    public static AtlanTagPayload of(String atlanTagName, boolean propagate, boolean removePropagationsOnDelete) {
        return AtlanTagPayload.builder()
                .typeName(atlanTagName)
                .propagate(propagate)
                .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                .build();
    }
}
