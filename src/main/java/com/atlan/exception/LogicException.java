/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when an unexpected logic problem arises. If these are ever experienced, they should be
 * immediately reported against the SDK as bugs.
 */
public class LogicException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public LogicException(String message, String code, Integer statusCode) {
        super(message, code, statusCode);
    }

    public LogicException(String message, String code, Integer statusCode, Throwable e) {
        super(message, code, statusCode, e);
    }
}
