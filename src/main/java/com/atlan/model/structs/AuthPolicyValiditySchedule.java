/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the validity schedule for a policy.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AuthPolicyValiditySchedule extends AtlanStruct {

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
