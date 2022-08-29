/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when there is a problem with the API token configured in the SDK.
 */
public class AuthenticationException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public AuthenticationException(String message, String code, Integer statusCode) {
        super(message, code, statusCode);
    }
}
