/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

public class AuthenticationException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public AuthenticationException(String message, String requestId, String code, Integer statusCode) {
        super(message, requestId, code, statusCode);
    }
}
