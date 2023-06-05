/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the conditions for a policy.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AuthPolicyCondition extends AtlanStruct {

    public static final String TYPE_NAME = "AuthPolicyCondition";

    /** Fixed typeName for AuthPolicyCondition. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String policyConditionType;

    /** TBC */
    List<String> policyConditionValues;

    /**
     * Quickly create a new AuthPolicyCondition.
     * @param policyConditionType TBC
     * @param policyConditionValues TBC
     * @return a AuthPolicyCondition with the provided information
     */
    public static AuthPolicyCondition of(String policyConditionType, List<String> policyConditionValues) {
        return AuthPolicyCondition.builder()
                .policyConditionType(policyConditionType)
                .policyConditionValues(policyConditionValues)
                .build();
    }
}
