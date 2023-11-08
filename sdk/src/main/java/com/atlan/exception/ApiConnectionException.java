/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs when there is an intermittent issue with the API, such as a network outage or an inability
 * to connect due to an incorrect URL.
 */
public class ApiConnectionException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public ApiConnectionException(ErrorCode error) {
        super(error, null);
    }

    public ApiConnectionException(ErrorCode error, Throwable e, String... params) {
        super(error, e, params);
    }
}
