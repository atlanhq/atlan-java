/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the comparison logic of a Monte Carlo rule.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class MCRuleComparison extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MCRuleComparison";

    /** Fixed typeName for MCRuleComparison. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of comparison, for example threshold. */
    String mcRuleComparisonType;

    /** Field being compared. */
    String mcRuleComparisonField;

    /** Metric being compared. */
    String mcRuleComparisonMetric;

    /** Operator used for the comparison, for example greater than (GT). */
    String mcRuleComparisonOperator;

    /** Threshold being compared. */
    Double mcRuleComparisonThreshold;

    /** Whether the threshold comparison is relative (true) or absolute (false). */
    Boolean mcRuleComparisonIsThresholdRelative;

    /**
     * Quickly create a new MCRuleComparison.
     * @param mcRuleComparisonType Type of comparison, for example threshold.
     * @param mcRuleComparisonField Field being compared.
     * @param mcRuleComparisonMetric Metric being compared.
     * @param mcRuleComparisonOperator Operator used for the comparison, for example greater than (GT).
     * @param mcRuleComparisonThreshold Threshold being compared.
     * @param mcRuleComparisonIsThresholdRelative Whether the threshold comparison is relative (true) or absolute (false).
     * @return a MCRuleComparison with the provided information
     */
    public static MCRuleComparison of(
            String mcRuleComparisonType,
            String mcRuleComparisonField,
            String mcRuleComparisonMetric,
            String mcRuleComparisonOperator,
            Double mcRuleComparisonThreshold,
            Boolean mcRuleComparisonIsThresholdRelative) {
        return MCRuleComparison.builder()
                .mcRuleComparisonType(mcRuleComparisonType)
                .mcRuleComparisonField(mcRuleComparisonField)
                .mcRuleComparisonMetric(mcRuleComparisonMetric)
                .mcRuleComparisonOperator(mcRuleComparisonOperator)
                .mcRuleComparisonThreshold(mcRuleComparisonThreshold)
                .mcRuleComparisonIsThresholdRelative(mcRuleComparisonIsThresholdRelative)
                .build();
    }
}
