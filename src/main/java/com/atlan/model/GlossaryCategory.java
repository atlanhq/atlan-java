/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryCategory extends Asset {

  /** Fixed typeName for categories. */
  @Getter(onMethod_ = {@Override})
  @Setter(onMethod_ = {@Override})
  @Builder.Default
  String typeName = "AtlasGlossaryCategory";

  /** Attributes for this category. */
  @Getter(onMethod_ = {@Override})
  GlossaryCategoryAttributes attributes;

  /** Map of the relationships to this category. */
  @Getter(onMethod_ = {@Override})
  GlossaryCategoryRelationshipAttributes relationshipAttributes;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof GlossaryCategory;
  }

  /**
   * Builds the minimal request necessary to create a category. At least one of glossaryGuid or
   * glossaryQualifiedName must be provided.
   *
   * @param qualifiedName of the category
   * @param name of the category
   * @param glossaryGuid unique identifier of the category's glossary
   * @param glossaryQualifiedName unique name of the category's glossary
   * @return the minimal request necessary to create the category
   */
  public static GlossaryCategory createRequest(
    String qualifiedName, String name, String glossaryGuid, String glossaryQualifiedName) {
    return GlossaryCategory.builder()
      .attributes(
        GlossaryCategoryAttributes.builder().qualifiedName(qualifiedName).name(name).build())
      .relationshipAttributes(
        GlossaryCategoryRelationshipAttributes.createRequest(glossaryGuid, glossaryQualifiedName))
      .build();
  }

  /**
   * Builds the minimal request necessary to update a category. At least one of glossaryGuid or
   * glossaryQualifiedName must be provided.
   *
   * @param qualifiedName of the category
   * @param name of the category
   * @param glossaryGuid unique identifier of the category's glossary
   * @param glossaryQualifiedName unique name of the category's glossary
   * @return the minimal request necessary to update the category
   */
  public static GlossaryCategory updateRequest(
    String qualifiedName, String name, String glossaryGuid, String glossaryQualifiedName) {
    // Turns out that updating a category requires exactly the same info as creating one
    // TODO: update using qualifiedName for the glossary fails...
    return createRequest(qualifiedName, name, glossaryGuid, glossaryQualifiedName);
  }
}
