/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GlossaryTermRelationshipAttributes extends AssetRelationshipAttributes {

  /** Glossary in which the term is located. */
  Reference anchor;

  /** Assets that are attached to this term. */
  List<Reference> assignedEntities;

  /** Categories within which this term is organized. */
  List<Reference> categories;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof GlossaryTermRelationshipAttributes;
  }

  /**
   * Set up the minimal object required to create a term. Only one of the following is required.
   *
   * @param glossaryGuid unique identifier of the glossary for the term
   * @param glossaryQualifiedName unique name of the glossary
   * @return a builder that can be further extended with other metadata
   */
  public static GlossaryTermRelationshipAttributes createRequest(
      String glossaryGuid, String glossaryQualifiedName) {
    Reference anchor = null;
    if (glossaryGuid == null && glossaryQualifiedName == null) {
      return null;
    } else if (glossaryGuid != null) {
      anchor = Reference.builder().typeName("AtlasGlossary").guid(glossaryGuid).build();
    } else {
      anchor =
          Reference.builder()
              .typeName("AtlasGlossary")
              .uniqueAttributes(
                  UniqueAttributes.builder().qualifiedName(glossaryQualifiedName).build())
              .build();
    }
    return GlossaryTermRelationshipAttributes.builder().anchor(anchor).build();
  }
}
