/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose a single filter criterion, by tag, for use in a linkable query.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@Slf4j
public class TagFilter {

    /** Atlan-internal tag name for the tag. */
    String name;

    /** Human-readable name for the tag. */
    String displayName;

    /** Value to compare the operand against. */
    @Singular
    List<TagValue> tagValues;

    @Getter
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    public static final class TagValue {
        /** Value of the tag to match. */
        String consolidatedValue;

        /** Unique name(s) of the source tags to match the value against. */
        @Singular
        List<String> tagQFNames;
    }
}
