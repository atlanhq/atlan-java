/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTerm extends Asset {

  /** Fixed typeName for terms. */
  @Getter(onMethod_ = {@Override})
  @Setter(onMethod_ = {@Override})
  @Builder.Default
  String typeName = "AtlasGlossaryTerm";

  /** Attributes for this term. */
  @Getter(onMethod_ = {@Override})
  GlossaryTermAttributes attributes;

  /** Map of the relationships to this term. */
  @Getter(onMethod_ = {@Override})
  GlossaryTermRelationshipAttributes relationshipAttributes;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof GlossaryTerm;
  }

  /**
   * Builds the minimal request necessary to create a term. At least one of glossaryGuid or
   * glossaryQualifiedName must be provided.
   *
   * @param qualifiedName of the term
   * @param name of the term
   * @param glossaryGuid unique identifier of the term's glossary
   * @param glossaryQualifiedName unique name of the term's glossary
   * @return the minimal request necessary to create the term
   */
  public static GlossaryTerm createRequest(
      String qualifiedName, String name, String glossaryGuid, String glossaryQualifiedName) {
    return GlossaryTerm.builder()
        .attributes(GlossaryTermAttributes.builder().qualifiedName(qualifiedName).name(name).build())
        .relationshipAttributes(
            GlossaryTermRelationshipAttributes.createRequest(glossaryGuid, glossaryQualifiedName))
        .build();
  }

  /**
   * Builds the minimal request necessary to update a term. At least one of guid or qualifiedName
   * must be provided.
   *
   * @param guid of the term
   * @param qualifiedName of the term
   * @param name of the term
   * @return the minimal request necessary to create the term
   */
  public static GlossaryTerm updateRequest(String guid, String qualifiedName, String name) {
    return GlossaryTerm.builder()
        .guid(guid)
        .attributes(GlossaryTermAttributes.builder().qualifiedName(qualifiedName).name(name).build())
        .build();
  }
}
