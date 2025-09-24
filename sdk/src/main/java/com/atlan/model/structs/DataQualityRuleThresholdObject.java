/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.DataQualityRuleThresholdUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the threshold object of rule.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DataQualityRuleThresholdObject extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataQualityRuleThresholdObject";

    /** Fixed typeName for DataQualityRuleThresholdObject. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Compare operator to compare with the metric value of the rule. */
    String dqRuleThresholdCompareOperator;

    /** Threshold value to compare with the metric value of the rule. */
    Double dqRuleThresholdValue;

    /** Unit of the threshold value. */
    DataQualityRuleThresholdUnit dqRuleThresholdUnit;

    /**
     * Quickly create a new DataQualityRuleThresholdObject.
     * @param dqRuleThresholdCompareOperator Compare operator to compare with the metric value of the rule.
     * @param dqRuleThresholdValue Threshold value to compare with the metric value of the rule.
     * @param dqRuleThresholdUnit Unit of the threshold value.
     * @return a DataQualityRuleThresholdObject with the provided information
     */
    public static DataQualityRuleThresholdObject of(
            String dqRuleThresholdCompareOperator,
            Double dqRuleThresholdValue,
            DataQualityRuleThresholdUnit dqRuleThresholdUnit) {
        return DataQualityRuleThresholdObject.builder()
                .dqRuleThresholdCompareOperator(dqRuleThresholdCompareOperator)
                .dqRuleThresholdValue(dqRuleThresholdValue)
                .dqRuleThresholdUnit(dqRuleThresholdUnit)
                .build();
    }

    public abstract static class DataQualityRuleThresholdObjectBuilder<
                    C extends DataQualityRuleThresholdObject, B extends DataQualityRuleThresholdObjectBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
