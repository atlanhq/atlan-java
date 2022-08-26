/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.api.IndexSearchEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.IndexSearchResponse;
import com.atlan.net.AtlanObjectJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class IndexSearchRequest extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include on each result document. */
    @Singular
    List<String> attributes;

    /** Attributes to include on each related entity of each result document. */
    @Singular
    List<String> relationAttributes;

    /** Run the search. */
    public IndexSearchResponse search() throws AtlanException {
        return IndexSearchEndpoint.search(this);
    }
}
