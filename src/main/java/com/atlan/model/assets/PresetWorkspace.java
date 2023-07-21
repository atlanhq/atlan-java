/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
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
@ToString(callSuper = true)
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
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    Long presetDashboardId;

    /** TBC */
    @Attribute
    String presetDashboardQualifiedName;

    /** Collections within this workspace. */
    @Attribute
    @Singular
    SortedSet<IPresetDashboard> presetDashboards;

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

    /**
     * Start an asset filter that will return all PresetWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) PresetWorkspace assets will be included.
     *
     * @return an asset filter that includes all PresetWorkspace assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all PresetWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) PresetWorkspaces will be included
     * @return an asset filter that includes all PresetWorkspace assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

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
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a PresetWorkspace by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the PresetWorkspace to retrieve
     * @return the requested full PresetWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetWorkspace does not exist or the provided GUID is not a PresetWorkspace
     */
    public static PresetWorkspace retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
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
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a PresetWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the PresetWorkspace to retrieve
     * @return the requested full PresetWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetWorkspace does not exist
     */
    public static PresetWorkspace retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
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
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) PresetWorkspace to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the PresetWorkspace
     * @return true if the PresetWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
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
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeDescription(client, updater(qualifiedName, name));
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
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeUserDescription(client, updater(qualifiedName, name));
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
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetWorkspace's owners
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeOwners(client, updater(qualifiedName, name));
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
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetWorkspace's certificate
     * @param qualifiedName of the PresetWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PresetWorkspace)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetWorkspace's certificate
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeCertificate(client, updater(qualifiedName, name));
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
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetWorkspace's announcement
     * @param qualifiedName of the PresetWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (PresetWorkspace)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan client from which to remove the PresetWorkspace's announcement
     * @param qualifiedName of the PresetWorkspace
     * @param name of the PresetWorkspace
     * @return the updated PresetWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeAnnouncement(client, updater(qualifiedName, name));
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
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to replace the PresetWorkspace's assigned terms
     * @param qualifiedName for the PresetWorkspace
     * @param name human-readable name of the PresetWorkspace
     * @param terms the list of terms to replace on the PresetWorkspace, or null to remove all terms from the PresetWorkspace
     * @return the PresetWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetWorkspace) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
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
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the PresetWorkspace, without replacing existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the PresetWorkspace
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to append to the PresetWorkspace
     * @return the PresetWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetWorkspace) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
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
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetWorkspace, without replacing all existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the PresetWorkspace
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to remove from the PresetWorkspace, which must be referenced by GUID
     * @return the PresetWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetWorkspace) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
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
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetWorkspace, without replacing existing Atlan tags linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetWorkspace
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetWorkspace
     */
    public static PresetWorkspace appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PresetWorkspace) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PresetWorkspace, without replacing existing Atlan tags linked to the PresetWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PresetWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetWorkspace
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetWorkspace
     */
    public static PresetWorkspace appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetWorkspace) Asset.appendAtlanTags(
                client,
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
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the PresetWorkspace
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetWorkspace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the PresetWorkspace
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
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
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
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a PresetWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a PresetWorkspace
     * @param qualifiedName of the PresetWorkspace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetWorkspace
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
