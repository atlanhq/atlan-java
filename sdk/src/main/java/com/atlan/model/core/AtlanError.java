/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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
        if (errorMessage != null && !errorMessage.isEmpty()) {
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
}
