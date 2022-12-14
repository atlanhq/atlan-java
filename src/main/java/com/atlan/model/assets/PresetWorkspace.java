/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Preset workspace in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PresetWorkspace extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetWorkspace";

    /** Fixed typeName for PresetWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether public collections are allowed in the workspace (true) or not (false). */
    @Attribute
    Boolean presetWorkspacePublicDashboardsAllowed;

    /** ID of the cluster for the Preset workspace. */
    @Attribute
    Long presetWorkspaceClusterId;

    /** Hostname of the Preset workspace. */
    @Attribute
    String presetWorkspaceHostname;

    /** Whether the workspace is in maintenance mode (true) or not (false). */
    @Attribute
    Boolean presetWorkspaceIsInMaintenanceMode;

    /** Region of the workspace. */
    @Attribute
    String presetWorkspaceRegion;

    /** Status of the workspace. */
    @Attribute
    String presetWorkspaceStatus;

    /** ID of the deployment for the Preset workspace. */
    @Attribute
    Long presetWorkspaceDeploymentId;

    /** Number of collections in the workspace. */
    @Attribute
    Long presetWorkspaceDashboardCount;

    /** Number of datasets in the workspace. */
    @Attribute
    Long presetWorkspaceDatasetCount;

    /** Collections within this workspace. */
    @Attribute
    @Singular
    SortedSet<PresetDashboard> presetDashboards;

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
    public static PresetWorkspace updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
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
     * Add classifications to a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PresetWorkspace
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PresetWorkspace.
     *
     * @param qualifiedName of the PresetWorkspace
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PresetWorkspace
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static PresetWorkspace replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PresetWorkspace) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetWorkspace, without replacing existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls ??? one to retrieve the PresetWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to append to the PresetWorkspace
     * @return the PresetWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetWorkspace) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetWorkspace, without replacing all existing terms linked to the PresetWorkspace.
     * Note: this operation must make two API calls ??? one to retrieve the PresetWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetWorkspace
     * @param terms the list of terms to remove from the PresetWorkspace, which must be referenced by GUID
     * @return the PresetWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetWorkspace removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetWorkspace) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
