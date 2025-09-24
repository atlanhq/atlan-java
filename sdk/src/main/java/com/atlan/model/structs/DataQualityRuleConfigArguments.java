/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the configurations of rule.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DataQualityRuleConfigArguments extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataQualityRuleConfigArguments";

    /** Fixed typeName for DataQualityRuleConfigArguments. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Threshold object for the rule. */
    DataQualityRuleThresholdObject dqRuleThresholdObject;

    /** Raw json string of the rule config that contains the rule definitions. */
    String dqRuleConfigArgumentsRaw;

    /** Json string containing the rule conditions. */
    String dqRuleConfigRuleConditions;

    /**
     * Quickly create a new DataQualityRuleConfigArguments.
     * @param dqRuleThresholdObject Threshold object for the rule.
     * @param dqRuleConfigArgumentsRaw Raw json string of the rule config that contains the rule definitions.
     * @param dqRuleConfigRuleConditions Json string containing the rule conditions.
     * @return a DataQualityRuleConfigArguments with the provided information
     */
    public static DataQualityRuleConfigArguments of(
            DataQualityRuleThresholdObject dqRuleThresholdObject,
            String dqRuleConfigArgumentsRaw,
            String dqRuleConfigRuleConditions) {
        return DataQualityRuleConfigArguments.builder()
                .dqRuleThresholdObject(dqRuleThresholdObject)
                .dqRuleConfigArgumentsRaw(dqRuleConfigArgumentsRaw)
                .dqRuleConfigRuleConditions(dqRuleConfigRuleConditions)
                .build();
    }

    public abstract static class DataQualityRuleConfigArgumentsBuilder<
                    C extends DataQualityRuleConfigArguments, B extends DataQualityRuleConfigArgumentsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
