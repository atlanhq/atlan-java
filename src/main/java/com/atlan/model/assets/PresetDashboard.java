/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.GuidReference;
import com.atlan.model.relations.Reference;
import com.atlan.util.StringUtils;
import java.util.List;
import java.util.Set;
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

    /** Fixed typeName for Preset collections. */
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

    /** Workspace in which the collection exists. */
    @Attribute
    Reference presetWorkspace;

    /** Charts contained within the collection. */
    @Attribute
    @Singular
    Set<Reference> presetCharts;

    /** Datasets contained within the collection. */
    @Attribute
    @Singular
    Set<Reference> presetDatasets;

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
                .presetWorkspace(Reference.by(PresetWorkspace.TYPE_NAME, workspaceQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param name of the collection
     * @return the minimal object necessary to update the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Preset collection, from a potentially
     * more-complete Preset collection object.
     *
     * @return the minimal object necessary to update the Preset collection, as a builder
     */
    @Override
    protected PresetDashboardBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated collection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PresetDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param name of the collection
     * @return the updated collection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Preset collection.
     *
     * @param qualifiedName of the collection
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
     * Remove the announcement from a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param name of the collection
     * @return the updated collection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the collection
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Preset collection.
     *
     * @param qualifiedName of the collection
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the collection
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the collection.
     *
     * @param qualifiedName for the collection
     * @param name human-readable name of the collection
     * @param terms the list of terms to replace on the collection, or null to remove all terms from the collection
     * @return the collection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard replaceTerms(String qualifiedName, String name, List<Reference> terms)
            throws AtlanException {
        return (PresetDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Preset collection, without replacing existing terms linked to the collection.
     * Note: this operation must make two API calls — one to retrieve the collection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the collection
     * @param terms the list of terms to append to the collection
     * @return the collection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard appendTerms(String qualifiedName, List<Reference> terms) throws AtlanException {
        return (PresetDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Preset collection, without replacing all existing terms linked to the collection.
     * Note: this operation must make two API calls — one to retrieve the collection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the collection
     * @param terms the list of terms to remove from the collection, which must be referenced by GUID
     * @return the collection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDashboard removeTerms(String qualifiedName, List<GuidReference> terms) throws AtlanException {
        return (PresetDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
