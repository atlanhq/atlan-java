/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.responses;

import com.atlan.model.workflow.WorkflowMetadata;
import com.atlan.model.workflow.WorkflowSpec;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    WorkflowMetadata metadata;
    WorkflowSpec spec;
    List<Object> payload;
}
