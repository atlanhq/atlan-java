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
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Preset collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PresetDashboard extends Asset implements IPresetDashboard, IPreset, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetDashboard";

    /** Fixed typeName for PresetDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Username of the user who last changed the collection. */
    @Attribute
    String presetDashboardChangedByName;

    /** TBC */
    @Attribute
    String presetDashboardChangedByURL;

    /** Number of charts within the collection. */
    @Attribute
    Long presetDashboardChartCount;

    /** TBC */
    @Attribute
    Long presetDashboardId;

    /** Whether the collection is managed externally (true) or not (false). */
    @Attribute
    Boolean presetDashboardIsManagedExternally;

    /** Whether the collection is published (true) or not (false). */
    @Attribute
    Boolean presetDashboardIsPublished;

    /** TBC */
    @Attribute
    String presetDashboardQualifiedName;

    /** URL to a thumbnail illustration of the collection. */
    @Attribute
    String presetDashboardThumbnailURL;

    /** TBC */
    @Attribute
    Long presetWorkspaceId;

    /** TBC */
    @Attribute
    String presetWorkspaceQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Charts contained within the collection. */
    @Attribute
    @Singular
    SortedSet<IPresetChart> presetCharts;

    /** Datasets contained within the collection. */
    @Attribute
    @Singular
    SortedSet<IPresetDataset> presetDatasets;

    /** Workspace in which the collection exists. */
    @Attribute
    IPresetWorkspace presetWorkspace;

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
     * Retrieves a PresetDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetDashboard to retrieve
     * @return the requested full PresetDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetDashboard does not exist or the provided GUID is not a PresetDashboard
     */
    public static PresetDashboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PresetDashboard) {
            return (PresetDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PresetDashboard");
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PresetDashboard) {
            return (PresetDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PresetDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetDashboard to active.
     *
     * @param qualifiedName for the PresetDashboard
     * @return true if the PresetDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
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
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetDashboard are not found in the initial object
     */
    @Override
    public PresetDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
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
        return (PresetDashboard) Asset.removeDescription(updater(qualifiedName, name));
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
        return (PresetDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (PresetDashboard) Asset.removeOwners(updater(qualifiedName, name));
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
    public static PresetDashboard updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
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
        return (PresetDashboard) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (PresetDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static PresetDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
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
    public static PresetDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
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
    public static PresetDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PresetDashboard, without replacing existing Atlan tags linked to the PresetDashboard.
     * Note: this operation must make two API calls — one to retrieve the PresetDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetDashboard
     */
    public static PresetDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PresetDashboard) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetDashboard, without replacing existing Atlan tags linked to the PresetDashboard.
     * Note: this operation must make two API calls — one to retrieve the PresetDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetDashboard
     */
    public static PresetDashboard appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetDashboard) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetDashboard
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
