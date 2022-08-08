/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public static final String TYPE_NAME = "AtlasGlossaryCategory";

    /** Fixed typeName for categories. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    private final String shortDescription = "";

    private final String longDescription = "";
    private final Map<String, String> additionalAttributes = Collections.emptyMap();

    /** Glossary in which the category is located. */
    @Attribute
    Reference anchor;

    /** Parent category in which this category is located (or null if this is a root-level category). */
    @Attribute
    Reference parentCategory;

    /** Terms organized within this category. */
    @Singular
    @Attribute
    List<Reference> terms;

    /** Child categories organized within this category. */
    @Singular("childCategory")
    @Attribute
    List<Reference> childrenCategories;

    /**
     * Builds the minimal object necessary for creating a category. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     * To continue adding to the object, call {@link #toBuilder()} on
     * the result and continue calling additional methods to add metadata followed by {@link GlossaryCategoryBuilder#build()}.
     *
     * @param name of the category
     * @param glossaryGuid unique identifier of the category's glossary
     * @param glossaryQualifiedName unique name of the category's glossary
     * @return the minimal object necessary to create the category
     */
    public static GlossaryCategory toCreate(String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryCategory.builder()
                .qualifiedName(name)
                .name(name)
                .anchor(GlossaryTerm.anchorLink(glossaryGuid, glossaryQualifiedName))
                .build();
    }

    /**
     * Builds the minimal object necessary to update a category. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     * To continue adding to the object, call {@link #toBuilder()} on
     * the result and continue calling additional methods to add metadata followed by {@link GlossaryCategoryBuilder#build()}.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique identifier of the category's glossary
     * @return the minimal object necessary to update the category
     */
    public static GlossaryCategory toUpdate(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a category requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryCategory.builder()
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(GlossaryTerm.anchorLink(glossaryGuid, null))
                .build();
    }
}
