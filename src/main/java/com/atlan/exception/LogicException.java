package com.atlan.exception;

/**
 * Exceptions when an unexpected logic error occurs. If these are ever experienced, they should be
 * immediately reported against the library as bugs.
 */
public class LogicException extends AtlanException {
    private static final long serialVersionUID = 2L;

    public LogicException(String message, String requestId, String code, Integer statusCode) {
        super(message, requestId, code, statusCode);
    }

    public LogicException(String message, String requestId, String code, Integer statusCode, Throwable e) {
        super(message, requestId, code, statusCode, e);
    }
}
