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
public class GlossaryCategoryRelationshipAttributes extends AssetRelationshipAttributes {

    /** Glossary in which the category is located. */
    Reference anchor;

    /** Parent category in which this category is located (or null if this is a root-level category). */
    Reference parentCategory;

    /** Terms organized within this category. */
    List<Reference> terms;

    /** Child categories organized within this category. */
    List<Reference> childrenCategories;

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof GlossaryCategoryRelationshipAttributes;
    }

    /**
     * Set up the minimal object required to create a category. Only one of the following is required.
     *
     * @param glossaryGuid unique identifier of the glossary for the category
     * @param glossaryQualifiedName unique name of the glossary
     * @return a builder that can be further extended with other metadata
     */
    public static GlossaryCategoryRelationshipAttributes createRequest(
            String glossaryGuid, String glossaryQualifiedName) {
        Reference anchor = null;
        if (glossaryGuid == null && glossaryQualifiedName == null) {
            return null;
        } else if (glossaryGuid != null) {
            anchor = Reference.builder()
                    .typeName("AtlasGlossary")
                    .guid(glossaryGuid)
                    .build();
        } else {
            anchor = Reference.builder()
                    .typeName("AtlasGlossary")
                    .uniqueAttributes(UniqueAttributes.builder()
                            .qualifiedName(glossaryQualifiedName)
                            .build())
                    .build();
        }
        return GlossaryCategoryRelationshipAttributes.builder().anchor(anchor).build();
    }
}
