/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

import lombok.Getter;

/**
 * Error that occurs if the request being attempted is not valid for some reason, such as containing insufficient
 * parameters of incorrect values for those parameters.
 */
@Getter
public class InvalidRequestException extends AtlanException {
    private static final long serialVersionUID = 2L;

    private final String param;

    public InvalidRequestException(String message, String param, String code, Integer statusCode, Throwable e) {
        super(message, code, statusCode, e);
        this.param = param;
    }
}
