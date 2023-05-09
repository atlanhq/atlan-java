/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Preset dataset in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PresetDataset extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetDataset";

    /** Fixed typeName for PresetDatasets. */
    @Getter(onMethod_ = {@Override})
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
    PresetDashboard presetDashboard;

    /**
     * Reference to a PresetDataset by GUID.
     *
     * @param guid the GUID of the PresetDataset to reference
     * @return reference to a PresetDataset that can be used for defining a relationship to a PresetDataset
     */
    public static PresetDataset refByGuid(String guid) {
        return PresetDataset.builder().guid(guid).build();
    }

    /**
     * Reference to a PresetDataset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PresetDataset to reference
     * @return reference to a PresetDataset that can be used for defining a relationship to a PresetDataset
     */
    public static PresetDataset refByQualifiedName(String qualifiedName) {
        return PresetDataset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PresetDataset by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetDataset to retrieve
     * @return the requested full PresetDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetDataset does not exist or the provided GUID is not a PresetDataset
     */
    public static PresetDataset retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PresetDataset) {
            return (PresetDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PresetDataset");
        }
    }

    /**
     * Retrieves a PresetDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PresetDataset to retrieve
     * @return the requested full PresetDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetDataset does not exist
     */
    public static PresetDataset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PresetDataset) {
            return (PresetDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PresetDataset");
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetDataset to active.
     *
     * @param qualifiedName for the PresetDataset
     * @return true if the PresetDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

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
                .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the minimal request necessary to update the PresetDataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetDataset, from a potentially
     * more-complete PresetDataset object.
     *
     * @return the minimal object necessary to update the PresetDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetDataset are not found in the initial object
     */
    @Override
    public PresetDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the updated PresetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the updated PresetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the updated PresetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PresetDataset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the updated PresetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
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
     * Remove the announcement from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the updated PresetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetDataset) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PresetDataset.
     *
     * @param qualifiedName for the PresetDataset
     * @param name human-readable name of the PresetDataset
     * @param terms the list of terms to replace on the PresetDataset, or null to remove all terms from the PresetDataset
     * @return the PresetDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PresetDataset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetDataset, without replacing existing terms linked to the PresetDataset.
     * Note: this operation must make two API calls — one to retrieve the PresetDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetDataset
     * @param terms the list of terms to append to the PresetDataset
     * @return the PresetDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetDataset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetDataset, without replacing all existing terms linked to the PresetDataset.
     * Note: this operation must make two API calls — one to retrieve the PresetDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetDataset
     * @param terms the list of terms to remove from the PresetDataset, which must be referenced by GUID
     * @return the PresetDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetDataset removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetDataset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a PresetDataset, without replacing existing classifications linked to the PresetDataset.
     * Note: this operation must make two API calls — one to retrieve the PresetDataset's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the PresetDataset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated PresetDataset
     */
    public static PresetDataset appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (PresetDataset) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a PresetDataset, without replacing existing classifications linked to the PresetDataset.
     * Note: this operation must make two API calls — one to retrieve the PresetDataset's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the PresetDataset
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetDataset
     */
    public static PresetDataset appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetDataset) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PresetDataset
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PresetDataset
     * @deprecated see {@link #appendClassifications(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove a classification from a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PresetDataset
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
