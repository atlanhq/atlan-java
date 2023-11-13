/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Definition of a single task within a single step of a workflow.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowTask extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the task. */
    String name;

    /** Arguments the task uses in its execution. */
    WorkflowParameters arguments;

    /** Reference to another workflow template that will be used to run this task. */
    WorkflowTemplateRef templateRef;
}
