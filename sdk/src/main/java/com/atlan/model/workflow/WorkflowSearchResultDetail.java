/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class WorkflowSearchResultDetail extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String kind;
    String apiVersion;
    WorkflowMetadata metadata;
    WorkflowSpec spec;
    WorkflowSearchResultStatus status;
}
