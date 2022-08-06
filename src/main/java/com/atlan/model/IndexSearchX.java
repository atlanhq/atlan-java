/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.IndexSearchResponseX;
import com.atlan.net.ApiResource;
import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class IndexSearchX extends AtlanObject {
    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include on each result document. */
    @Singular
    List<String> attributes;

    /** Attributes to include on each related entity of each result document. */
    @Singular
    List<String> relationAttributes;

    /** Run the search. */
    public IndexSearchResponseX search() throws AtlanException {
        String url = String.format("%s%s", Atlan.getApiBase(), "/api/meta/search/indexsearch");
        return ApiResource.request(ApiResource.RequestMethod.POST, url, this, IndexSearchResponseX.class, null);
    }
}
