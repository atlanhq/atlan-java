/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowSearchResultStatus extends AtlanObject {
    private static final long serialVersionUID = 2L;

    @JsonIgnore
    Object artifactRepositoryRef;

    @JsonIgnore
    String compressedNodes;

    @JsonIgnore
    Object nodes;

    @JsonIgnore
    Object storedTemplates;

    @JsonIgnore
    Object storedWorkflowTemplateSpec;

    Long estimatedDuration;
    AtlanWorkflowPhase phase;
    WorkflowParameters outputs;
    String startedAt;
    String finishedAt;
    String message;
    String progress;
    List<Object> conditions;
    Map<String, Long> resourcesDuration;
    Map<String, Object> synchronization;
}
