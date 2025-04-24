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
 * Definition of a specific step in a workflow process.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowTemplate extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of this step. */
    String name;

    /** Inputs to this step. */
    final Object inputs;

    /** Outputs from this step. */
    final Object outputs;

    /** Metadata describing the step. */
    final Object metadata;

    /** Definition of the tasks within the step. */
    WorkflowDAG dag;
}
