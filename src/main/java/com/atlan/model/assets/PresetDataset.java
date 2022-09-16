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
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Preset dataset in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PresetDataset extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetDataset";

    /** Fixed typeName for Preset datasets. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the data source for the dataset. */
    @Attribute
    String presetDatasetDatasourceName;

    /** ID of the dataset. */
    @Attribute
    Long presetDatasetId;

    /** Type of the dataset. */
    @Attribute
    String presetDatasetType;

    /** Collection in which the dataset exists. */
    @Attribute
    Reference presetDashboard;

    /**
     * Builds the minimal object necessary to create a Preset dataset.
     *
     * @param name of the dataset
     * @param collectionQualifiedName unique name of the collection in which the dataset exists
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String[] tokens = collectionQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetDataset.builder()
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetDashboardQualifiedName(collectionQualifiedName)
                .presetDashboard(Reference.by(PresetDashboard.TYPE_NAME, collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param name of the dataset
     * @return the minimal object necessary to update the dataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Preset dataset, from a potentially
     * more-complete Preset dataset object.
     *
     * @return the minimal object necessary to update the Preset dataset, as a builder
     */
    @Override
    protected PresetDatasetBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated dataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PresetDataset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param name of the dataset
     * @return the updated dataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PresetDataset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param name of the dataset
     * @return the updated dataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the dataset
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Preset dataset.
     *
     * @param qualifiedName of the dataset
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the dataset
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the dataset.
     *
     * @param qualifiedName for the dataset
     * @param name human-readable name of the dataset
     * @param terms the list of terms to replace on the dataset, or null to remove all terms from the dataset
     * @return the dataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset replaceTerms(String qualifiedName, String name, List<Reference> terms)
            throws AtlanException {
        return (PresetDataset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Preset dataset, without replacing existing terms linked to the dataset.
     * Note: this operation must make two API calls — one to retrieve the dataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the dataset
     * @param terms the list of terms to append to the dataset
     * @return the dataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset appendTerms(String qualifiedName, List<Reference> terms) throws AtlanException {
        return (PresetDataset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Preset dataset, without replacing all existing terms linked to the dataset.
     * Note: this operation must make two API calls — one to retrieve the dataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the dataset
     * @param terms the list of terms to remove from the dataset, which must be referenced by GUID
     * @return the dataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeTerms(String qualifiedName, List<GuidReference> terms) throws AtlanException {
        return (PresetDataset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
