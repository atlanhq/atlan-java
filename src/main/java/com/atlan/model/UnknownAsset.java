/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.core.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class is necessary to allow outputting a generic asset representation (with only those
 * attributes common to all assets), without causing reflection errors with GSON's native type
 * translation due to the `attributes` and `relationshipAttributes` fields appearing at all levels
 * of the inheritance hierarchy.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UnknownAsset extends Entity {

  /** Name of the type definition that defines this asset. */
  @Getter(onMethod_ = {@Override})
  @Setter(onMethod_ = {@Override})
  String typeName;

  /** Attributes that can exist across all assets in Atlan. */
  @Getter(onMethod_ = {@Override})
  AssetAttributes attributes;

  /** Relationships that can exist across all assets in Atlan. */
  @Getter(onMethod_ = {@Override})
  AssetRelationshipAttributes relationshipAttributes;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UnknownAsset;
  }
}
