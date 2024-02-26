/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.mesh;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBase;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.util.ObjectBuilder;
import jakarta.json.stream.JsonGenerator;
import javax.annotation.Nullable;

/**
 * Wrapper class to reimplement how filter criteria are output, specifically
 * for data products -- these require a `filter` as a nested map construct within
 * an outer bool, not an array (which is all the default Elastic client will serialize).
 */
public class FilterQuery extends QueryBase implements QueryVariant {

    private final Query filter;

    private FilterQuery(Builder builder) {
        super(builder);
        this.filter = builder.filter;
    }

    @Override
    public Query.Kind _queryKind() {
        return Query.Kind.Bool;
    }

    public final Query filter() {
        return this.filter;
    }

    @Override
    protected void serializeInternal(JsonGenerator generator, JsonpMapper mapper) {
        super.serializeInternal(generator, mapper);
        if (this.filter != null) {
            generator.writeKey("filter");
            this.filter.serialize(generator, mapper);
        }
    }

    public static class Builder extends QueryBase.AbstractBuilder<FilterQuery.Builder>
            implements ObjectBuilder<FilterQuery> {
        @Nullable
        private Query filter;

        public Builder() {}

        public final FilterQuery.Builder filter(@Nullable Query query) {
            this.filter = query;
            return this;
        }

        @Override
        protected FilterQuery.Builder self() {
            return this;
        }

        @Override
        public FilterQuery build() {
            this._checkSingleUse();
            return new FilterQuery(this);
        }

        public boolean hasClauses() {
            return this.filter != null;
        }
    }
}
