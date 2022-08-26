/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

public class NotFoundException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public NotFoundException(String message, String requestId, String code, Integer statusCode, Throwable e) {
        super(message, requestId, code, statusCode, e);
    }
}
