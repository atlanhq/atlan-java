/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

public class RateLimitException extends InvalidRequestException {
    private static final long serialVersionUID = 2L;

    public RateLimitException(
            String message, String param, String requestId, String code, Integer statusCode, Throwable e) {
        super(message, param, requestId, code, statusCode, e);
    }
}
