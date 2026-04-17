/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a rule evaluated in an External DQ Test run.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestRule extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestRule";

    /** Fixed typeName for AssetExternalDQTestRule. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the rule evaluated in the DQ Test run on the external DQ tool. */
    String assetExternalDQTestRuleName;

    /** Evaluation status of the rule in the DQ Test run on the external DQ tool. */
    String assetExternalDQTestRuleEvaluationStatus;

    /** Time (epoch) at which the rule was evaluated in the DQ Test run on the external DQ tool, in milliseconds. */
    Long assetExternalDQTestRuleEvaluatedAt;

    /** Impact level of the rule evaluation result in the DQ Test run on the external DQ tool. */
    String assetExternalDQTestRuleImpact;

    /** Type or category of the rule (e.g. Null Check, Range Check, Row Condition). */
    String assetExternalDQTestRuleType;

    /** Column targeted by this rule, if applicable. */
    String assetExternalDQTestRuleColumnName;

    /** Quality dimension this rule belongs to (e.g. COMPLETENESS, VALIDITY, UNIQUENESS). */
    String assetExternalDQTestRuleDimension;

    /**
     * Quickly create a new AssetExternalDQTestRule.
     * @param assetExternalDQTestRuleName Name of the rule evaluated in the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestRuleEvaluationStatus Evaluation status of the rule in the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestRuleEvaluatedAt Time (epoch) at which the rule was evaluated in the DQ Test run on the external DQ tool, in milliseconds.
     * @param assetExternalDQTestRuleImpact Impact level of the rule evaluation result in the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestRuleType Type or category of the rule (e.g. Null Check, Range Check, Row Condition).
     * @param assetExternalDQTestRuleColumnName Column targeted by this rule, if applicable.
     * @param assetExternalDQTestRuleDimension Quality dimension this rule belongs to (e.g. COMPLETENESS, VALIDITY, UNIQUENESS).
     * @return a AssetExternalDQTestRule with the provided information
     */
    public static AssetExternalDQTestRule of(
            String assetExternalDQTestRuleName,
            String assetExternalDQTestRuleEvaluationStatus,
            Long assetExternalDQTestRuleEvaluatedAt,
            String assetExternalDQTestRuleImpact,
            String assetExternalDQTestRuleType,
            String assetExternalDQTestRuleColumnName,
            String assetExternalDQTestRuleDimension) {
        return AssetExternalDQTestRule.builder()
                .assetExternalDQTestRuleName(assetExternalDQTestRuleName)
                .assetExternalDQTestRuleEvaluationStatus(assetExternalDQTestRuleEvaluationStatus)
                .assetExternalDQTestRuleEvaluatedAt(assetExternalDQTestRuleEvaluatedAt)
                .assetExternalDQTestRuleImpact(assetExternalDQTestRuleImpact)
                .assetExternalDQTestRuleType(assetExternalDQTestRuleType)
                .assetExternalDQTestRuleColumnName(assetExternalDQTestRuleColumnName)
                .assetExternalDQTestRuleDimension(assetExternalDQTestRuleDimension)
                .build();
    }

    public abstract static class AssetExternalDQTestRuleBuilder<
                    C extends AssetExternalDQTestRule, B extends AssetExternalDQTestRuleBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
