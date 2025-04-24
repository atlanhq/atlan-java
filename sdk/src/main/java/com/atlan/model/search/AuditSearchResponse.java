/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import static com.atlan.model.search.IndexSearchDSL.DEFAULT_PAGE_SIZE;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Captures the response from a search against Atlan's activity log.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AuditSearchResponse extends ApiResource implements Iterable<EntityAudit> {
    private static final long serialVersionUID = 2L;
    private static final long MASS_EXTRACT_THRESHOLD = 10000L - DEFAULT_PAGE_SIZE;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

    /** Connectivity to the Atlan tenant where the search was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

    /** Request used to produce this audit search response. */
    @Setter
    @JsonIgnore
    AuditSearchRequest request;

    /** List of results from the search. */
    List<EntityAudit> entityAudits;

    /** Map of results for the requested aggregations. */
    Map<String, AggregationResult> aggregations;

    /** Number of results returned in this response. */
    Long count;

    /** Total number of results. */
    Long totalCount;

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public AuditSearchResponse getNextPage() throws AtlanException {
        IndexSearchDSL dsl = getRequest().getDsl();
        int from = dsl.getFrom() == null ? 0 : dsl.getFrom();
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
        dsl = dsl.toBuilder().from(from + page).build();

        AuditSearchRequest.AuditSearchRequestBuilder<?, ?> next =
                AuditSearchRequest.builder().dsl(dsl);
        if (getRequest().getAttributes() != null) {
            next = next.attributes(getRequest().getAttributes());
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
    public List<EntityAudit> getSpecificPage(int offset, int pageSize) throws AtlanException {
        IndexSearchDSL dsl =
                getRequest().getDsl().toBuilder().from(offset).size(pageSize).build();
        AuditSearchRequest.AuditSearchRequestBuilder<?, ?> next =
                AuditSearchRequest.builder().dsl(dsl);
        if (getRequest().getAttributes() != null) {
            next = next.attributes(getRequest().getAttributes());
        }
        AuditSearchResponse response = next.build().search(client);
        if (response != null && response.getEntityAudits() != null) {
            return response.getEntityAudits();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieve the next page of results from this response, using bulk-oriented paging.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    protected AuditSearchResponse getNextBulkPage() throws AtlanException {
        if (getEntityAudits() == null) {
            // If there are no results, return this no-asset page (we're at the end of paging).
            return this;
        }
        IndexSearchDSL dsl = getRequest().getDsl();

        // Check for a timestamp condition to determine if this query is being streamed
        Query query = dsl.getQuery();
        List<Query> rewrittenFilters = new ArrayList<>();
        boolean streamed = presortedByTimestamp(dsl.getSort());
        if (query.isBool()) {
            BoolQuery original = query.bool();
            List<Query> filters = original.filter();
            for (Query candidate : filters) {
                if (!isPagingTimestampQuery(candidate)) {
                    rewrittenFilters.add(candidate);
                }
            }
        }
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
        long firstRecord = -2L;
        long lastRecord;
        if (getEntityAudits().size() > 1) {
            firstRecord = getEntityAudits().get(0).getCreated();
            lastRecord = getEntityAudits().get(getEntityAudits().size() - 1).getCreated();
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
            dsl = dsl.toBuilder()
                    .from(from + getEntityAudits().size())
                    .size(page)
                    .build();
        }

        AuditSearchRequest.AuditSearchRequestBuilder<?, ?> next =
                AuditSearchRequest.builder().dsl(dsl);
        if (getRequest().getAttributes() != null) {
            next = next.attributes(getRequest().getAttributes());
        }
        return next.build().search(client);
    }

    private boolean isPagingTimestampQuery(Query candidate) {
        return candidate.isRange()
                && candidate.range().untyped().field().equals(AuditSearchRequest.CREATED.getNumericFieldName())
                && candidate.range().untyped().gte() != null
                && candidate.range().untyped().gte().to(Long.class) > 0
                && candidate.range().untyped().lt() == null
                && candidate.range().untyped().lte() == null;
    }

    private Query getPagingTimestampQuery(long lastTimestamp) {
        return AuditSearchRequest.CREATED.gte(lastTimestamp);
    }

    private AuditSearchResponse getFirstPageTimestampOrdered() throws AtlanException {
        IndexSearchDSL dsl = getRequest().getDsl();
        List<SortOptions> revisedSort = sortByTimestampFirst(dsl.getSort());
        int page = dsl.getSize() == null ? DEFAULT_PAGE_SIZE : dsl.getSize();
        dsl = dsl.toBuilder().from(0).size(page).clearSort().sort(revisedSort).build();
        AuditSearchRequest.AuditSearchRequestBuilder<?, ?> first =
                AuditSearchRequest.builder().dsl(dsl);
        if (getRequest().getAttributes() != null) {
            first = first.attributes(getRequest().getAttributes());
        }
        return first.build().search(client);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<EntityAudit> iterator() {
        return new AuditSearchResponse.AuditSearchResponseIterator(this);
    }

    /**
     * Returns an iterator over the elements of the index search, lazily paged,
     * but in such a way that they can be iterated through in bulk (10,000's of results or more).
     * Note: this will reorder the results and will NOT retain the sort ordering you have specified (if any).
     * (Uses offset-limited sequential paging, to avoid large offsets that effectively need to re-retrieve many
     * large numbers of previous pages' results.)
     * @return iterator through the audit entries in the search results
     */
    public Iterator<EntityAudit> biterator() {
        return new AuditSearchResponse.AuditSearchResponseBulkIterator(this);
    }

    /** {@inheritDoc} */
    @Override
    public Spliterator<EntityAudit> spliterator() {
        long pageSize = getRequest().getDsl().getSize();
        AuditSearchResponse.AuditSearchResponseSpliterator spliterator =
                new AuditSearchResponse.AuditSearchResponseSpliterator(this, 0, this.getTotalCount(), pageSize);
        List<EntityAudit> audits = getEntityAudits() == null ? Collections.emptyList() : getEntityAudits();
        spliterator.firstPage = audits.spliterator();
        return spliterator;
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<EntityAudit> stream() {
        if (totalCount > MASS_EXTRACT_THRESHOLD) {
            log.debug(
                    "Results size exceeds threshold ({}), rewriting stream as a bulk stream (ignoring original sorting).",
                    MASS_EXTRACT_THRESHOLD);
            return bulkStream();
        } else {
            return StreamSupport.stream(Spliterators.spliterator(iterator(), totalCount, CHARACTERISTICS), false);
        }
    }

    /**
     * Stream the results in parallel across all pages (may do more than limited to in a request).
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<EntityAudit> parallelStream() {
        if (totalCount > MASS_EXTRACT_THRESHOLD) {
            log.debug(
                    "Results size exceeds threshold ({}), ignoring request for parallelized streaming and falling back to bulk streaming.",
                    MASS_EXTRACT_THRESHOLD);
            return bulkStream();
        } else {
            return StreamSupport.stream(this::spliterator, CHARACTERISTICS, true);
        }
    }

    /**
     * Stream a large number of results (lazily) for processing without needing to manually manage paging.
     * Note: this will reorder the results in order to iterate through a large number (more than 10,000) results,
     * so any sort ordering you have specified may be ignored.
     *
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<EntityAudit> bulkStream() {
        return StreamSupport.stream(Spliterators.spliterator(biterator(), totalCount, CHARACTERISTICS), false);
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
                && sort.get(0).field().field().equals(AuditSearchRequest.CREATED.getNumericFieldName())
                && sort.get(0).field().order() == SortOrder.Asc;
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
            return !fieldName.equals(AuditSearchRequest.ENTITY_ID.getKeywordFieldName()) || sort.size() != 1;
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
            return List.of(AuditSearchRequest.CREATED.order(SortOrder.Asc));
        } else {
            List<SortOptions> rewritten = new ArrayList<>();
            rewritten.add(AuditSearchRequest.CREATED.order(SortOrder.Asc));
            for (SortOptions candidate : sort) {
                if (!candidate.isField()
                        || !candidate.field().field().equals(AuditSearchRequest.CREATED.getNumericFieldName())) {
                    rewritten.add(candidate);
                }
            }
            return rewritten;
        }
    }

    /**
     * Allow results to be iterated through in parallel without managing paging retrievals.
     * With inspiration from: https://stackoverflow.com/questions/38128274/how-can-i-create-a-general-purpose-paging-spliterator
     */
    private static class AuditSearchResponseSpliterator implements Spliterator<EntityAudit> {

        private final AuditSearchResponse response;
        private long start;
        private final long end;
        private final long pageSize;
        private Spliterator<EntityAudit> firstPage;
        private Spliterator<EntityAudit> currentPage;

        AuditSearchResponseSpliterator(AuditSearchResponse response, long start, long end, long pageSize) {
            this.response = response;
            this.start = start;
            this.end = end;
            this.pageSize = pageSize;
        }

        /** {@inheritDoc} */
        @Override
        public boolean tryAdvance(Consumer<? super EntityAudit> action) {
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
        public void forEachRemaining(Consumer<? super EntityAudit> action) {
            do {
                ensurePage().forEachRemaining(action);
                currentPage = null;
            } while (start < end);
        }

        /** {@inheritDoc} */
        @Override
        public Spliterator<EntityAudit> trySplit() {
            if (firstPage != null) {
                // If we have a first page already fetched, use it
                Spliterator<EntityAudit> fp = firstPage;
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
                return new AuditSearchResponse.AuditSearchResponseSpliterator(response, start, start = mid, pageSize);
            }
            return ensurePage().trySplit();
        }

        /** Only fetch data immediately before traversing or sub-page splitting. */
        private Spliterator<EntityAudit> ensurePage() {
            if (firstPage != null) {
                // If we already have a first page fetched, use it
                Spliterator<EntityAudit> fp = firstPage;
                firstPage = null; // clear it once we've used it
                currentPage = fp;
                start = fp.getExactSizeIfKnown();
                return fp;
            } else {
                // Otherwise, try to use the current page (if any)
                Spliterator<EntityAudit> sp = currentPage;
                if (sp == null) {
                    // ... and if there is no current page...
                    if (start >= end) {
                        // Return an empty spliterator if we're at the end
                        return Spliterators.emptySpliterator();
                    } else {
                        // Otherwise (only NOW), go fetch the next specific page of results needed
                        List<EntityAudit> audits;
                        try {
                            audits = response.getSpecificPage((int) start, (int) Math.min(end - start, pageSize));
                        } catch (AtlanException e) {
                            log.warn(
                                    "Unable to fetch the specific page from {} to {}",
                                    start,
                                    Math.min(end - start, pageSize),
                                    e);
                            audits = Collections.emptyList();
                        }
                        // And update this spliterator's starting point accordingly
                        sp = audits.spliterator();
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
    private static class AuditSearchResponseIterator implements Iterator<EntityAudit> {

        private AuditSearchResponse response;
        private int i;

        public AuditSearchResponseIterator(AuditSearchResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getEntityAudits() != null && response.getEntityAudits().size() > i) {
                return true;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getEntityAudits() != null
                            && response.getEntityAudits().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of search results.", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public EntityAudit next() {
            return response.getEntityAudits().get(i++);
        }
    }

    /**
     * Allow results to be iterated through without managing paging retrievals, and
     * without overwhelming system resources, even when iterating through 10,000's or more
     * results.
     */
    private static class AuditSearchResponseBulkIterator implements Iterator<EntityAudit> {

        private AuditSearchResponse response;
        // TODO: Consider optimizing memory using UUID.fromString() to store UUIDs rather than Strings
        private final Set<String> processedGuids;
        private int i;

        public AuditSearchResponseBulkIterator(AuditSearchResponse response) {
            try {
                IndexSearchDSL dsl = response.getRequest().getDsl();
                if (presortedByTimestamp(dsl.getSort())) {
                    // If the results are already sorted in ascending order by timestamp, proceed
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
            this.processedGuids = new HashSet<>();
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getEntityAudits() == null) {
                // If there are no results in this page, then there are no more results
                // so exit straightaway
                return false;
            }
            if (response.getEntityAudits().size() > i) {
                EntityAudit candidate = response.getEntityAudits().get(i);
                if (candidate != null && !processedGuids.contains(candidate.getEventKey())) {
                    return true;
                } else {
                    for (int j = i; j < response.getEntityAudits().size(); j++) {
                        candidate = response.getEntityAudits().get(j);
                        if (candidate != null && !processedGuids.contains(candidate.getEventKey())) {
                            i = j;
                            return true;
                        }
                    }
                }
            }
            try {
                response = response.getNextBulkPage();
                i = 0;
                return response.getEntityAudits() != null
                        && response.getEntityAudits().size() > i;
            } catch (AtlanException e) {
                throw new RuntimeException("Unable to iterate through all pages of search results.", e);
            }
        }

        /** {@inheritDoc} */
        @Override
        public EntityAudit next() {
            EntityAudit candidate = response.getEntityAudits().get(i++);
            processedGuids.add(candidate.getEventKey());
            return candidate;
        }
    }
}
