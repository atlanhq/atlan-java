/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowMetadata extends AtlanObject {
    private static final long serialVersionUID = 2L;

    Map<String, String> labels;
    Map<String, String> annotations;

    String name;
    String namespace;
    final String uid;
    final String resourceVersion;
    final Long generation;
    final String creationTimestamp;
    final List<Object> managedFields;
}
