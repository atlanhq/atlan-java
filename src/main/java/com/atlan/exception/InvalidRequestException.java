/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends AtlanException {
    private static final long serialVersionUID = 2L;

    private final String param;

    public InvalidRequestException(
            String message, String param, String requestId, String code, Integer statusCode, Throwable e) {
        super(message, requestId, code, statusCode, e);
        this.param = param;
    }
}
