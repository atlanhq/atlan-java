/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AtlanError extends AtlanObject {

    /** A short string indicating the error code reported. */
    String errorCode;

    /** A human-readable message providing more details about the error. */
    String errorMessage;

    /** TBC */
    String entityGuid;
}
