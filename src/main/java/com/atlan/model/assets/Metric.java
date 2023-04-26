/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for metrics assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({})
@Slf4j
public abstract class Metric extends DataQuality {

    public static final String TYPE_NAME = "Metric";

    /** TBC */
    @Attribute
    String metricType;

    /** TBC */
    @Attribute
    String metricSQL;

    /** TBC */
    @Attribute
    String metricFilters;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> metricTimeGrains;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Asset> assets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Column> metricDimensionColumns;

    /** TBC */
    @Attribute
    Column metricTimestampColumn;
}
