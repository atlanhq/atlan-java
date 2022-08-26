/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a category in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryCategory extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryCategory";

    /** Fixed typeName for categories. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    @JsonIgnore
    String shortDescription;

    @JsonIgnore
    String longDescription;

    @JsonIgnore
    Map<String, String> additionalAttributes;

    /** Glossary in which the category is located. */
    @Attribute
    Reference anchor;

    /** Parent category in which this category is located (or null if this is a root-level category). */
    @Attribute
    Reference parentCategory;

    /** Terms organized within this category. */
    @Singular
    @Attribute
    Set<Reference> terms;

    /** Child categories organized within this category. */
    @Singular("childCategory")
    @Attribute
    Set<Reference> childrenCategories;

    /**
     * Builds the minimal object necessary for creating a category. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param name of the category
     * @param glossaryGuid unique identifier of the category's glossary
     * @param glossaryQualifiedName unique name of the category's glossary
     * @return the minimal object necessary to create the category, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> creator(
            String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryCategory.builder()
                .qualifiedName(name)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, glossaryQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a category. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique identifier of the category's glossary
     * @return the minimal object necessary to update the category, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> updater(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a category requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryCategory.builder()
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, null));
    }

    /**
     * Builds the minimal object necessary to apply an update to a category, from a potentially
     * more-complete category object.
     *
     * @return the minimal object necessary to update the category, as a builder
     */
    @Override
    protected GlossaryCategoryBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }
}
