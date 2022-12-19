/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
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
@SuppressWarnings("cast")
public class GlossaryCategory extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryCategory";

    /** Fixed typeName for GlossaryCategories. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Unused.
     * @see #description
     */
    @Attribute
    String shortDescription;

    /**
     * Unused.
     * @see #description
     */
    @Attribute
    String longDescription;

    /** Unused. */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** Terms organized within this category. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> terms;

    /** Glossary in which the category is located. */
    @Attribute
    Glossary anchor;

    /** Parent category in which this category is located (or null if this is a root-level category). */
    @Attribute
    GlossaryCategory parentCategory;

    /** Child categories organized within this category. */
    @Attribute
    @Singular("childCategory")
    SortedSet<GlossaryCategory> childrenCategories;

    /**
     * Reference to a GlossaryCategory by GUID.
     *
     * @param guid the GUID of the GlossaryCategory to reference
     * @return reference to a GlossaryCategory that can be used for defining a relationship to a GlossaryCategory
     */
    public static GlossaryCategory refByGuid(String guid) {
        return GlossaryCategory.builder().guid(guid).build();
    }

    /**
     * Reference to a GlossaryCategory by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GlossaryCategory to reference
     * @return reference to a GlossaryCategory that can be used for defining a relationship to a GlossaryCategory
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
     * Builds the minimal object necessary to apply an update to a GlossaryCategory, from a potentially
     * more-complete GlossaryCategory object.
     *
     * @return the minimal object necessary to update the GlossaryCategory, as a builder
     * @throws InvalidRequestException InvalidRequestException if any of the minimal set of required properties for GlossaryCategory are not found in the initial object
     */
    @Override
    public GlossaryCategoryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getAnchor() == null
                || this.getAnchor().getGuid() == null
                || this.getAnchor().getGuid().length() == 0) {
            missing.add("anchor.guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating GlossaryCategory is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Retrieves a GlossaryCategory by its GUID, complete with all of its relationships.
     *
     * @param guid of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     */
    public static GlossaryCategory retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof GlossaryCategory) {
            return (GlossaryCategory) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a GlossaryCategory.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a GlossaryCategory by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist
     */
    public static GlossaryCategory retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof GlossaryCategory) {
            return (GlossaryCategory) entity;
        } else {
            throw new NotFoundException(
                    "No GlossaryCategory found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) GlossaryCategory to active.
     *
     * @param qualifiedName for the GlossaryCategory
     * @return true if the GlossaryCategory is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeDescription(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeUserDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeUserDescription(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeOwners(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeOwners(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            String qualifiedName, String name, String glossaryGuid, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateCertificate(updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeCertificate(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the category
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryCategory, or null if the update failed
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
     * Remove the announcement from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryCategory) Asset.removeAnnouncement(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Add classifications to a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GlossaryCategory
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the GlossaryCategory
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
