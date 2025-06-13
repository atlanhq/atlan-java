/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/**
 * Error that occurs if the operation being attempted hits a lock within Atlan. For example, trying to update
 * or create a type definition while others are still being processed.
 */
public class LockedException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public LockedException(ErrorCode error, String... params) {
        super(error, null, params);
    }

    public LockedException(ExceptionMessageDefinition error) {
        super(error, 423);
    }
}
