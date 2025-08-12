/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.AppWorkflowRunStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A step within a workflow execution.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AppWorkflowRunStep extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AppWorkflowRunStep";

    /** Fixed typeName for AppWorkflowRunStep. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name describing the step's group. */
    String appWorkflowRunLabel;

    /** Current execution state of this workflow step. */
    AppWorkflowRunStatus appWorkflowRunStatus;

    /** Timestamp when the step began execution. */
    Long appWorkflowRunStartedAt;

    /** Timestamp when the step finished execution. */
    Long appWorkflowRunCompletedAt;

    /** Results and metrics produced by this step. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> appWorkflowRunOutputs;

    /**
     * Quickly create a new AppWorkflowRunStep.
     * @param appWorkflowRunLabel Name describing the step's group.
     * @param appWorkflowRunStatus Current execution state of this workflow step.
     * @param appWorkflowRunStartedAt Timestamp when the step began execution.
     * @param appWorkflowRunCompletedAt Timestamp when the step finished execution.
     * @param appWorkflowRunOutputs Results and metrics produced by this step.
     * @return a AppWorkflowRunStep with the provided information
     */
    public static AppWorkflowRunStep of(
            String appWorkflowRunLabel,
            AppWorkflowRunStatus appWorkflowRunStatus,
            Long appWorkflowRunStartedAt,
            Long appWorkflowRunCompletedAt,
            Map<String, String> appWorkflowRunOutputs) {
        return AppWorkflowRunStep.builder()
                .appWorkflowRunLabel(appWorkflowRunLabel)
                .appWorkflowRunStatus(appWorkflowRunStatus)
                .appWorkflowRunStartedAt(appWorkflowRunStartedAt)
                .appWorkflowRunCompletedAt(appWorkflowRunCompletedAt)
                .appWorkflowRunOutputs(appWorkflowRunOutputs)
                .build();
    }

    public abstract static class AppWorkflowRunStepBuilder<
                    C extends AppWorkflowRunStep, B extends AppWorkflowRunStepBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
