/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.UTMTags;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Set of search parameters that are returned as part of the response of a search against Atlan.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SearchParameters extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** List of attributes that were requested in the search. */
    List<String> attributes;

    /** List of attributes that were requested to be included on each relationship found in the search. */
    List<String> relationAttributes;

    /** Whether to show the score associated with a result (true) or not (false). */
    Boolean showSearchScore;

    /** TBC */
    Boolean suppressLogs;

    /** Whether the term relationships for assets are excluded from the results (true) or not (false). */
    Boolean excludeMeanings;

    /** Whether the Atlan tags of assets are excluded from the results (true) or not (false). */
    @JsonProperty("excludeClassifications")
    Boolean excludeAtlanTags;

    /**
     * Whether to include Atlan tag names for assets (true) or not (false). By default, this is true and
     * Atlan tag names are therefore included. Note that this can be set to true even when excludeAtlanTags
     * is set to false.
     */
    @JsonProperty("includeClassificationNames")
    Boolean includeAtlanTagNames;

    /**
     * Whether to fully restrict results of the search based on the policies of the requestor.
     */
    Boolean enableFullRestriction;

    /** TBC */
    Boolean accessControlExclusive;

    /**
     * Whether to include deleted relationships to this asset (true) or not (false). By default, this is false
     * and therefore only active (not deleted) relationships will be included.
     */
    Boolean allowDeletedRelations;

    /**
     * Whether to include relationship-level attributes for any relationships to each asset (true) or not (false).
     * By default, this is false and therefore relationship-level attributes are not included.
     * @deprecated see {@link #includeRelationshipAttributes} instead
     */
    @Deprecated
    Boolean requestRelationshipAttrsForSearch;

    /**
     * Whether to include relationship-level attributes for any relationships to each asset (true) or not (false).
     * By default, this is false and therefore relationship-level attributes are not included.
     */
    Boolean includeRelationshipAttributes;

    /**
     * Whether to include lower-level search metadata in results (true) or not (false).
     * This must be true in order to use extensive paging (beyond built-in thresholds).
     */
    Boolean showSearchMetadata;

    /** TBC */
    Boolean showHighlights;

    /**
     * Query that was run to produce these search results.
     * Note that this is in string form, rather than a JSON object, but is equivalent content
     * to {@link IndexSearchDSL}.
     */
    String query;
}
