/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

import lombok.Getter;

/**
 * Error that occurs if the request being attempted is not valid for some reason, such as containing insufficient
 * parameters or incorrect values for those parameters.
 */
@Getter
public class InvalidRequestException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public InvalidRequestException(ExceptionMessageDefinition error) {
        super(error, 400);
    }

    protected InvalidRequestException(ExceptionMessageDefinition error, int statusCode) {
        super(error, statusCode);
    }

    public InvalidRequestException(ErrorCode error, String... params) {
        super(error, null, params);
    }

    public InvalidRequestException(ErrorCode error) {
        super(error, null);
    }

    public InvalidRequestException(ErrorCode error, Throwable e) {
        super(error, e);
    }
}
