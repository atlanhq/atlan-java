/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Captures the response from a search against Atlan. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Slf4j
public class IndexSearchResponse extends ApiResource implements Iterable<Asset> {
    private static final long serialVersionUID = 2L;

    private static final int CHARACTERISTICS = Spliterator.NONNULL
            | Spliterator.IMMUTABLE
            | Spliterator.ORDERED
            | Spliterator.SIZED
            | Spliterator.SUBSIZED;

    /** Connectivity to the Atlan tenant where the search was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

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
        IndexSearchDSL dsl = getQuery();
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
        return next.build().search(client);
    }

    /**
     * Retrieve a specific page of results using the same query used to produce this response.
     *
     * @param offset starting point for the specific page
     * @param pageSize maximum number of results beyond the starting point to retrieve
     * @return specific page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public List<Asset> getSpecificPage(int offset, int pageSize) throws AtlanException {
        IndexSearchDSL dsl = getQuery().toBuilder().from(offset).size(pageSize).build();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> next = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            next = next.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            next = next.relationAttributes(searchParameters.getRelationAttributes());
        }
        IndexSearchResponse response = next.build().search(client);
        if (response != null && response.getAssets() != null) {
            return response.getAssets();
        } else {
            return Collections.emptyList();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Asset> iterator() {
        return new IndexSearchResponseIterator(this);
    }

    /** {@inheritDoc} */
    @Override
    public Spliterator<Asset> spliterator() {
        long pageSize;
        try {
            pageSize = getQuery().getSize();
        } catch (LogicException e) {
            log.warn("Unable to parse page size from query, falling back to 50.", e);
            pageSize = 50;
        }
        IndexSearchResponseSpliterator spliterator =
                new IndexSearchResponseSpliterator(this, 0, this.getApproximateCount(), pageSize);
        List<Asset> assets = getAssets() == null ? Collections.emptyList() : getAssets();
        spliterator.firstPage = assets.spliterator();
        return spliterator;
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), approximateCount, CHARACTERISTICS), false);
    }

    /**
     * Stream the results in parallel across all pages (may do more than limited to in a request).
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> parallelStream() {
        return StreamSupport.stream(this::spliterator, CHARACTERISTICS, true);
    }

    /**
     * Parse the original query from the search parameters.
     *
     * @return original query as DSL
     * @throws LogicException if the query could not be parsed
     */
    @JsonIgnore
    public IndexSearchDSL getQuery() throws LogicException {
        try {
            return client.readValue(searchParameters.getQuery(), IndexSearchDSL.class);
        } catch (IOException e) {
            throw new LogicException(ErrorCode.UNABLE_TO_PARSE_ORIGINAL_QUERY, e);
        }
    }

    /**
     * Allow results to be iterated through in parallel without managing paging retrievals.
     * With inspiration from: https://stackoverflow.com/questions/38128274/how-can-i-create-a-general-purpose-paging-spliterator
     */
    private static class IndexSearchResponseSpliterator implements Spliterator<Asset> {

        private final IndexSearchResponse response;
        private long start;
        private final long end;
        private final long pageSize;
        private Spliterator<Asset> firstPage;
        private Spliterator<Asset> currentPage;

        IndexSearchResponseSpliterator(IndexSearchResponse response, long start, long end, long pageSize) {
            this.response = response;
            this.start = start;
            this.end = end;
            this.pageSize = pageSize;
        }

        /** {@inheritDoc} */
        @Override
        public boolean tryAdvance(Consumer<? super Asset> action) {
            while (true) {
                if (ensurePage().tryAdvance(action)) {
                    // If we're able to advance the page we're on, go for it
                    return true;
                } else if (start >= end) {
                    // Alternatively, if we're at the end, we can stop now
                    return false;
                } else {
                    // Otherwise, reset the current page to empty and try
                    // to advance again
                    currentPage = null;
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void forEachRemaining(Consumer<? super Asset> action) {
            do {
                ensurePage().forEachRemaining(action);
                currentPage = null;
            } while (start < end);
        }

        /** {@inheritDoc} */
        @Override
        public Spliterator<Asset> trySplit() {
            if (firstPage != null) {
                // If we have a first page already fetched, use it
                Spliterator<Asset> fp = firstPage;
                firstPage = null; // clear it once we've used it
                start = fp.getExactSizeIfKnown();
                return fp;
            } else if (currentPage != null) {
                // Otherwise, if we have a current page already fetched, use that
                return currentPage.trySplit();
            } else if (end - start > pageSize) {
                // Otherwise, if we know there are remaining elements that we have not yet fetched...
                // Calculate a new split-point midway between them (shift rather than divide, to avoid overflow)
                long mid = (start + end) >>> 1;
                // Round the mid-point to a multiple of the pageSize
                mid = mid / pageSize * pageSize;
                if (mid == start) {
                    mid += pageSize;
                }
                // Create a new spliterator with the new mid-point, while resetting this existing
                // spliterator's end-point to the midpoint
                return new IndexSearchResponseSpliterator(response, start, start = mid, pageSize);
            }
            return ensurePage().trySplit();
        }

        /** Only fetch data immediately before traversing or sub-page splitting. */
        private Spliterator<Asset> ensurePage() {
            if (firstPage != null) {
                // If we already have a first page fetched, use it
                Spliterator<Asset> fp = firstPage;
                firstPage = null; // clear it once we've used it
                currentPage = fp;
                start = fp.getExactSizeIfKnown();
                return fp;
            } else {
                // Otherwise, try to use the current page (if any)
                Spliterator<Asset> sp = currentPage;
                if (sp == null) {
                    // ... and if there is no current page...
                    if (start >= end) {
                        // Return an empty spliterator if we're at the end
                        return Spliterators.emptySpliterator();
                    } else {
                        // Otherwise (only NOW), go fetch the next specific page of results needed
                        List<Asset> assets;
                        try {
                            assets = response.getSpecificPage((int) start, (int) Math.min(end - start, pageSize));
                        } catch (AtlanException e) {
                            log.warn(
                                    "Unable to fetch the specific page from {} to {}",
                                    start,
                                    Math.min(end - start, pageSize),
                                    e);
                            assets = Collections.emptyList();
                        }
                        // And update this spliterator's starting point accordingly
                        sp = assets.spliterator();
                        if (sp.getExactSizeIfKnown() > 0) {
                            // If there are any results in the page, increment the start by the size
                            start += sp.getExactSizeIfKnown();
                        } else {
                            // Otherwise, increment the start by the page size (so we skip over this "page"
                            // entirely rather than attempting to re-retrieve it endlessly)
                            start += pageSize;
                        }
                        currentPage = sp;
                    }
                }
                return sp;
            }
        }

        /** {@inheritDoc} */
        @Override
        public long estimateSize() {
            if (currentPage != null) {
                return currentPage.estimateSize();
            }
            return end - start;
        }

        /** {@inheritDoc} */
        @Override
        public int characteristics() {
            return CHARACTERISTICS;
        }
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
