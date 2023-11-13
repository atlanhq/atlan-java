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
 * Reference to an existing workflow template.
 * These are typically used within a task, within a step of a workflow, to reuse
 * an existing workflow template to run the logic of that task.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowTemplateRef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the existing workflow template. */
    String name;

    /** Entry point to begin executing the referenced workflow template. */
    String template;

    /** TBC */
    Boolean clusterScope;
}
