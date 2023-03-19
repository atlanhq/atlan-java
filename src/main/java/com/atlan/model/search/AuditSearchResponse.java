/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Captures the response from a search against Atlan's activity log.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class AuditSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** List of results from the search. */
    List<EntityAudit> entityAudits;

    /** Unused. */
    @JsonIgnore
    final Object aggregations = null;

    /** Number of results returned in this response. */
    Long count;

    /** Total number of results. */
    Long totalCount;
}
