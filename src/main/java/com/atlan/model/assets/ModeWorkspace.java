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
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ModeWorkspace extends Mode {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeWorkspace";

    /** Fixed typeName for ModeWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long modeCollectionCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ModeCollection> modeCollections;

    /**
     * Reference to a ModeWorkspace by GUID.
     *
     * @param guid the GUID of the ModeWorkspace to reference
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByGuid(String guid) {
        return ModeWorkspace.builder().guid(guid).build();
    }

    /**
     * Reference to a ModeWorkspace by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeWorkspace to reference
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByQualifiedName(String qualifiedName) {
        return ModeWorkspace.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the minimal request necessary to update the ModeWorkspace, as a builder
     */
    public static ModeWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeWorkspace.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeWorkspace, from a potentially
     * more-complete ModeWorkspace object.
     *
     * @return the minimal object necessary to update the ModeWorkspace, as a builder
     */
    @Override
    protected ModeWorkspaceBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ModeWorkspace by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     */
    public static ModeWorkspace retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof ModeWorkspace) {
            return (ModeWorkspace) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a ModeWorkspace.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a ModeWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist
     */
    public static ModeWorkspace retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof ModeWorkspace) {
            return (ModeWorkspace) entity;
        } else {
            throw new NotFoundException(
                    "No ModeWorkspace found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeWorkspace to active.
     *
     * @param qualifiedName for the ModeWorkspace
     * @return true if the ModeWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeWorkspace)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeWorkspace) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ModeWorkspace)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (ModeWorkspace) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ModeWorkspace)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ModeWorkspace) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ModeWorkspace)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ModeWorkspace
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ModeWorkspace
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ModeWorkspace.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param name human-readable name of the ModeWorkspace
     * @param terms the list of terms to replace on the ModeWorkspace, or null to remove all terms from the ModeWorkspace
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ModeWorkspace) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeWorkspace, without replacing existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to append to the ModeWorkspace
     * @return the ModeWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeWorkspace) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeWorkspace, without replacing all existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to remove from the ModeWorkspace, which must be referenced by GUID
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeWorkspace) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
