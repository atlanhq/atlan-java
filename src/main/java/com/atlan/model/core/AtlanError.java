/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class AtlanError extends AtlanObject {

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
