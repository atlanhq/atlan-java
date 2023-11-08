/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import java.util.List;

public interface IRelationSearchable {
    /**
     * Returns a query that will only match assets that have at least one active relationship for
     * the field.
     *
     * @return a query that will only match assets that have at least one active relationship for
     * the field.
     */
    Query hasAny();

    /**
     * Returns a query that will only match assets that have at least one active relationship for
     * the field.
     *
     * @param relationship name of the relationship to search
     * @return a query that will only match assets that have at least one active relationship for
     * the field.
     */
    static Query hasAny(final String relationship) {
        return NestedQuery.of(n -> n.path("relationshipList")
                        .query(BoolQuery.of(b -> b.must(List.of(
                                        TermQuery.of(t -> t.field("relationshipList.typeName")
                                                        .value(relationship))
                                                ._toQuery(),
                                        TermQuery.of(t -> t.field("relationshipList.status")
                                                        .value("ACTIVE"))
                                                ._toQuery())))
                                ._toQuery()))
                ._toQuery();
    }
}
