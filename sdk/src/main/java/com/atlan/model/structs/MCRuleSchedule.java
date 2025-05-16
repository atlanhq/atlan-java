/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the schedule for a Monte Carlo rule.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class MCRuleSchedule extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MCRuleSchedule";

    /** Fixed typeName for MCRuleSchedule. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of schedule for the rule, for example dynamic or manual. */
    String mcRuleScheduleType;

    /** How often the monitor should run, in minutes. */
    Integer mcRuleScheduleIntervalInMinutes;

    /** When the first execution of the rule should occur (blank means immediate). */
    Long mcRuleScheduleStartTime;

    /** Crontab for the schedule. */
    String mcRuleScheduleCrontab;

    /**
     * Quickly create a new MCRuleSchedule.
     * @param mcRuleScheduleType Type of schedule for the rule, for example dynamic or manual.
     * @param mcRuleScheduleIntervalInMinutes How often the monitor should run, in minutes.
     * @param mcRuleScheduleStartTime When the first execution of the rule should occur (blank means immediate).
     * @param mcRuleScheduleCrontab Crontab for the schedule.
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

    public abstract static class MCRuleScheduleBuilder<C extends MCRuleSchedule, B extends MCRuleScheduleBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
