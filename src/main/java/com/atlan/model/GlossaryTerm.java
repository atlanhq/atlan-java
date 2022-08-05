/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTerm extends Asset {

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for terms. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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
     * @param name of the term
     * @param glossaryGuid unique identifier of the term's glossary
     * @param glossaryQualifiedName unique name of the term's glossary
     * @return the minimal request necessary to create the term
     */
    public static GlossaryTerm createRequest(String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryTerm.builder()
                .attributes(GlossaryTermAttributes.builder()
                        .qualifiedName(name)
                        .name(name)
                        .build())
                .relationshipAttributes(
                        GlossaryTermRelationshipAttributes.createRequest(glossaryGuid, glossaryQualifiedName))
                .build();
    }

    /**
     * Builds the minimal request necessary to update a term. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param qualifiedName of the term
     * @param name of the term
     * @param glossaryGuid unique identifier of the term's glossary
     * @return the minimal request necessary to update the term
     */
    public static GlossaryTerm updateRequest(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTerm.builder()
                .attributes(GlossaryTermAttributes.builder()
                        .qualifiedName(qualifiedName)
                        .name(name)
                        .build())
                .relationshipAttributes(GlossaryTermRelationshipAttributes.createRequest(glossaryGuid, null))
                .build();
    }
}
