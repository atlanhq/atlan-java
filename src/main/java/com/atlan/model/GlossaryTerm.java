/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for terms. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    private final String shortDescription = "";

    private final String longDescription = "";
    private final List<String> examples = Collections.emptyList();
    private final String abbreviation = "";
    private final String usage = "";
    private final Map<String, String> additionalAttributes = Collections.emptyMap();

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
    Set<Reference> synonyms;

    /** Glossary terms that have the opposite (or near opposite) meaning in the same language. */
    @Singular
    @Attribute
    Set<Reference> antonyms;

    /** These terms are preferred in place of this term. */
    @Singular
    @Attribute
    Set<Reference> preferredTerms;

    /**
     * These terms should be used instead of this term.
     */
    @Singular("addToReplacedBy")
    @Attribute
    Set<Reference> replacedBy;

    /**
     * These terms represent the same meaning, but each is in a different language.
     */
    @Singular
    @Attribute
    Set<Reference> translatedTerms;

    /**
     * Unused relationships.
     */
    private final Set<Reference> classifies = Collections.emptySet();

    /**
     * These terms each represent one of the valid values that could be assigned to a data item that has the meaning
     * described by this term.
     */
    @Singular("addToValidValuesFor")
    @Attribute
    Set<Reference> validValuesFor;

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
    public static GlossaryTerm updateRequest(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTerm.builder()
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
