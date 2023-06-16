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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Preset workspace in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PresetWorkspace extends Asset implements IPresetWorkspace, IPreset, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetWorkspace";

    /** Fixed typeName for PresetWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long presetDashboardId;

    /** TBC */
    @Attribute
    String presetDashboardQualifiedName;

    /** ID of the cluster for the Preset workspace. */
    @Attribute
    Long presetWorkspaceClusterId;

    /** Number of collections in the workspace. */
    @Attribute
    Long presetWorkspaceDashboardCount;

    /** Number of datasets in the workspace. */
    @Attribute
    Long presetWorkspaceDatasetCount;

    /** ID of the deployment for the Preset workspace. */
    @Attribute
    Long presetWorkspaceDeploymentId;

    /** Hostname of the Preset workspace. */
    @Attribute
    String presetWorkspaceHostname;

    /** TBC */
    @Attribute
    Long presetWorkspaceId;

    /** Whether the workspace is in maintenance mode (true) or not (false). */
    @Attribute
    Boolean presetWorkspaceIsInMaintenanceMode;

    /** Whether public collections are allowed in the workspace (true) or not (false). */
    @Attribute
    Boolean presetWorkspacePublicDashboardsAllowed;

    /** TBC */
    @Attribute
    String presetWorkspaceQualifiedName;

    /** Region of the workspace. */
    @Attribute
    String presetWorkspaceRegion;

    /** Status of the workspace. */
    @Attribute
    String presetWorkspaceStatus;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Collections within this workspace. */
    @Attribute
    @Singular
    SortedSet<IPresetDashboard> presetDashboards;

    /**
     * Reference to a PresetWorkspace by GUID.
     *
     * @param guid the GUID of the PresetWorkspace to reference
     * @return reference to a PresetWorkspace that can be used for defining a relationship to a PresetWorkspace
     */
    public static PresetWorkspace refByGuid(String guid) {
        return PresetWorkspace.builder().guid(guid).build();
    }

    /**
     * Reference to a PresetWorkspace by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PresetWorkspace to reference
     * @return reference to a PresetWorkspace that can be used for defining a relationship to a PresetWorkspace
     */
    public static PresetWorkspace refByQualifiedName(String qualifiedName) {
        return PresetWorkspace.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PresetWorkspace by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetWorkspace to retrieve
     * @return the requested full PresetWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetWorkspace does not exist or the provided GUID is not a PresetWorkspace
     */
    public static PresetWorkspace retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PresetWorkspace) {
            return (PresetWorkspace) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PresetWorkspace");
        }
    }

    /**
     * Retrieves a PresetWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PresetWorkspace to retrieve
     * @return the requested full PresetWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetWorkspace does not exist
     */
    public static PresetWorkspace retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PresetWorkspace) {
            return (PresetWorkspace) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PresetWorkspace");
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetWorkspace to active.
     *
     * @param qualifiedName for the PresetWorkspace
     * @return true if the PresetWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset workspace.
     *
     * @param name of the workspace
     * @param connectionQualifiedName unique name of the connection through which the workspace is accessible
     * @return the minimal object necessary to create the workspace, as a builder
     */
    public static PresetWorkspaceBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return PresetWorkspace.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.PRESET);
    }

    /**
     * Builds the minimal object necessary to update a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the minimal request necessary to update the PresetWorkspace, as a builder
     */
    public static PresetWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetWorkspace.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetWorkspace, from a potentially
     * more-complete PresetWorkspace object.
     *
     * @return the minimal object necessary to update the PresetWorkspace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetWorkspace are not found in the initial object
     */
    @Override
    public PresetWorkspaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetWorkspace", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique Preset workspace name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param name for the workspace
     * @return a unique name for the workspace
     */
    private static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Remove the system description from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetWorkspace) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetWorkspace) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PresetWorkspace) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PresetWorkspace) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetWorkspace) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PresetWorkspace) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetWorkspace) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PresetWorkspace.
     *
     * @param qualifiedName for the PresetWorkspace
     * @param name human-readable name of the PresetWorkspace
     * @param terms the list of terms to replace on the PresetWorkspace, or null to remove all terms from the PresetWorkspace
     * @return the PresetWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetWorkspace) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetWorkspace, without replacing existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to append to the PresetWorkspace
     * @return the PresetWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetWorkspace) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetWorkspace, without replacing all existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to remove from the PresetWorkspace, which must be referenced by GUID
     * @return the PresetWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetWorkspace) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PresetWorkspace, without replacing existing Atlan tags linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetWorkspace
     */
    public static PresetWorkspace appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PresetWorkspace) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetWorkspace, without replacing existing Atlan tags linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetWorkspace
     */
    public static PresetWorkspace appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetWorkspace) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetWorkspace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetWorkspace
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
     * Remove an Atlan tag from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetWorkspace
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
