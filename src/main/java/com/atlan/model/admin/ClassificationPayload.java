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
}
