/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AtlanError extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** A numeric error code. */
    Long code;

    /** A short string indicating the error code reported. */
    String errorCode;

    /** A human-readable message providing more details about the error. */
    String errorMessage;

    /** A human-readable message providing more details about the error. */
    String message;

    /** A human-readable suggestion on resolving the error. */
    String error;

    /** TBC */
    String entityGuid;

    /** TBC */
    String requestId;

    /** TBC */
    String info;
}
