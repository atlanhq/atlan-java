/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.exception;

import java.text.MessageFormat;
import lombok.Builder;
import lombok.Getter;

/**
 * Class to capture the details of individual error messages.
 */
@Getter
@Builder
public class ExceptionMessageDefinition {

    /** HTTP response code for the error. */
    private final int httpErrorCode;

    /** Unique, language-independent code for the error. */
    private final String errorId;

    /** Message for the error, with optional formatting placeholders. */
    private final String errorMessage;

    /** Recommended action for the user to take upon observing this error. */
    private final String userAction;

    /**
     * Retrieve the formatted, parameterized error message.
     *
     * @param params the values to inject into the formatting placeholders of the message
     * @return the formatted error message
     */
    public String getErrorMessage(String... params) {
        if (params != null) {
            MessageFormat mf = new MessageFormat(errorMessage);
            return mf.format(params);
        } else {
            return errorMessage;
        }
    }
}
