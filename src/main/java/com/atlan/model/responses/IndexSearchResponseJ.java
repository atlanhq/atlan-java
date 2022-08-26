/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityJ;
import com.atlan.net.ApiResourceJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    /** Type of query. */
    String queryType;

    /** Parameters for the search. */
    Object searchParameters;

    /** List of results from the search. */
    List<EntityJ> entities;

    /** Approximate number of total results. */
    Long approximateCount;
}
