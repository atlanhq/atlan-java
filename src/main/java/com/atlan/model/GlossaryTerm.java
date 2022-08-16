/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a term in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTerm extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for terms. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    transient String shortDescription;

    transient String longDescription;
    transient List<String> examples;
    transient String abbreviation;
    transient String usage;
    transient Map<String, String> additionalAttributes;

    /** Glossary in which the term is located. */
    @Attribute
    Reference anchor;

    /** Assets that are attached to this term. */
    @Singular
    @Attribute
    List<Reference> assignedEntities;

    /** Categories within which this term is organized. */
    @Singular
    @Attribute
    List<Reference> categories;

    /** Linked glossary terms that may also be of interest. */
    @Singular("addToSeeAlso")
    @Attribute
    List<Reference> seeAlso;

    /** Glossary terms that have the same, or a very similar meaning in the same language. */
    @Singular
    @Attribute
    List<Reference> synonyms;

    /** Glossary terms that have the opposite (or near opposite) meaning in the same language. */
    @Singular
    @Attribute
    List<Reference> antonyms;

    /** These terms are preferred in place of this term. */
    @Singular
    @Attribute
    List<Reference> preferredTerms;

    /** These terms should be used instead of this term. */
    @Singular("addToReplacedBy")
    @Attribute
    List<Reference> replacedBy;

    /** These terms represent the same meaning, but each is in a different language. */
    @Singular
    @Attribute
    List<Reference> translatedTerms;

    /** Unused relationships. */
    transient List<Reference> classifies;

    /**
     * These terms each represent one of the valid values that could be assigned to a data item that has the meaning
     * described by this term.
     */
    @Singular("addToValidValuesFor")
    @Attribute
    List<Reference> validValuesFor;

    /** Remove the linked assets from the term, if any are set on the term. */
    public void removeAssignedEntities() {
        addNullField("assignedEntities");
    }

    /**
     * Builds the minimal object necessary for creating a term. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     * To continue adding to the object, call {@link #toBuilder()} on
     * the result and continue calling additional methods to add metadata followed by {@link GlossaryTermBuilder#build()}.
     *
     * @param name of the term
     * @param glossaryGuid unique identifier of the term's glossary
     * @param glossaryQualifiedName unique name of the term's glossary
     * @return the minimal request necessary to create the term
     */
    public static GlossaryTerm toCreate(String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryTerm.builder()
                .qualifiedName(name)
                .name(name)
                .anchor(anchorLink(glossaryGuid, glossaryQualifiedName))
                .build();
    }

    /**
     * Builds the minimal object necessary to update a term. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     * To continue adding to the object, call {@link #toBuilder()} on
     * the result and continue calling additional methods to add metadata followed by {@link GlossaryTermBuilder#build()}.
     *
     * @param qualifiedName of the term
     * @param name of the term
     * @param glossaryGuid unique identifier of the term's glossary
     * @return the minimal object necessary to update the term
     */
    public static GlossaryTerm toUpdate(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTerm.builder()
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(anchorLink(glossaryGuid, null))
                .build();
    }

    /**
     * Set up the minimal object required to reference a glossary. Only one of the following is required.
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
                    .typeName(Glossary.TYPE_NAME)
                    .guid(glossaryGuid)
                    .build();
        } else {
            anchor = Reference.builder()
                    .typeName(Glossary.TYPE_NAME)
                    .uniqueAttributes(UniqueAttributes.builder()
                            .qualifiedName(glossaryQualifiedName)
                            .build())
                    .build();
        }
        return anchor;
    }
}
