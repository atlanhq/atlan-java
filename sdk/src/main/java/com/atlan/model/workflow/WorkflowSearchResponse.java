/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.net.ApiResource;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Long took;
    WorkflowSearchHits hits;
    Map<String, Object> _shards;
}
