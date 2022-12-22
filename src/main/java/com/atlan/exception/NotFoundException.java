/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if a requested object does not exist. For example, trying to retrieve an asset that does not exist.
 */
public class NotFoundException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public NotFoundException(String message, String code, Throwable e) {
        this(message, code, 404, e);
    }

    public NotFoundException(String message, String code, Integer statusCode, Throwable e) {
        super(message, code, statusCode, e);
    }
}
