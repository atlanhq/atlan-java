/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the details of a classification in a request.
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ClassificationPayload extends AtlanRequestPayload {

    /** Name of the classification. */
    String typeName;

    /** Whether the classification should propagate. */
    @Builder.Default
    Boolean propagate = true;

    /** Whether removing the classification should also remove its propagations. */
    @Builder.Default
    Boolean removePropagationsOnEntityDelete = false;

    /** Unused. */
    List<Object> validityPeriods;

    /**
     * Create a classification payload with the specified classification and defaults for propagation.
     *
     * @param classificationName human-readable name of the classification
     * @return the classification payload with defaults for propagation
     */
    public static ClassificationPayload of(String classificationName) {
        return ClassificationPayload.builder().typeName(classificationName).build();
    }

    /**
     * Create a classification payload with the specified details.
     *
     * @param classificationName human-readable name of the classification
     * @param propagate whether the classification should propagate (true) or not (false)
     * @param removePropagationsOnDelete whether the propagated classifications should be cascaded (true) or not (false)
     * @return the classification payload with the specified propagation settings
     */
    public static ClassificationPayload of(
            String classificationName, boolean propagate, boolean removePropagationsOnDelete) {
        return ClassificationPayload.builder()
                .typeName(classificationName)
                .propagate(propagate)
                .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                .build();
    }
}
