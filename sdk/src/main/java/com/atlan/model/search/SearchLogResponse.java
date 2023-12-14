/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Captures the response from a search against Atlan's search log. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Slf4j
public class SearchLogResponse extends ApiResource implements Iterable<SearchLogEntry> {
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

    /** Parameters for the search. */
    SearchLogRequest searchParameters;

    /** List of results from the search. */
    @JsonProperty("logs")
    List<SearchLogEntry> logEntries;

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
    public SearchLogResponse getNextPage() throws AtlanException {
        IndexSearchDSL dsl = getSearchParameters().getDsl();
        int from = dsl.getFrom() == null ? 0 : dsl.getFrom();
        int page = dsl.getSize() == null ? 10 : dsl.getSize();
        dsl = dsl.toBuilder().from(from + page).build();

        SearchLogRequest.SearchLogRequestBuilder<?, ?> next = SearchLogRequest.builder(dsl);
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
    public List<SearchLogEntry> getSpecificPage(int offset, int pageSize) throws AtlanException {
        IndexSearchDSL dsl = getSearchParameters().getDsl().toBuilder()
                .from(offset)
                .size(pageSize)
                .build();
        SearchLogRequest.SearchLogRequestBuilder<?, ?> next = SearchLogRequest.builder(dsl);
        SearchLogResponse response = next.build().search(client);
        if (response != null && response.getLogEntries() != null) {
            return response.getLogEntries();
        } else {
            return Collections.emptyList();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<SearchLogEntry> iterator() {
        return new SearchLogResponseIterator(this);
    }

    /** {@inheritDoc} */
    @Override
    public Spliterator<SearchLogEntry> spliterator() {
        Integer pageSize = getSearchParameters().getDsl().getSize();
        if (pageSize == null) {
            pageSize = 50;
        }
        SearchLogResponseSpliterator spliterator =
                new SearchLogResponseSpliterator(this, 0, this.getApproximateCount(), pageSize);
        List<SearchLogEntry> entries = getLogEntries() == null ? Collections.emptyList() : getLogEntries();
        spliterator.firstPage = entries.spliterator();
        return spliterator;
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<SearchLogEntry> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), approximateCount, CHARACTERISTICS), false);
    }

    /**
     * Stream the results in parallel across all pages (may do more than limited to in a request).
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<SearchLogEntry> parallelStream() {
        return StreamSupport.stream(this::spliterator, CHARACTERISTICS, true);
    }

    /**
     * Allow results to be iterated through in parallel without managing paging retrievals.
     * With inspiration from: https://stackoverflow.com/questions/38128274/how-can-i-create-a-general-purpose-paging-spliterator
     */
    private static class SearchLogResponseSpliterator implements Spliterator<SearchLogEntry> {

        private final SearchLogResponse response;
        private long start;
        private final long end;
        private final long pageSize;
        private Spliterator<SearchLogEntry> firstPage;
        private Spliterator<SearchLogEntry> currentPage;

        SearchLogResponseSpliterator(SearchLogResponse response, long start, long end, long pageSize) {
            this.response = response;
            this.start = start;
            this.end = end;
            this.pageSize = pageSize;
        }

        /** {@inheritDoc} */
        @Override
        public boolean tryAdvance(Consumer<? super SearchLogEntry> action) {
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
        public void forEachRemaining(Consumer<? super SearchLogEntry> action) {
            do {
                ensurePage().forEachRemaining(action);
                currentPage = null;
            } while (start < end);
        }

        /** {@inheritDoc} */
        @Override
        public Spliterator<SearchLogEntry> trySplit() {
            if (firstPage != null) {
                // If we have a first page already fetched, use it
                Spliterator<SearchLogEntry> fp = firstPage;
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
                return new SearchLogResponseSpliterator(response, start, start = mid, pageSize);
            }
            return ensurePage().trySplit();
        }

        /** Only fetch data immediately before traversing or sub-page splitting. */
        private Spliterator<SearchLogEntry> ensurePage() {
            if (firstPage != null) {
                // If we already have a first page fetched, use it
                Spliterator<SearchLogEntry> fp = firstPage;
                firstPage = null; // clear it once we've used it
                currentPage = fp;
                start = fp.getExactSizeIfKnown();
                return fp;
            } else {
                // Otherwise, try to use the current page (if any)
                Spliterator<SearchLogEntry> sp = currentPage;
                if (sp == null) {
                    // ... and if there is no current page...
                    if (start >= end) {
                        // Return an empty spliterator if we're at the end
                        return Spliterators.emptySpliterator();
                    } else {
                        // Otherwise (only NOW), go fetch the next specific page of results needed
                        List<SearchLogEntry> entries;
                        try {
                            entries = response.getSpecificPage((int) start, (int) Math.min(end - start, pageSize));
                        } catch (AtlanException e) {
                            log.warn(
                                    "Unable to fetch the specific page from {} to {}",
                                    start,
                                    Math.min(end - start, pageSize),
                                    e);
                            entries = Collections.emptyList();
                        }
                        // And update this spliterator's starting point accordingly
                        sp = entries.spliterator();
                        start += sp.getExactSizeIfKnown();
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
    private static class SearchLogResponseIterator implements Iterator<SearchLogEntry> {

        private SearchLogResponse response;
        private int i;

        public SearchLogResponseIterator(SearchLogResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getLogEntries() != null && response.getLogEntries().size() > i) {
                return true;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getLogEntries() != null
                            && response.getLogEntries().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of search results.", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public SearchLogEntry next() {
            return response.getLogEntries().get(i++);
        }
    }
}