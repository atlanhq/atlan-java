/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = false)
public class LineageListResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Entities in the lineage requested. */
    List<Asset> entities;

    /** Whether there are more entities present in lineage that can be traversed (true) or not (false). */
    Boolean hasMore;

    /** Total count of entities returned, equal to the size of the {@code entities} list. */
    Integer entityCount;

    /** Request used to produce this lineage. */
    LineageListRequest searchParameters;

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public LineageListResponse getNextPage() throws AtlanException {
        int from = searchParameters.getFrom() == null ? 0 : searchParameters.getFrom();
        int page = searchParameters.getSize() == null ? 10 : searchParameters.getSize();
        LineageListRequest nextRequest =
                searchParameters.toBuilder().from(from + page).build();
        return nextRequest.fetch();
    }
}
