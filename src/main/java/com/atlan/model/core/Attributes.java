/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Attributes extends AtlanObject {
  /**
   * Unique name for this entity. This is typically a concatenation of the asset's name onto its
   * parent's qualifiedName.
   */
  String qualifiedName;

  /** Human-readable name of the asset. */
  String name;
}
