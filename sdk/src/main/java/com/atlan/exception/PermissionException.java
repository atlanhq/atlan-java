/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if the API token configured for the SDK does not have permission to access or carry out the
 * requested operation on a given object. These can be temporary in nature, as there is some asynchronous processing
 * that occurs when permissions are granted.
 */
public class PermissionException extends AuthenticationException {
    private static final long serialVersionUID = 2L;

    public PermissionException(ErrorCode error, String... params) {
        super(error, params);
    }

    public PermissionException(ExceptionMessageDefinition error) {
        super(error, 403);
    }

    public PermissionException(ErrorCode error, Throwable e) {
        super(error, e);
    }
}
