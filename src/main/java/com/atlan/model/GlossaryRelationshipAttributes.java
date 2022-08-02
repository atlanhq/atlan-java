/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GlossaryRelationshipAttributes extends AssetRelationshipAttributes {
  /** Terms within this glossary. */
  List<Reference> terms;

  /** Categories within this glossary. */
  List<Reference> categories;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof GlossaryRelationshipAttributes;
  }
}
