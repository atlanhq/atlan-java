/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Read/view count for a given time window on a Tableau asset, anchored to the crawl's as-of date.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class TableauReadCountWindow extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauReadCountWindow";

    /** Fixed typeName for TableauReadCountWindow. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Size of the time window in days (e.g., 14, 30, 90, 365). */
    Long tableauReadCountWindowDays;

    /** Number of reads/views recorded within this window. */
    Long tableauReadCountWindowCount;

    /** Anchor date for this window — when the source reported the count (typically the crawl run time). */
    Long tableauReadCountWindowAsOfDate;

    /**
     * Quickly create a new TableauReadCountWindow.
     * @param tableauReadCountWindowDays Size of the time window in days (e.g., 14, 30, 90, 365).
     * @param tableauReadCountWindowCount Number of reads/views recorded within this window.
     * @param tableauReadCountWindowAsOfDate Anchor date for this window — when the source reported the count (typically the crawl run time).
     * @return a TableauReadCountWindow with the provided information
     */
    public static TableauReadCountWindow of(
            Long tableauReadCountWindowDays, Long tableauReadCountWindowCount, Long tableauReadCountWindowAsOfDate) {
        return TableauReadCountWindow.builder()
                .tableauReadCountWindowDays(tableauReadCountWindowDays)
                .tableauReadCountWindowCount(tableauReadCountWindowCount)
                .tableauReadCountWindowAsOfDate(tableauReadCountWindowAsOfDate)
                .build();
    }

    public abstract static class TableauReadCountWindowBuilder<
                    C extends TableauReadCountWindow, B extends TableauReadCountWindowBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
