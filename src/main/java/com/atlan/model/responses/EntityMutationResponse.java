/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.net.ApiResource;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityMutationResponse extends ApiResource {
  /** Assets that were changed. */
  MutatedEntities mutatedEntities;

  /** Map of assigned unique identifiers for the created assets. */
  Map<String, String> guidAssignments;
}
