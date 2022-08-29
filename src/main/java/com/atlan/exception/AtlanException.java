/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.model.core.AtlanError;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AtlanException extends Exception {
    private static final long serialVersionUID = 2L;

    /** The error resource returned by Atlan's API that caused the exception. */
    // transient so the exception can be serialized, as AtlanObject does not
    // implement Serializable
    @Setter
    transient AtlanError atlanError;

    private String code;
    private Integer statusCode;

    protected AtlanException(String message, String code, Integer statusCode) {
        this(message, code, statusCode, null);
    }

    /** Constructs a new Atlan exception with the specified details. */
    protected AtlanException(String message, String code, Integer statusCode, Throwable e) {
        super(message, e);
        this.code = code;
        this.statusCode = statusCode;
    }

    /**
     * Returns a description of the exception, including the HTTP status code and request ID (if
     * applicable).
     *
     * @return a string representation of the exception.
     */
    @Override
    public String getMessage() {
        String additionalInfo = "";
        if (code != null) {
            additionalInfo += "; code: " + code;
        }
        return super.getMessage() + additionalInfo;
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
