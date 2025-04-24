/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Metadata that describes a workflow.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class WorkflowMetadata extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Labels that describe the workflow. These are usually a combination of keys that
     * start with {@code orchestration.atlan.com}, {@code package.argoproj.io}, and
     * {@code workflows.argoproj.io}.
     */
    @Singular
    Map<String, String> labels;

    /**
     * Annotations that describe the workflow. These are usually a combination of keys that
     * start with {@code orchestration.atlan.com} and {@code package.argoproj.io}.
     */
    @Singular
    Map<String, String> annotations;

    /** Name of the workflow. */
    String name;

    /** Kubernetes namespace in which the workflow is defined. */
    String namespace;

    /** Unique identifier (GUID) of the workflow. */
    final String uid;

    /** TBC */
    final String resourceVersion;

    /** TBC */
    final String generateName;

    /**
     * Indication of the version of this workflow. Each modification to the workflow will increment the
     * generation, such that a generation of {@code 1} is the initial workflow definition, a generation of
     * {@code 2} is the first time the workflow's configuration has been modified, and so on.
     */
    final Long generation;

    /** Time at which the workflow was created, as a formatted string. */
    final String creationTimestamp;

    /** TBC **/
    final List<Object> managedFields;
}
