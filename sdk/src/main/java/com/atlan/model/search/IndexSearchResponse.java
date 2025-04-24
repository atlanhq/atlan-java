/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import static com.atlan.model.search.IndexSearchDSL.DEFAULT_PAGE_SIZE;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AtlanObject;
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
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

/**
 * Captures the response from a search against Atlan. Also provides the ability to iteratively
 * page through results, without needing to track or re-run the original query using {@link #getNextPage()}.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class IndexSearchResponse extends ApiResource implements Iterable<Asset> {
    private static final long serialVersionUID = 2L;
    private static final long MASS_EXTRACT_THRESHOLD = 100000L - DEFAULT_PAGE_SIZE;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

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

    /** Details about each search result, only available when {@code showSearchMetadata} is true in the request. */
    Map<String, Metadata> searchMetadata;

    /** Map of results for the requested aggregations. */
    Map<String, AggregationResult> aggregations;

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Metadata extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** TBC */
        Object highlights;

        /** Tags to associate with the search request. */
        @JsonProperty("sort")
        @Singular
        List<Object> sorts;
    }

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
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
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
     * Retrieve the next page of results from this response, using bulk-oriented paging.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    protected IndexSearchResponse getNextBulkPage() throws AtlanException {
        if (getAssets() == null) {
            // If there are no assets, return this no-asset page (we're at the end of paging).
            return this;
        }
        IndexSearchDSL dsl = getQuery();
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
        // Inject the search_after sorts to use for the subsequent page
        List<Asset> results = getAssets();
        Asset last = results.get(results.size() - 1);
        Metadata offsets = getSearchMetadata().get(last.getGuid());
        dsl = dsl.toBuilder()
                .from(0)
                .size(page)
                .clearPageOffsets()
                .pageOffsets(offsets.getSorts())
                .build();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> next = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            next = next.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            next = next.relationAttributes(searchParameters.getRelationAttributes());
        }
        return next.showSearchMetadata(true).build().search(client);
    }

    private IndexSearchResponse getFirstPageTimestampOrdered() throws AtlanException {
        IndexSearchDSL dsl = getQuery();
        List<SortOptions> revisedSort = sortByTimestampFirst(dsl.getSort());
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
        dsl = dsl.toBuilder().from(0).size(page).clearSort().sort(revisedSort).build();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> first = IndexSearchRequest.builder(dsl);
        if (searchParameters.getAttributes() != null) {
            first = first.attributes(searchParameters.getAttributes());
        }
        if (searchParameters.getRelationAttributes() != null) {
            first = first.relationAttributes(searchParameters.getRelationAttributes());
        }
        return first.showSearchMetadata(true).build().search(client);
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

    /**
     * Returns an iterator over the elements of the index search, lazily paged,
     * but in such a way that they can be iterated through in bulk (100,000's of results or more).
     * Note: this will reorder the results and will NOT retain the sort ordering you have specified (if any).
     * (Uses offset-limited sequential paging, to avoid large offsets that effectively need to re-retrieve many
     * large numbers of previous pages' results.)
     * @return iterator through the assets in the search results
     */
    public Iterator<Asset> biterator() {
        return new IndexSearchResponseBulkIterator(this);
    }

    /** {@inheritDoc} */
    @Override
    public Spliterator<Asset> spliterator() {
        long pageSize;
        try {
            pageSize = getQuery().getSize();
        } catch (LogicException e) {
            log.warn("Unable to parse page size from query, falling back to {}.", DEFAULT_PAGE_SIZE, e);
            pageSize = DEFAULT_PAGE_SIZE;
        }
        IndexSearchResponseSpliterator spliterator =
                new IndexSearchResponseSpliterator(this, 0, this.getApproximateCount(), pageSize);
        List<Asset> assets = getAssets() == null ? Collections.emptyList() : getAssets();
        spliterator.firstPage = assets.spliterator();
        return spliterator;
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * Note: if the number of results exceeds the predefined threshold (100,000 assets)
     * this will be automatically converted into a bulkStream().
     *
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> stream() {
        if (approximateCount > MASS_EXTRACT_THRESHOLD) {
            log.debug(
                    "Results size exceeds threshold ({}), rewriting stream as a bulk stream (ignoring original sorting).",
                    MASS_EXTRACT_THRESHOLD);
            return bulkStream();
        } else {
            return StreamSupport.stream(Spliterators.spliterator(iterator(), approximateCount, CHARACTERISTICS), false);
        }
    }

    /**
     * Stream a large number of results (lazily) for processing without needing to manually manage paging.
     * Note: this will reorder the results in order to iterate through a large number (more than 100,000) results,
     * so any sort ordering you have specified may be ignored.
     *
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> bulkStream() {
        return StreamSupport.stream(Spliterators.spliterator(biterator(), approximateCount, CHARACTERISTICS), false);
    }

    /**
     * Stream the results in parallel across all pages (may do more than limited to in a request).
     * Note: if the number of results exceeds the predefined threshold (100,000 assets)
     * this will be automatically converted into a bulkStream().
     *
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<Asset> parallelStream() {
        if (approximateCount > MASS_EXTRACT_THRESHOLD) {
            log.debug(
                    "Results size exceeds threshold ({}), ignoring request for parallelized streaming and falling back to bulk streaming.",
                    MASS_EXTRACT_THRESHOLD);
            return bulkStream();
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
     * Indicates whether the search is ready to use Elastic's search_after paging (true) or not (false).
     *
     * @param response the search response for which to check readiness
     * @return true if the response is ready for search_after paging
     */
    public static boolean readyForSearchAfter(IndexSearchResponse response) {
        return response.getSearchMetadata() != null
                && !response.getSearchMetadata().isEmpty();
    }

    /**
     * Indicates whether the sort options contain any user-requested sorting (true) or not (false).
     *
     * @param sort list of sorting options
     * @return true if the sorting options have any user-requested sorting
     */
    public static boolean hasUserRequestedSort(List<SortOptions> sort) {
        if (presortedByTimestamp(sort)) {
            return false;
        }
        if (sort != null && !sort.isEmpty() && sort.get(0).isField()) {
            String fieldName = sort.get(0).field().field();
            return !fieldName.equals(Asset.GUID.getInternalFieldName()) || sort.size() != 1;
        }
        return true;
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

    /**
     * Allow results to be iterated through without managing paging retrievals, and
     * without overwhelming system resources, even when iterating through 100,000's or more
     * results.
     */
    private static class IndexSearchResponseBulkIterator implements Iterator<Asset> {

        private IndexSearchResponse response;
        private int i;

        public IndexSearchResponseBulkIterator(IndexSearchResponse response) {
            try {
                IndexSearchDSL dsl = response.getQuery();
                if (presortedByTimestamp(dsl.getSort()) && readyForSearchAfter(response)) {
                    // If the results are already sorted in ascending order by timestamp,
                    // and include necessary metadata for search_after, proceed
                    this.response = response;
                } else if (hasUserRequestedSort(dsl.getSort())) {
                    // Alternatively, if they are sorted by any user-requested sort, we need to throw an error
                    throw new IllegalArgumentException(
                            "Bulk searches can only be sorted by timestamp in ascending order - you must remove your own requested sorting to run a bulk search.");
                } else {
                    // Otherwise, re-fetch the first page sorted first by timestamp
                    this.response = response.getFirstPageTimestampOrdered();
                }
            } catch (AtlanException e) {
                throw new RuntimeException("Unable to rewrite original query in preparation for iteration.", e);
            }
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getAssets() == null) {
                // If there are no assets in this page, then there are no more assets
                // so exit straightaway
                return false;
            }
            if (response.getAssets().size() > i) {
                Asset candidate = response.getAssets().get(i);
                if (candidate != null) {
                    return true;
                }
            }
            try {
                response = response.getNextBulkPage();
                i = 0;
                return response.getAssets() != null && response.getAssets().size() > i;
            } catch (AtlanException e) {
                throw new RuntimeException("Unable to iterate through all pages of search results.", e);
            }
        }

        /** {@inheritDoc} */
        @Override
        public Asset next() {
            return response.getAssets().get(i++);
        }
    }
}
