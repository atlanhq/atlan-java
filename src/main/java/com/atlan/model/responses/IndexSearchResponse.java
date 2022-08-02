/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IndexSearchResponse extends ApiResource {
  /** Type of query. */
  String queryType;

  /** Parameters for the search. */
  Object searchParameters;

  /** List of results from the search. */
  List<Entity> entities;

  /** Approximate number of total results. */
  Long approximateCount;
}
