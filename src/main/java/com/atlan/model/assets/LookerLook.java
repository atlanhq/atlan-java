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
public class LookerLook extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerLook";

    /** Fixed typeName for LookerLooks. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String folderName;

    /** TBC */
    @Attribute
    Integer sourceUserId;

    /** TBC */
    @Attribute
    Integer sourceViewCount;

    /** TBC */
    @Attribute
    Integer sourcelastUpdaterId;

    /** TBC */
    @Attribute
    Long sourceLastAccessedAt;

    /** TBC */
    @Attribute
    Long sourceLastViewedAt;

    /** TBC */
    @Attribute
    Integer sourceContentMetadataId;

    /** TBC */
    @Attribute
    Integer sourceQueryId;

    /** TBC */
    @Attribute
    String modelName;

    /** TBC */
    @Attribute
    LookerFolder folder;

    /** TBC */
    @Attribute
    LookerQuery query;

    /** TBC */
    @Attribute
    LookerTile tile;

    /** TBC */
    @Attribute
    LookerModel model;

    /** TBC */
    @Attribute
    LookerDashboard dashboard;

    /**
     * Reference to a LookerLook by GUID.
     *
     * @param guid the GUID of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByGuid(String guid) {
        return LookerLook.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerLook by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByQualifiedName(String qualifiedName) {
        return LookerLook.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the minimal request necessary to update the LookerLook, as a builder
     */
    public static LookerLookBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerLook.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerLook, from a potentially
     * more-complete LookerLook object.
     *
     * @return the minimal object necessary to update the LookerLook, as a builder
     */
    @Override
    protected LookerLookBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerLook by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    public static LookerLook retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof LookerLook) {
            return (LookerLook) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a LookerLook.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a LookerLook by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist
     */
    public static LookerLook retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof LookerLook) {
            return (LookerLook) entity;
        } else {
            throw new NotFoundException(
                    "No LookerLook found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerLook to active.
     *
     * @param qualifiedName for the LookerLook
     * @return the LookerLook that was restored
     * @throws AtlanException on any API problems
     */
    public static LookerLook restore(String qualifiedName) throws AtlanException {
        return (LookerLook) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerLook)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerLook) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerLook)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerLook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerLook) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerLook)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerLook) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerLook)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerLook
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerLook
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerLook.
     *
     * @param qualifiedName for the LookerLook
     * @param name human-readable name of the LookerLook
     * @param terms the list of terms to replace on the LookerLook, or null to remove all terms from the LookerLook
     * @return the LookerLook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerLook) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerLook, without replacing existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to append to the LookerLook
     * @return the LookerLook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerLook) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerLook, without replacing all existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to remove from the LookerLook, which must be referenced by GUID
     * @return the LookerLook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerLook) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
