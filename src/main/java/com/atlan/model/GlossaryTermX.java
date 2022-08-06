/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTermX extends AssetX {

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for terms. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Glossary in which the term is located. */
    @Attribute Reference anchor;

    /** Assets that are attached to this term. */
    @Singular
    @Attribute List<Reference> assignedEntities;

    /** Categories within which this term is organized. */
    @Singular
    @Attribute List<Reference> categories;

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
    public static GlossaryTermX createRequest(String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryTermX.builder()
            .qualifiedName(name)
            .name(name)
            .anchor(anchorLink(glossaryGuid, glossaryQualifiedName))
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
    public static GlossaryTermX updateRequest(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTermX.builder()
            .qualifiedName(qualifiedName)
            .name(name)
            .anchor(anchorLink(glossaryGuid, null))
            .build();
    }

    /**
     * Set up the minimal object required to create a term. Only one of the following is required.
     *
     * @param glossaryGuid unique identifier of the glossary for the term
     * @param glossaryQualifiedName unique name of the glossary
     * @return a builder that can be further extended with other metadata
     */
    static Reference anchorLink(String glossaryGuid, String glossaryQualifiedName) {
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
        return anchor;
    }
}
