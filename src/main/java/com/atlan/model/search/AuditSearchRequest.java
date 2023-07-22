/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Class from which to configure and run a search against Atlan's activity log.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuditSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private static final SortOptions LATEST_FIRST =
            SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("created").order(SortOrder.Desc))));

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include in the entityDetail of each resulting audit entry. */
    @Singular
    List<String> attributes;

    /**
     * Run the search.
     *
     * @return the matching audit log records
     */
    public AuditSearchResponse search() throws AtlanException {
        return search(Atlan.getDefaultClient());
    }

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @return the matching audit log records
     */
    public AuditSearchResponse search(AtlanClient client) throws AtlanException {
        return client.assets.auditLogs(this);
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its GUID.
     *
     * @param guid unique identifier of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byGuid(String guid, int size) {
        return AuditSearchRequest.builder()
                .dsl(IndexSearchDSL.builder(BoolQuery.of(b -> b.filter(
                                        TermQuery.of(t -> t.field("entityId").value(guid))
                                                ._toQuery()))
                                ._toQuery())
                        .size(size)
                        .sortOption(LATEST_FIRST)
                        .build());
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its qualifiedName.
     *
     * @param typeName the type of asset for which to retrieve the audit history
     * @param qualifiedName unique name of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byQualifiedName(String typeName, String qualifiedName, int size) {
        return AuditSearchRequest.builder()
                .dsl(IndexSearchDSL.builder(BoolQuery.of(b -> b.must(List.of(
                                        TermQuery.of(t -> t.field("entityQualifiedName")
                                                        .value(qualifiedName))
                                                ._toQuery(),
                                        TermQuery.of(t -> t.field("typeName").value(typeName))
                                                ._toQuery())))
                                ._toQuery())
                        .size(size)
                        .sortOption(LATEST_FIRST)
                        .build());
    }

    /**
     * Start building an audit search request for the last changes made to any assets, by a given user.
     *
     * @param userName the name of the user for which to look for any changes
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byUser(String userName, int size) {
        return AuditSearchRequest.builder()
                .dsl(IndexSearchDSL.builder(BoolQuery.of(b -> b.must(List.of(
                                        TermQuery.of(t -> t.field("user").value(userName))
                                                ._toQuery())))
                                ._toQuery())
                        .size(size)
                        .sortOption(LATEST_FIRST)
                        .build());
    }
}
