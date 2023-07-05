/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Captures the response from a search against Atlan. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponse extends ApiResource implements Iterable<Asset> {
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
            dsl = Atlan.getDefaultClient().readValue(searchParameters.getQuery(), IndexSearchDSL.class);
        } catch (IOException e) {
            throw new LogicException(ErrorCode.UNABLE_TO_PARSE_ORIGINAL_QUERY, e);
        }
        int from = dsl.getFrom() == null ? 0 : dsl.getFrom();
        int page = dsl.getSize() == null ? 10 : dsl.getSize();
        dsl = dsl.toBuilder().from(from + page).build();

        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> next = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            next = next.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            next = next.relationAttributes(searchParameters.getRelationAttributes());
        }
        return next.build().search();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Asset> iterator() {
        return new IndexSearchResponseIterator(this);
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(iterator(), approximateCount, Spliterator.NONNULL + Spliterator.ORDERED),
                false);
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class IndexSearchResponseIterator implements Iterator<Asset> {

        private IndexSearchResponse response;
        private int i;

        public IndexSearchResponseIterator(IndexSearchResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getAssets() != null && response.getAssets().size() > i) {
                return true;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getAssets() != null && response.getAssets().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of search results.", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public Asset next() {
            return response.getAssets().get(i++);
        }
    }
}
