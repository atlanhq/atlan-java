/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if the operation being attempted hits a conflict within Atlan. For example, trying to create
 * an object that already exists.
 */
public class ConflictException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public ConflictException(ErrorCode error, String... params) {
        super(error, null, params);
    }

    public ConflictException(ExceptionMessageDefinition error) {
        super(error, 409);
    }
}
