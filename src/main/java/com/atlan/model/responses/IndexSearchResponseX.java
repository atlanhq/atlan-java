/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityX;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponseX extends ApiResource {
    /** Type of query. */
    String queryType;

    /** Parameters for the search. */
    Object searchParameters;

    /** List of results from the search. */
    List<EntityX> entities;

    /** Approximate number of total results. */
    Long approximateCount;
}
