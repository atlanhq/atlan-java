/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if a requested object does not exist. For example, trying to retrieve an asset that does not exist.
 */
public class NotFoundException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public NotFoundException(ErrorCode error, String... params) {
        super(error, null, params);
    }

    public NotFoundException(ErrorCode error, Throwable e, String... params) {
        super(error, e, params);
    }

    public NotFoundException(ExceptionMessageDefinition error) {
        super(error, 404);
    }
}
