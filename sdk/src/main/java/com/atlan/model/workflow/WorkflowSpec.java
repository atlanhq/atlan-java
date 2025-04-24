/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Definition of the workflow: its inputs, outputs and logic.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowSpec extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * List of specific steps in the overall workflow process.
     */
    List<WorkflowTemplate> templates;

    /** Name of the template to use as the starting point when running the workflow. */
    String entrypoint;

    /** TBC */
    final Object arguments;

    /** TBC */
    final Map<String, String> workflowTemplateRef;

    /** Metadata about the workflow, typically the {@code package.argoproj.io/name} annotation. */
    final WorkflowMetadata workflowMetadata;
}
