/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for metrics assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DbtMetric.class, name = DbtMetric.TYPE_NAME),
})
@Slf4j
public abstract class Metric extends Asset implements IMetric, IDataQuality, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Metric";

    /** TBC */
    @Attribute
    String metricFilters;

    /** TBC */
    @Attribute
    String metricSQL;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> metricTimeGrains;

    /** TBC */
    @Attribute
    String metricType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAsset> assets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> metricDimensionColumns;

    /** TBC */
    @Attribute
    IColumn metricTimestampColumn;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
