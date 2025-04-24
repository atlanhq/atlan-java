/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * Captures the response from a lineage retrieval against Atlan. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Getter
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class LineageListResponse extends ApiResource implements Iterable<Asset> {
    private static final long serialVersionUID = 2L;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

    /** Connectivity to the Atlan tenant where the lineage request was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

    /** Entities in the lineage requested. */
    @JsonProperty("entities")
    List<Asset> assets;

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
        return nextRequest.fetch(client);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Asset> iterator() {
        return new LineageListResponseIterator(this);
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), CHARACTERISTICS), false);
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class LineageListResponseIterator implements Iterator<Asset> {

        private LineageListResponse response;
        private int i;

        public LineageListResponseIterator(LineageListResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getAssets() != null && response.getAssets().size() > i) {
                return true;
            } else if (!response.getHasMore()) {
                return false;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getAssets() != null && response.getAssets().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of lineage results.", e);
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
