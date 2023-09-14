/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when there is a problem with the API token configured in the SDK.
 */
public class AuthenticationException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public AuthenticationException(ExceptionMessageDefinition error) {
        super(error, 401);
    }

    protected AuthenticationException(ExceptionMessageDefinition error, int statusCode) {
        super(error, statusCode);
    }

    public AuthenticationException(ErrorCode error, String... params) {
        super(error, null, params);
    }

    public AuthenticationException(ErrorCode error) {
        super(error, null);
    }

    public AuthenticationException(ErrorCode error, Throwable e) {
        super(error, e);
    }
}
