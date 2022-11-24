/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Preset collection in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PresetDashboard extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetDashboard";

    /** Fixed typeName for PresetDashboards. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Username of the user who last changed the collection. */
    @Attribute
    String presetDashboardChangedByName;

    /** TBC */
    @Attribute
    String presetDashboardChangedByURL;

    /** Whether the collection is managed externally (true) or not (false). */
    @Attribute
    Boolean presetDashboardIsManagedExternally;

    /** Whether the collection is published (true) or not (false). */
    @Attribute
    Boolean presetDashboardIsPublished;

    /** URL to a thumbnail illustration of the collection. */
    @Attribute
    String presetDashboardThumbnailURL;

    /** Number of charts within the collection. */
    @Attribute
    Long presetDashboardChartCount;

    /** Datasets contained within the collection. */
    @Attribute
    @Singular
    SortedSet<PresetDataset> presetDatasets;

    /** Charts contained within the collection. */
    @Attribute
    @Singular
    SortedSet<PresetChart> presetCharts;

    /** Workspace in which the collection exists. */
    @Attribute
    PresetWorkspace presetWorkspace;

    /**
     * Reference to a PresetDashboard by GUID.
     *
     * @param guid the GUID of the PresetDashboard to reference
     * @return reference to a PresetDashboard that can be used for defining a relationship to a PresetDashboard
     */
    public static PresetDashboard refByGuid(String guid) {
        return PresetDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a PresetDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PresetDashboard to reference
     * @return reference to a PresetDashboard that can be used for defining a relationship to a PresetDashboard
     */
    public static PresetDashboard refByQualifiedName(String qualifiedName) {
        return PresetDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspaceQualifiedName unique name of the workspace in which the collection exists
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        String[] tokens = workspaceQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetDashboard.builder()
                .name(name)
                .qualifiedName(workspaceQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .presetWorkspace(PresetWorkspace.refByQualifiedName(workspaceQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the minimal request necessary to update the PresetDashboard, as a builder
     */
    public static PresetDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetDashboard, from a potentially
     * more-complete PresetDashboard object.
     *
     * @return the minimal object necessary to update the PresetDashboard, as a builder
     */
    @Override
    protected PresetDashboardBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PresetDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetDashboard to retrieve
     * @return the requested full PresetDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetDashboard does not exist or the provided GUID is not a PresetDashboard
     */
    public static PresetDashboard retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PresetDashboard) {
            return (PresetDashboard) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PresetDashboard.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PresetDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PresetDashboard to retrieve
     * @return the requested full PresetDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetDashboard does not exist
     */
    public static PresetDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PresetDashboard) {
            return (PresetDashboard) entity;
        } else {
            throw new NotFoundException(
                    "No PresetDashboard found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetDashboard to active.
     *
     * @param qualifiedName for the PresetDashboard
     * @return the PresetDashboard that was restored
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard restore(String qualifiedName) throws AtlanException {
        return (PresetDashboard) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the updated PresetDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the updated PresetDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the updated PresetDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PresetDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the updated PresetDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PresetDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the updated PresetDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PresetDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PresetDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PresetDashboard.
     *
     * @param qualifiedName for the PresetDashboard
     * @param name human-readable name of the PresetDashboard
     * @param terms the list of terms to replace on the PresetDashboard, or null to remove all terms from the PresetDashboard
     * @return the PresetDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PresetDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetDashboard, without replacing existing terms linked to the PresetDashboard.
     * Note: this operation must make two API calls — one to retrieve the PresetDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetDashboard
     * @param terms the list of terms to append to the PresetDashboard
     * @return the PresetDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetDashboard, without replacing all existing terms linked to the PresetDashboard.
     * Note: this operation must make two API calls — one to retrieve the PresetDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetDashboard
     * @param terms the list of terms to remove from the PresetDashboard, which must be referenced by GUID
     * @return the PresetDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
