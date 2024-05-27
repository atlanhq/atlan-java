/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
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
    private static final long MASS_EXTRACT_THRESHOLD = IndexSearchDSL.DEFAULT_PAGE_SIZE * 1000L;

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

        // Check for a timestamp condition to determine if this query is being streamed
        Query query = dsl.getQuery();
        boolean streamed = false;
        List<Query> rewrittenFilters = new ArrayList<>();
        if (query.isBool()) {
            BoolQuery original = query.bool();
            List<Query> filters = original.filter();
            for (Query candidate : filters) {
                if (isPagingTimestampQuery(candidate)) {
                    // Skip the paging parameter from the rewrite
                    streamed = true;
                } else {
                    rewrittenFilters.add(candidate);
                }
            }
        }
        int page = dsl.getSize() == null ? IndexSearchDSL.DEFAULT_PAGE_SIZE : dsl.getSize();
        long firstRecord = -2L;
        long lastRecord;
        if (getAssets() != null && getAssets().size() > 1) {
            firstRecord = getAssets().get(0).getCreateTime();
            lastRecord = getAssets().get(getAssets().size() - 1).getCreateTime();
        } else {
            lastRecord = -2L;
        }
        if (streamed && firstRecord != lastRecord) {
            // If we're streaming and the first and last record have different timestamps,
            // page based on a new timestamp (to keep offsets low)
            rewrittenFilters.add(getPagingTimestampQuery(lastRecord));
            BoolQuery original = query.bool();
            BoolQuery rewritten = BoolQuery.of(b -> b.filter(rewrittenFilters)
                    .must(original.must())
                    .mustNot(original.mustNot())
                    .minimumShouldMatch(original.minimumShouldMatch())
                    .should(original.should())
                    .boost(original.boost()));
            dsl = dsl.toBuilder().from(0).size(page).query(rewritten._toQuery()).build();
        } else {
            // If the first and last record in the page have the same timestamp,
            // or we're not streaming, use "normal" offset-based paging
            int from = dsl.getFrom() == null ? 0 : dsl.getFrom();
            dsl = dsl.toBuilder().from(from + getAssets().size()).size(page).build();
        }

        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> next = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            next = next.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            next = next.relationAttributes(searchParameters.getRelationAttributes());
        }
        return next.build().search(client);
    }

    private boolean isPagingTimestampQuery(Query candidate) {
        return candidate.isRange()
                && candidate.range().field().equals(Asset.CREATE_TIME.getInternalFieldName())
                && candidate.range().gte() != null
                && candidate.range().gte().to(Long.class) > 0;
    }

    private Query getPagingTimestampQuery(long lastTimestamp) {
        return Asset.CREATE_TIME.gte(lastTimestamp);
    }

    private IndexSearchResponse getFirstPageTimestampOrdered() throws AtlanException {
        IndexSearchDSL dsl = getQuery();
        List<SortOptions> revisedSort = sortByTimestampFirst(dsl.getSort());
        int page = dsl.getSize() == null ? IndexSearchDSL.DEFAULT_PAGE_SIZE : dsl.getSize();
        dsl = dsl.toBuilder().from(0).size(page).clearSort().sort(revisedSort).build();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> first = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            first = first.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            first = first.relationAttributes(searchParameters.getRelationAttributes());
        }
        return first.build().search(client);
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
        if (approximateCount > MASS_EXTRACT_THRESHOLD) {
            log.debug(
                    "Results size exceeds threshold ({}), ignoring request for parallelized streaming and falling back to offset-limited sequential paging.",
                    MASS_EXTRACT_THRESHOLD);
            return stream();
        } else {
            return StreamSupport.stream(this::spliterator, CHARACTERISTICS, true);
        }
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
     * Indicates whether the sort options prioritize creation-time in ascending order as the first
     * sorting key (true) or anything else (false).
     *
     * @param sort list of sorting options
     * @return true if the sorting options have creation time, ascending as the first option
     */
    public static boolean presortedByTimestamp(List<SortOptions> sort) {
        return sort != null
                && !sort.isEmpty()
                && sort.get(0).isField()
                && sort.get(0).field().field().equals(Asset.CREATE_TIME.getInternalFieldName())
                && sort.get(0).field().order() == SortOrder.Asc;
    }

    /**
     * Rewrites the sorting options to ensure that sorting by creation time, ascending, is the top
     * priority. Adds this condition if it does not already exist, or moves it up to the top sorting
     * priority if it does already exist in the list.
     *
     * @param sort list of sorting options
     * @return rewritten sorting options, making sorting by creation time in ascending order the top priority
     */
    public static List<SortOptions> sortByTimestampFirst(List<SortOptions> sort) {
        if (sort == null || sort.isEmpty()) {
            return List.of(Asset.CREATE_TIME.order(SortOrder.Asc));
        } else {
            List<SortOptions> rewritten = new ArrayList<>();
            rewritten.add(Asset.CREATE_TIME.order(SortOrder.Asc));
            for (SortOptions candidate : sort) {
                if (!candidate.isField()
                        || !candidate.field().field().equals(Asset.CREATE_TIME.getInternalFieldName())) {
                    rewritten.add(candidate);
                }
            }
            return rewritten;
        }
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class IndexSearchResponseIterator implements Iterator<Asset> {

        private IndexSearchResponse response;
        private final Set<String> processedGuids;
        private int i;

        public IndexSearchResponseIterator(IndexSearchResponse response) {
            try {
                IndexSearchDSL dsl = response.getQuery();
                if (presortedByTimestamp(dsl.getSort())) {
                    // If the results are already sorted in ascending order by timestamp, proceed
                    this.response = response;
                } else {
                    // Otherwise, re-fetch the first page sorted first by timestamp
                    this.response = response.getFirstPageTimestampOrdered();
                }
            } catch (AtlanException e) {
                throw new RuntimeException("Unable to rewrite original query in preparation for iteration.", e);
            }
            this.processedGuids = new HashSet<>();
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
            // TODO: Consider optimizing memory using UUID.fromString() to store UUIDs rather than Strings
            Asset candidate = response.getAssets().get(i++);
            if (candidate != null) {
                if (!processedGuids.contains(candidate.getGuid())) {
                    processedGuids.add(candidate.getGuid());
                    return candidate;
                } else if (hasNext()) {
                    return next();
                }
            }
            return null;
        }
    }
}
