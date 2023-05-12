/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the schedule for a Monte Carlo rule.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MCRuleSchedule extends AtlanObject {

    /**
     * Quickly create a new MCRuleSchedule.
     * @param mcRuleScheduleType Type of schedule for the rule, for example dynamic or manual.
     * @param mcRuleScheduleIntervalInMinutes How often the monitor should run, in minutes.
     * @param mcRuleScheduleStartTime When the first execution of the rule should occur (blank means immediate).
     * @param mcRuleScheduleCrontab TBC
     * @return a MCRuleSchedule with the provided information
     */
    public static MCRuleSchedule of(
            String mcRuleScheduleType,
            Integer mcRuleScheduleIntervalInMinutes,
            Long mcRuleScheduleStartTime,
            String mcRuleScheduleCrontab) {
        return MCRuleSchedule.builder()
                .mcRuleScheduleType(mcRuleScheduleType)
                .mcRuleScheduleIntervalInMinutes(mcRuleScheduleIntervalInMinutes)
                .mcRuleScheduleStartTime(mcRuleScheduleStartTime)
                .mcRuleScheduleCrontab(mcRuleScheduleCrontab)
                .build();
    }

    /** Type of schedule for the rule, for example dynamic or manual. */
    String mcRuleScheduleType;

    /** How often the monitor should run, in minutes. */
    Integer mcRuleScheduleIntervalInMinutes;

    /** When the first execution of the rule should occur (blank means immediate). */
    Long mcRuleScheduleStartTime;

    /** TBC */
    String mcRuleScheduleCrontab;
}
