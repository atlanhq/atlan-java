/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when no further requests are being accepted from the IP address on which the SDK is running.
 * By default, Atlan allows 1800 requests per minute. If your use of the SDK exceed this, you will begin to see
 * these exceptions.
 */
public class RateLimitException extends InvalidRequestException {
    private static final long serialVersionUID = 2L;

    public RateLimitException(String message, String param, String code, Throwable e) {
        this(message, param, code, 429, e);
    }

    public RateLimitException(String message, String param, String code, Integer statusCode, Throwable e) {
        super(message, param, code, statusCode, e);
    }
}
