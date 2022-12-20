/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.exception.AtlanException;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the response from a search against Atlan. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Type of query. */
    String queryType;

    /** Parameters for the search. */
    SearchParameters searchParameters;

    /** List of results from the search. */
    @JsonProperty("entities")
    List<Asset> assets;

    /** Approximate number of total results. */
    Long approximateCount;

    /** Map of results for the requested aggregations. */
    Map<String, AggregationResult> aggregations;

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public IndexSearchResponse getNextPage() throws AtlanException {
        IndexSearchDSL dsl;
        try {
            dsl = Serde.mapper.readValue(searchParameters.getQuery(), IndexSearchDSL.class);
        } catch (JsonProcessingException e) {
            throw new LogicException(
                    "Unable to parse original query from the response.", "ATLAN-JAVA-CLIENT-500-040", 500, e);
        }
        int from = dsl.getFrom() == null ? 0 : dsl.getFrom();
        int page = dsl.getSize() == null ? 10 : dsl.getSize();
        dsl.setFrom(from + page);

        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> next =
                IndexSearchRequest.builder().dsl(dsl);
        if (searchParameters.getAttributes() != null) {
            next = next.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            next = next.relationAttributes(searchParameters.getRelationAttributes());
        }
        return next.build().search();
    }
}
