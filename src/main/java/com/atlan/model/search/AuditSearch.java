/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.fields.AtlanField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan's
 * audit log (removing the need to understand the guts of Elastic).
 */
@SuperBuilder(builderMethodName = "_internal")
@SuppressWarnings("cast")
public class AuditSearch extends CompoundQuery {

    /**
     * Build an audit search against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @return the start of an audit search against the tenant
     */
    public static AuditSearchBuilder<?, ?> builder(AtlanClient client) {
        return _internal().client(client);
    }

    /** Client through which to retrieve the audit log. */
    AtlanClient client;

    /** Criteria by which to sort the results. */
    @Singular
    List<SortOptions> sorts;

    /**
     * Aggregations to run against the results of the search.
     * You provide any key you want to the map (you'll use it to look at the results of a specific aggregation).
     */
    @Singular("aggregate")
    Map<String, Aggregation> aggregations;

    /** Number of results to retrieve per underlying API request. */
    Integer pageSize;

    /** Attributes to retrieve for the entity detail in each audit log entry. */
    @Singular("includeOnResults")
    List<AtlanField> includesOnResults;

    /** Attributes to retrieve for the entity detail in each audit log entry (for internal use, unchecked!). */
    @Singular("_includeOnResults")
    List<String> _includesOnResults;

    /**
     * Translate the Atlan audit search into an Atlan search DSL builder.
     *
     * @return an Atlan search DSL builder that encapsulates the audit search
     */
    protected IndexSearchDSL.IndexSearchDSLBuilder<?, ?> _dsl() {
        return IndexSearchDSL.builder(toQuery());
    }

    /**
     * Translate the Atlan audit search into an Atlan audit search request builder.
     *
     * @return an Atlan audit search request builder that encapsulates the audit search
     */
    protected AuditSearchRequest.AuditSearchRequestBuilder<?, ?> _requestBuilder() {
        IndexSearchDSL.IndexSearchDSLBuilder<?, ?> dsl = _dsl();
        if (pageSize != null) {
            dsl.size(pageSize);
        }
        if (sorts != null) {
            dsl.sort(sorts);
        }
        if (aggregations != null) {
            dsl.aggregations(aggregations);
        }
        AuditSearchRequest.AuditSearchRequestBuilder<?, ?> request =
                AuditSearchRequest.builder().dsl(dsl.build());
        if (_includesOnResults != null) {
            request.attributes(_includesOnResults);
        }
        if (includesOnResults != null) {
            request.attributes(includesOnResults.stream()
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList()));
        }
        return request;
    }

    public abstract static class AuditSearchBuilder<C extends AuditSearch, B extends AuditSearchBuilder<C, B>>
            extends CompoundQueryBuilder<C, B> {

        /**
         * Translate the Atlan audit search into an Atlan audit search request builder.
         *
         * @return an Atlan audit search request builder that encapsulates the audit search
         */
        public AuditSearchRequest.AuditSearchRequestBuilder<?, ?> toRequestBuilder() {
            return build()._requestBuilder();
        }

        /**
         * Translate the Atlan audit search into an Atlan audit search request.
         *
         * @return an Atlan audit search request that encapsulates the audit search
         */
        public AuditSearchRequest toRequest() {
            return toRequestBuilder().build();
        }

        /**
         * Return the total number of audit entries that will match the supplied criteria,
         * using the most minimal query possible (retrieves minimal data).
         *
         * @return the count of audit entries that will match the supplied criteria
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public long count() throws AtlanException {
            if (client == null) {
                throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
            }
            // As long as there is a client, build the search request for just a single result (with count)
            // and then just return the count
            AuditSearchRequest request = AuditSearchRequest.builder()
                    .dsl(build()._dsl().size(1).clearAggregations().build())
                    .build();
            return request.search(client).getTotalCount();
        }

        /**
         * Run the audit search to retrieve audit log entries that match the supplied criteria.
         *
         * @return a stream of audit log entries that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<EntityAudit> stream() throws AtlanException {
            return stream(false);
        }

        /**
         * Run the audit search to retrieve audit log entries that match the supplied criteria.
         *
         * @param parallel if true, returns a parallel stream
         * @return a stream of audit log entries that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<EntityAudit> stream(boolean parallel) throws AtlanException {
            if (client == null) {
                throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
            }
            AuditSearchRequest request = toRequest();
            if (parallel) {
                return request.search(client).parallelStream();
            } else {
                return request.search(client).stream();
            }
        }
    }
}
