/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Instance of an ai model metrics in databricks.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DatabricksAIModelVersionMetric extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksAIModelVersionMetric";

    /** Fixed typeName for DatabricksAIModelVersionMetric. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** The name of metric key for an individual experiment. */
    String databricksAIModelVersionMetricKey;

    /** The metric key value for an individual experiment. */
    Double databricksAIModelVersionMetricValue;

    /** The metric timestamp value for an individual experiment. */
    Long databricksAIModelVersionMetricTimestamp;

    /** The metric step value for an individual experiment. */
    Integer databricksAIModelVersionMetricStep;

    /**
     * Quickly create a new DatabricksAIModelVersionMetric.
     * @param databricksAIModelVersionMetricKey The name of metric key for an individual experiment.
     * @param databricksAIModelVersionMetricValue The metric key value for an individual experiment.
     * @param databricksAIModelVersionMetricTimestamp The metric timestamp value for an individual experiment.
     * @param databricksAIModelVersionMetricStep The metric step value for an individual experiment.
     * @return a DatabricksAIModelVersionMetric with the provided information
     */
    public static DatabricksAIModelVersionMetric of(
            String databricksAIModelVersionMetricKey,
            Double databricksAIModelVersionMetricValue,
            Long databricksAIModelVersionMetricTimestamp,
            Integer databricksAIModelVersionMetricStep) {
        return DatabricksAIModelVersionMetric.builder()
                .databricksAIModelVersionMetricKey(databricksAIModelVersionMetricKey)
                .databricksAIModelVersionMetricValue(databricksAIModelVersionMetricValue)
                .databricksAIModelVersionMetricTimestamp(databricksAIModelVersionMetricTimestamp)
                .databricksAIModelVersionMetricStep(databricksAIModelVersionMetricStep)
                .build();
    }

    public abstract static class DatabricksAIModelVersionMetricBuilder<
                    C extends DatabricksAIModelVersionMetric, B extends DatabricksAIModelVersionMetricBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
