/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class GroupResponse extends ApiResource implements Iterable<AtlanGroup> {
    private static final long serialVersionUID = 2L;

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.ORDERED;

    /** Connectivity to the Atlan tenant where the search was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

    /** Request that produced this response. */
    @Setter
    @JsonIgnore
    GroupRequest request;

    /** Total number of groups. */
    Integer totalRecord;

    /** Filtered number of groups. */
    Integer filterRecord;

    /** Details about each group in this page of the response. */
    List<AtlanGroup> records;

    /**
     * Retrieve the next page of results from this response.
     *
     * @return next page of results from this response
     * @throws AtlanException on any API interaction problem
     */
    @JsonIgnore
    public GroupResponse getNextPage() throws AtlanException {
        int from = request.getOffset() < 0 ? 0 : request.getOffset();
        int page = request.getLimit() < 0 ? 20 : request.getLimit();
        GroupRequest next = request.toBuilder().offset(from + page).build();
        return next.list(client);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<AtlanGroup> iterator() {
        return new GroupResponseIterator(this);
    }

    /**
     * Stream the results (lazily) for processing without needing to manually manage paging.
     * @return a lazily-loaded stream of results from the search
     */
    public Stream<AtlanGroup> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), CHARACTERISTICS), false);
    }

    /**
     * Allow results to be iterated through without managing paging retrievals.
     */
    private static class GroupResponseIterator implements Iterator<AtlanGroup> {

        private GroupResponse response;
        private int i;

        public GroupResponseIterator(GroupResponse response) {
            this.response = response;
            this.i = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            if (response.getRecords() != null && response.getRecords().size() > i) {
                return true;
            } else {
                try {
                    response = response.getNextPage();
                    i = 0;
                    return response.getRecords() != null
                            && response.getRecords().size() > i;
                } catch (AtlanException e) {
                    throw new RuntimeException("Unable to iterate through all pages of search results.", e);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public AtlanGroup next() {
            return response.getRecords().get(i++);
        }
    }
}
