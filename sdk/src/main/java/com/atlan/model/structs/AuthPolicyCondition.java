/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Policy condition schedule struct
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuthPolicyCondition extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AuthPolicyCondition";

    /** Fixed typeName for AuthPolicyCondition. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String policyConditionType;

    /** TBC */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
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
