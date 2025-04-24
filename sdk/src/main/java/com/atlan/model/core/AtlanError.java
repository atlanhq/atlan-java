/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.serde.Serde;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AtlanError extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** A numeric error code. */
    @Setter
    Long code;

    /** A short string indicating the error code reported. */
    String errorCode;

    /** A human-readable message providing more details about the error. */
    @Setter
    String errorMessage;

    /** A human-readable description providing more details about the error. */
    @JsonProperty("error_description")
    String errorDescription;

    /** A human-readable explanation of what caused the error. */
    String errorCause;

    /** Unique ID for the error (from the back-end). */
    String errorId;

    /** A human-readable message providing more details about the error. */
    String message;

    /** A human-readable suggestion on resolving the error. */
    String error;

    /** TBC */
    String entityGuid;

    /** TBC */
    String requestId;

    /** TBC */
    String info;

    /** Details about where the error occurred. */
    Map<String, String> errorDetailsMap;

    /** TBC */
    String servlet;

    /** Response code for unauthorized calls. */
    String status;

    /** Path attempted to access for an unauthorized call. */
    String url;

    /** URL to documentation describing the error in more detail. */
    String doc;

    /** Underlying causes noted for the error, if any. */
    List<Cause> causes;

    /**
     * Find the code within the error.
     *
     * @return the code
     */
    public String findCode() {
        if (errorCode != null && !errorCode.isEmpty()) {
            return errorCode;
        } else if (error != null && !error.isEmpty()) {
            return error;
        } else if (code != null) {
            return "" + code;
        } else if (status != null && !status.isEmpty()) {
            return status;
        } else {
            return "(unknown)";
        }
    }

    /**
     * Find the message within the error.
     *
     * @return the message
     */
    public String findMessage() {
        if (errorCause != null && !errorCause.isEmpty()) {
            return errorCause;
        } else if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        } else if (message != null && !message.isEmpty()) {
            if (url != null && !url.isEmpty()) {
                return message + " (" + url + ")";
            }
            return message;
        } else if (errorDescription != null && !errorDescription.isEmpty()) {
            return errorDescription;
        } else if (info != null && !info.isEmpty()) {
            return info;
        } else {
            return "";
        }
    }

    /**
     * Retrieve the underlying causes as a JSON string.
     *
     * @return the underlying causes of the error as a JSON string.
     */
    public String renderCauses() {
        if (causes != null && !causes.isEmpty()) {
            try {
                return Serde.allInclusiveMapper.writeValueAsString(causes);
            } catch (IOException e) {
                return "[{\"errorMessage\":\"Unable to render causes.\"}]";
            }
        } else {
            return "[]";
        }
    }

    /** Details about an underlying cause for an error. */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class Cause extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Back-end component or class that was an underlying cause for the error. */
        String errorType;

        /** Message from the back-end component or class about the cause of the error. */
        String errorMessage;

        /** Specific location in the back-end code for the cause of the error. */
        String location;
    }
}
