/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.api.IndexSearchEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class from which to configure and run a search against Atlan.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
public class IndexSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Build a search using the provided query and default options.
     *
     * @param query the query to use for the search
     * @return the search request, with default options
     */
    public static IndexSearchRequestBuilder<?, ?> builder(Query query) {
        return builder(IndexSearchDSL.of(query));
    }

    /**
     * Build a search using the provided DSL and default options.
     *
     * @param dsl the query details to use for the search
     * @return the search request, with default options
     */
    public static IndexSearchRequestBuilder<?, ?> builder(IndexSearchDSL dsl) {
        return IndexSearchRequest._internal().dsl(dsl);
    }

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include on each result document. */
    @Singular
    List<String> attributes;

    /** Attributes to include on each related entity of each result document. */
    @Singular
    List<String> relationAttributes;

    /** TBC */
    @Builder.Default
    Boolean suppressLogs = true;

    /**
     * When true, include the score of each result. By default, this is false and scores are excluded.
     */
    @Builder.Default
    Boolean showSearchScore = false;

    /**
     * Whether to include term relationships for assets (false) or not (true). By default, this is false
     * and term relationships are therefore included.
     */
    @Builder.Default
    Boolean excludeMeanings = false;

    /**
     * Whether to include Atlan tags for assets (false) or not (true). By default, this is false and
     * Atlan tags are therefore included.
     */
    @Builder.Default
    @JsonProperty("excludeClassifications")
    Boolean excludeAtlanTags = false;

    /** Run the search. */
    public IndexSearchResponse search() throws AtlanException {
        return IndexSearchEndpoint.search(this);
    }
}
