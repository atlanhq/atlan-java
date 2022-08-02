/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class IndexSearchDSL extends AtlanObject {
  Integer from;
  Integer size;
  Aggregation aggregation;
  Query query;
  List<SortOptions> sort;
}
