/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MicroStrategy project in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MicroStrategyProject extends Asset
        implements IMicroStrategyProject, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyProject";

    /** Fixed typeName for MicroStrategyProjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Attributes contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyAttribute> microStrategyAttributes;

    /** TBC */
    @Attribute
    Long microStrategyCertifiedAt;

    /** TBC */
    @Attribute
    String microStrategyCertifiedBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** Cubes contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyCube> microStrategyCubes;

    /** Documents contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyDocument> microStrategyDocuments;

    /** Dossiers contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyDossier> microStrategyDossiers;

    /** Facts contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyFact> microStrategyFacts;

    /** TBC */
    @Attribute
    Boolean microStrategyIsCertified;

    /** TBC */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Metrics contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyMetric> microStrategyMetrics;

    /** TBC */
    @Attribute
    String microStrategyProjectName;

    /** TBC */
    @Attribute
    String microStrategyProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportQualifiedNames;

    /** Reports contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyReport> microStrategyReports;

    /** Visualizations contained within the project. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyVisualization> microStrategyVisualizations;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a MicroStrategyProject by GUID.
     *
     * @param guid the GUID of the MicroStrategyProject to reference
     * @return reference to a MicroStrategyProject that can be used for defining a relationship to a MicroStrategyProject
     */
    public static MicroStrategyProject refByGuid(String guid) {
        return MicroStrategyProject.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyProject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyProject to reference
     * @return reference to a MicroStrategyProject that can be used for defining a relationship to a MicroStrategyProject
     */
    public static MicroStrategyProject refByQualifiedName(String qualifiedName) {
        return MicroStrategyProject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyProject by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist or the provided GUID is not a MicroStrategyProject
     */
    public static MicroStrategyProject retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MicroStrategyProject by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist or the provided GUID is not a MicroStrategyProject
     */
    public static MicroStrategyProject retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyProject) {
            return (MicroStrategyProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyProject");
        }
    }

    /**
     * Retrieves a MicroStrategyProject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist
     */
    public static MicroStrategyProject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MicroStrategyProject by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist
     */
    public static MicroStrategyProject retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyProject) {
            return (MicroStrategyProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyProject");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyProject to active.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @return true if the MicroStrategyProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyProject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyProject
     * @return true if the MicroStrategyProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the minimal request necessary to update the MicroStrategyProject, as a builder
     */
    public static MicroStrategyProjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyProject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyProject, from a potentially
     * more-complete MicroStrategyProject object.
     *
     * @return the minimal object necessary to update the MicroStrategyProject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyProject are not found in the initial object
     */
    @Override
    public MicroStrategyProjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyProject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyProject's owners
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyProject's certificate
     * @param qualifiedName of the MicroStrategyProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyProject)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyProject's certificate
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyProject's announcement
     * @param qualifiedName of the MicroStrategyProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyProject)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyProject's announcement
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyProject.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param name human-readable name of the MicroStrategyProject
     * @param terms the list of terms to replace on the MicroStrategyProject, or null to remove all terms from the MicroStrategyProject
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyProject's assigned terms
     * @param qualifiedName for the MicroStrategyProject
     * @param name human-readable name of the MicroStrategyProject
     * @param terms the list of terms to replace on the MicroStrategyProject, or null to remove all terms from the MicroStrategyProject
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyProject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyProject, without replacing existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to append to the MicroStrategyProject
     * @return the MicroStrategyProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MicroStrategyProject, without replacing existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyProject
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to append to the MicroStrategyProject
     * @return the MicroStrategyProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyProject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyProject, without replacing all existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to remove from the MicroStrategyProject, which must be referenced by GUID
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyProject, without replacing all existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyProject
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to remove from the MicroStrategyProject, which must be referenced by GUID
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyProject
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyProject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(
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
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyProject
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyProject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyProject
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
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
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyProject
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
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
     * Remove an Atlan tag from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyProject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyProject
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyProject
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
