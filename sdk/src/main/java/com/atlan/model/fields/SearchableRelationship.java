/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for any relationship field in Atlan that can be searched.
 */
public class SearchableRelationship extends RelationField implements IRelationSearchable {

    @Getter(AccessLevel.PROTECTED)
    private final String relationshipName;

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param relationship name of the relationship type in the metastore
     */
    public SearchableRelationship(String atlan, String relationship) {
        super(atlan);
        this.relationshipName = relationship;
    }

    /** {@inheritDoc} */
    @Override
    public Query hasAny() {
        return IRelationSearchable.hasAny(getRelationshipName());
    }
}
