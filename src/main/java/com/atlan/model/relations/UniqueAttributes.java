/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import com.atlan.net.AtlanObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class UniqueAttributes extends AtlanObject {
  /** Unique name of the related entity. */
  @SerializedName("qualifiedName")
  String qualifiedName;
}
