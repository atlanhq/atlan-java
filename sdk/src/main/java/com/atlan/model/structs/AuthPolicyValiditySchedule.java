/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Validity schedule struct for policy
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AuthPolicyValiditySchedule extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AuthPolicyValiditySchedule";

    /** Fixed typeName for AuthPolicyValiditySchedule. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String policyValidityScheduleStartTime;

    /** TBC */
    String policyValidityScheduleEndTime;

    /** TBC */
    String policyValidityScheduleTimezone;

    /**
     * Quickly create a new AuthPolicyValiditySchedule.
     * @param policyValidityScheduleStartTime TBC
     * @param policyValidityScheduleEndTime TBC
     * @param policyValidityScheduleTimezone TBC
     * @return a AuthPolicyValiditySchedule with the provided information
     */
    public static AuthPolicyValiditySchedule of(
            String policyValidityScheduleStartTime,
            String policyValidityScheduleEndTime,
            String policyValidityScheduleTimezone) {
        return AuthPolicyValiditySchedule.builder()
                .policyValidityScheduleStartTime(policyValidityScheduleStartTime)
                .policyValidityScheduleEndTime(policyValidityScheduleEndTime)
                .policyValidityScheduleTimezone(policyValidityScheduleTimezone)
                .build();
    }
}
