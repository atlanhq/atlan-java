/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.UTMTags;
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
@ToString(callSuper = true)
@SuppressWarnings("serial")
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

    /**
     * Whether to include Atlan tag names for assets (true) or not (false). By default, this is true and
     * Atlan tag names are therefore included. Note that this can be set to true even when excludeAtlanTags
     * is set to false.
     */
    @Builder.Default
    @JsonProperty("includeClassificationNames")
    Boolean includeAtlanTagNames = true;

    /**
     * Whether to include deleted relationships to this asset (true) or not (false). By default, this is false
     * and therefore only active (not deleted) relationships will be included.
     */
    @Builder.Default
    Boolean allowDeletedRelations = false;

    /**
     * Whether to include relationship-level attributes for any relationships to each asset (true) or not (false).
     * By default, this is false and therefore relationship-level attributes are not included.
     */
    @Builder.Default
    Boolean requestRelationshipAttrsForSearch = false;

    /** Controls how the search is logged (if at all). */
    @Builder.Default
    Metadata requestMetadata =
            Metadata.builder().utmTag(UTMTags.PROJECT_SDK_JAVA.getValue()).build();

    /**
     * Whether to include lower-level search metadata in results (true) or not (false).
     * This must be true in order to use extensive paging (beyond built-in thresholds).
     */
    @Builder.Default
    Boolean showSearchMetadata = false;

    /** TBC */
    @Builder.Default
    Boolean showHighlights = false;

    /** Qualified name of a persona through which to restrict the results of the search. */
    String persona;

    /** Qualified name of a purpose through which to restrict the results of the search. */
    String purpose;

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @return the matching assets
     */
    public IndexSearchResponse search(AtlanClient client) throws AtlanException {
        return client.assets.search(this);
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Metadata extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Whether to log this search (true) or not (false). */
        @Builder.Default
        Boolean saveSearchLog = false;

        /** Tags to associate with the search request. */
        @Singular
        List<String> utmTags;

        /** Input in the searchbar when the search was run. */
        String searchInput;
    }

    public abstract static class IndexSearchRequestBuilder<
                    C extends IndexSearchRequest, B extends IndexSearchRequestBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
