/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when the SDK receives a response that indicates a problem, but that the SDK currently has no
 * other way of interpreting. Basically, this is a catch-all for errors that do not fit any more specific exception.
 */
public class ApiException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public ApiException(ExceptionMessageDefinition error) {
        super(error, error.getHttpErrorCode());
    }

    public ApiException(ErrorCode error, Throwable e, String... params) {
        super(error, e, params);
    }
}
