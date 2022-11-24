/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Badge extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Badge";

    /** Fixed typeName for Badges. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    List<BadgeCondition> badgeConditions;

    /** TBC */
    @Attribute
    String badgeMetadataAttribute;

    /**
     * Reference to a Badge by GUID.
     *
     * @param guid the GUID of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByGuid(String guid) {
        return Badge.builder().guid(guid).build();
    }

    /**
     * Reference to a Badge by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByQualifiedName(String qualifiedName) {
        return Badge.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the minimal request necessary to update the Badge, as a builder
     */
    public static BadgeBuilder<?, ?> updater(String qualifiedName, String name) {
        return Badge.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Badge, from a potentially
     * more-complete Badge object.
     *
     * @return the minimal object necessary to update the Badge, as a builder
     */
    @Override
    protected BadgeBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Badge by its GUID, complete with all of its relationships.
     *
     * @param guid of the Badge to retrieve
     * @return the requested full Badge, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    public static Badge retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Badge) {
            return (Badge) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Badge.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Badge by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Badge to retrieve
     * @return the requested full Badge, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist
     */
    public static Badge retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Badge) {
            return (Badge) entity;
        } else {
            throw new NotFoundException(
                    "No Badge found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Restore the archived (soft-deleted) Badge to active.
     *
     * @param qualifiedName for the Badge
     * @return the Badge that was restored
     * @throws AtlanException on any API problems
     */
    public static Badge restore(String qualifiedName) throws AtlanException {
        return (Badge) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Badge)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Badge) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Badge) Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a Badge.
     *
     * @param qualifiedName of the Badge
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Badge, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Badge updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Badge) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Badge)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Badge.
     *
     * @param qualifiedName of the Badge
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Badge updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Badge) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Badge)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Badge.
     *
     * @param qualifiedName of the Badge
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Badge
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Badge
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Badge.
     *
     * @param qualifiedName for the Badge
     * @param name human-readable name of the Badge
     * @param terms the list of terms to replace on the Badge, or null to remove all terms from the Badge
     * @return the Badge that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Badge replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Badge) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Badge, without replacing existing terms linked to the Badge.
     * Note: this operation must make two API calls — one to retrieve the Badge's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Badge
     * @param terms the list of terms to append to the Badge
     * @return the Badge that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Badge appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Badge) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Badge, without replacing all existing terms linked to the Badge.
     * Note: this operation must make two API calls — one to retrieve the Badge's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Badge
     * @param terms the list of terms to remove from the Badge, which must be referenced by GUID
     * @return the Badge that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Badge removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Badge) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
