/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Rules in the business policy
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessPolicyRule extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BusinessPolicyRule";

    /** Fixed typeName for BusinessPolicyRule. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** business policy rule id we have to keep it hierarchical e.g. policyId_ruleId */
    String bprId;

    /** Name for business policy rule it can be a display string to show on UI */
    String bprName;

    /** business policy rule to make the ordering easier */
    String bprSequence;

    /** operand in rule for business policy rule these can be attributes like certificateStatus, tags etc. */
    String bprOperand;

    /** operator to apply in rule for business policy rule this can be must_be, must_not_be etc */
    String bprOperator;

    /** value to validate for the operand against the operator for business policy rule. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("bprValue")
    List<String> bprValues;

    /** es query for business policy rule in combination with filter DSL of policy */
    String bprQuery;

    /**
     * Quickly create a new BusinessPolicyRule.
     * @param bprId business policy rule id we have to keep it hierarchical e.g. policyId_ruleId
     * @param bprName Name for business policy rule it can be a display string to show on UI
     * @param bprSequence business policy rule to make the ordering easier
     * @param bprOperand operand in rule for business policy rule these can be attributes like certificateStatus, tags etc.
     * @param bprOperator operator to apply in rule for business policy rule this can be must_be, must_not_be etc
     * @param bprValues value to validate for the operand against the operator for business policy rule.
     * @param bprQuery es query for business policy rule in combination with filter DSL of policy
     * @return a BusinessPolicyRule with the provided information
     */
    public static BusinessPolicyRule of(
            String bprId,
            String bprName,
            String bprSequence,
            String bprOperand,
            String bprOperator,
            List<String> bprValues,
            String bprQuery) {
        return BusinessPolicyRule.builder()
                .bprId(bprId)
                .bprName(bprName)
                .bprSequence(bprSequence)
                .bprOperand(bprOperand)
                .bprOperator(bprOperator)
                .bprValues(bprValues)
                .bprQuery(bprQuery)
                .build();
    }
}
