/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if the operation being attempted hits a conflict within Atlan. For example, trying to create
 * an object that already exists.
 */
public class ConflictException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public ConflictException(String message, String code, Throwable e) {
        this(message, code, 409, e);
    }

    public ConflictException(String message, String code, Integer statusCode, Throwable e) {
        super(message, code, statusCode, e);
    }
}
