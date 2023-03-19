/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.api.AuditSearchEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Class from which to configure and run a search against Atlan's activity log.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AuditSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include in the entityDetail of each resulting audit entry. */
    @Singular
    List<String> attributes;

    /** Run the search. */
    public AuditSearchResponse search() throws AtlanException {
        return AuditSearchEndpoint.search(this);
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
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(size)
                        .sortOption(SortOptions.of(s ->
                                s.field(FieldSort.of(f -> f.field("created").order(SortOrder.Desc)))))
                        .query(BoolQuery.of(b -> b.filter(
                                        TermQuery.of(t -> t.field("entityId").value(guid))
                                                ._toQuery()))
                                ._toQuery())
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
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(size)
                        .sortOption(SortOptions.of(s ->
                                s.field(FieldSort.of(f -> f.field("created").order(SortOrder.Desc)))))
                        .query(BoolQuery.of(b -> b.must(List.of(
                                        TermQuery.of(t -> t.field("entityQualifiedName")
                                                        .value(qualifiedName))
                                                ._toQuery(),
                                        TermQuery.of(t -> t.field("typeName").value(typeName))
                                                ._toQuery())))
                                ._toQuery())
                        .build());
    }
}
