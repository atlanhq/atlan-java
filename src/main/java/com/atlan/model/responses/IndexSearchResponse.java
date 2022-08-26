/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Type of query. */
    String queryType;

    /** Parameters for the search. */
    Object searchParameters;

    /** List of results from the search. */
    List<Entity> entities;

    /** Approximate number of total results. */
    Long approximateCount;
}
