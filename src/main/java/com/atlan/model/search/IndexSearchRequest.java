/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.api.IndexSearchEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Class from which to configure and run a search against Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class IndexSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

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

    /** TBC */
    @Builder.Default
    Boolean showSearchScore = false;

    /** Whether to include term relationships for assets (false) or not (true). */
    @Builder.Default
    Boolean excludeMeanings = false;

    /** Whether to include classifications for assets (false) or not (true). */
    @Builder.Default
    Boolean excludeClassifications = false;

    /** Run the search. */
    public IndexSearchResponse search() throws AtlanException {
        return IndexSearchEndpoint.search(this);
    }
}
