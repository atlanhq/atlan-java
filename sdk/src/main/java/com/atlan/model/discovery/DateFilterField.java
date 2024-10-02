/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import java.time.Instant;
import java.util.Date;
import lombok.Getter;

/**
 * Represents any field in Atlan that can be used for discovery by timestamp comparison.
 */
@Getter
public class DateFilterField extends DiscoveryFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public DateFilterField(String field) {
        super(field);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * before the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred before
     * @return a query that will only match assets whose value for the field is chronologically before the provided timestamp
     */
    public DiscoveryFilter before(Date timestamp) {
        return before(timestamp.toInstant());
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * before the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred before
     * @return a query that will only match assets whose value for the field is chronologically before the provided timestamp
     */
    public DiscoveryFilter before(Instant timestamp) {
        return before(timestamp.toEpochMilli());
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * before the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred before
     * @return a query that will only match assets whose value for the field is chronologically before the provided timestamp
     */
    public DiscoveryFilter before(long timestamp) {
        return build("lessThan", timestamp);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * after the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred after
     * @return a query that will only match assets whose value for the field is chronologically after the provided timestamp
     */
    public DiscoveryFilter after(Date timestamp) {
        return after(timestamp.toInstant());
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * after the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred after
     * @return a query that will only match assets whose value for the field is chronologically after the provided timestamp
     */
    public DiscoveryFilter after(Instant timestamp) {
        return after(timestamp.toEpochMilli());
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly
     * after the provided timestamp.
     *
     * @param timestamp the date and time to check the field's value occurred after
     * @return a query that will only match assets whose value for the field is chronologically after the provided timestamp
     */
    public DiscoveryFilter after(long timestamp) {
        return build("greaterThan", timestamp);
    }
}
