/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.exception;

public class IdempotencyException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public IdempotencyException(String message, String requestId, String code, Integer statusCode) {
        super(message, requestId, code, statusCode);
    }
}
