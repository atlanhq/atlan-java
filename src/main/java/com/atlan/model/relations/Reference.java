/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import com.atlan.net.AtlanObject;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Reference extends AtlanObject {

  /**
   * Unique identifier of the related entity. If the uniqueAttributes are not provided, this must be
   * provided.
   */
  String guid;

  /** Type of the related entity. */
  String typeName;

  /** Status of the related entity. */
  String entityStatus;

  /** Human-readable name of the related entity. */
  String displayText;

  /** Type of the relationship itself. */
  String relationshipType;

  /** Unique identifier of the relationship itself. */
  String relationshipGuid;

  /** Status of the relationship itself. */
  String relationshipStatus;

  /** Unused. */
  Map<String, Object> relationshipAttributes;

  /**
   * Attribute(s) that uniquely identify the related entity. If the guid is not provided, these must
   * be provided.
   */
  UniqueAttributes uniqueAttributes;
}
