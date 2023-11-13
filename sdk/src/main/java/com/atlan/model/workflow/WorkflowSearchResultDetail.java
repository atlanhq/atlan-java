/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Definition of a workflow in Atlan.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class WorkflowSearchResultDetail extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of workflow. */
    String kind;

    /** Version of the kubernetes object. */
    String apiVersion;

    /** Metadata that describes the workflow. */
    WorkflowMetadata metadata;

    /** Definition of the workflow. */
    WorkflowSpec spec;

    /** Status of the workflow (if run). */
    WorkflowSearchResultStatus status;
}
