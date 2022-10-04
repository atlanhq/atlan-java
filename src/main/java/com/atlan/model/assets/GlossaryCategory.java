/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
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
    Glossary anchor;

    /** Parent category in which this category is located (or null if this is a root-level category). */
    @Attribute
    GlossaryCategory parentCategory;

    /** Terms organized within this category. */
    @Singular
    @Attribute
    SortedSet<GlossaryTerm> terms;

    /** Child categories organized within this category. */
    @Singular("childCategory")
    @Attribute
    SortedSet<GlossaryCategory> childrenCategories;

    /**
     * Reference to a category by GUID.
     *
     * @param guid the GUID of the category to reference
     * @return reference to a category that can be used for defining a relationship to a category
     */
    public static GlossaryCategory refByGuid(String guid) {
        return GlossaryCategory.builder().guid(guid).build();
    }

    /**
     * Reference to a category by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the category to reference
     * @return reference to a category that can be used for defining a relationship to a category
     */
    public static GlossaryCategory refByQualifiedName(String qualifiedName) {
        return GlossaryCategory.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

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

    /**
     * Update the certificate on a category.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique ID (GUID) of the category's glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated category, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            String qualifiedName, String name, String glossaryGuid, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateCertificate(updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a category.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique ID (GUID) of the category's glossary
     * @return the updated category, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeCertificate(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a category.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique ID (GUID) of the category's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateAnnouncement(
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateAnnouncement(updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a category.
     *
     * @param qualifiedName of the category
     * @param name of the category
     * @param glossaryGuid unique ID (GUID) of the category's glossary
     * @return the updated category, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeAnnouncement(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Add classifications to a category.
     *
     * @param qualifiedName of the category
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the category
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a category.
     *
     * @param qualifiedName of the category
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the category
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
