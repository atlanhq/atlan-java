/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Set of search parameters that are returned as part of the response of a search against Atlan.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class SearchParameters extends AtlanObject {

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

    /** Whether the classifications of assets are excluded from the results (true) or not (false). */
    Boolean excludeClassifications;

    /** TBC */
    Boolean allowDeletedRelations;

    /**
     * Query that was run to produce these search results.
     * Note that this is in string form, rather than a JSON object, but is equivalent content
     * to {@link IndexSearchDSL}.
     */
    String query;
}
