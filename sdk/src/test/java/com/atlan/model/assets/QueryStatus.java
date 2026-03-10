/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum QueryStatus implements AtlanEnum {
    /** Query run has been requested, but not yet started or completed. */
    STARTED("started"),

    /** Query run is in progress (running). */
    IN_PROGRESS("in-progress"),

    /** Query has completed running, successfully. */
    COMPLETED("completed"),

    /**
     * Some other operation on the query has completed, successfully.
     * For example: it has been aborted or was only testing a connection.
     */
    OK("ok"),

    /** There was an error running the query. */
    ERROR("error"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QueryStatus(String value) {
        this.value = value;
    }

    public static QueryStatus fromValue(String value) {
        for (QueryStatus b : QueryStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
