/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.model.core.AtlanError;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for any error raised by interactions with Atlan's APIs.
 */
@Getter
public abstract class AtlanException extends Exception {
    private static final long serialVersionUID = 2L;

    /** The error resource returned by Atlan's API that caused the exception. */
    // transient so the exception can be serialized, as AtlanObject does not
    // implement Serializable
    @Setter
    transient AtlanError atlanError;

    private final String code;
    private final Integer statusCode;

    /**
     * Only intended to be used for exceptions that we pass through the SDK.
     *
     * @param error details of the error we pass through
     * @param statusCode HTTP response code of the error
     */
    protected AtlanException(ExceptionMessageDefinition error, int statusCode) {
        super(error.getErrorMessage());
        this.code = error.getErrorId();
        this.statusCode = statusCode;
    }

    /**
     * Only intended to be used for exceptions that we pass through the SDK.
     *
     * @param error details of the error we pass through
     * @param statusCode HTTP response code of the error
     * @param e the underlying cause of the error
     */
    protected AtlanException(ExceptionMessageDefinition error, int statusCode, Throwable e) {
        super(error.getErrorMessage(), e);
        this.code = error.getErrorId();
        this.statusCode = statusCode;
    }

    protected AtlanException(ErrorCode error, Throwable e) {
        super(error.getMessageDefinition().getErrorMessage(), e);
        this.code = error.getMessageDefinition().getErrorId();
        this.statusCode = error.getMessageDefinition().getHttpErrorCode();
    }

    protected AtlanException(ErrorCode error, Throwable e, String... params) {
        super(error.getMessageDefinition().getErrorMessage(params), e);
        this.code = error.getMessageDefinition().getErrorId();
        this.statusCode = error.getMessageDefinition().getHttpErrorCode();
    }

    /**
     * Returns a description of the exception, including the HTTP status code and request ID (if
     * applicable).
     *
     * @return a string representation of the exception.
     */
    @Override
    public String getMessage() {
        if (code != null) {
            return code + " " + super.getMessage();
        } else {
            return super.getMessage();
        }
    }

    /**
     * Returns a description of the user facing exception
     *
     * @return a string representation of the user facing exception.
     */
    public String getUserMessage() {
        return super.getMessage();
    }
}
