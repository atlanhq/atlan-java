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
 * Instance of a Tableau project in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauProject extends Asset implements ITableauProject, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauProject";

    /** Fixed typeName for TableauProjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauProject> childProjects;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauDatasource> datasources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauFlow> flows;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isTopLevelProject;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    ITableauProject parentProject;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    ITableauSite site;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauWorkbook> workbooks;

    /**
     * Reference to a TableauProject by GUID.
     *
     * @param guid the GUID of the TableauProject to reference
     * @return reference to a TableauProject that can be used for defining a relationship to a TableauProject
     */
    public static TableauProject refByGuid(String guid) {
        return TableauProject.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauProject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauProject to reference
     * @return reference to a TableauProject that can be used for defining a relationship to a TableauProject
     */
    public static TableauProject refByQualifiedName(String qualifiedName) {
        return TableauProject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauProject by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist or the provided GUID is not a TableauProject
     */
    public static TableauProject retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a TableauProject by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist or the provided GUID is not a TableauProject
     */
    public static TableauProject retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauProject) {
            return (TableauProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauProject");
        }
    }

    /**
     * Retrieves a TableauProject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist
     */
    public static TableauProject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a TableauProject by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the TableauProject to retrieve
     * @return the requested full TableauProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauProject does not exist
     */
    public static TableauProject retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof TableauProject) {
            return (TableauProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauProject");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauProject to active.
     *
     * @param qualifiedName for the TableauProject
     * @return true if the TableauProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TableauProject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauProject
     * @return true if the TableauProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the minimal request necessary to update the TableauProject, as a builder
     */
    public static TableauProjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauProject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauProject, from a potentially
     * more-complete TableauProject object.
     *
     * @return the minimal object necessary to update the TableauProject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauProject are not found in the initial object
     */
    @Override
    public TableauProjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauProject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauProject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauProject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TableauProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauProject's owners
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauProject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauProject's certificate
     * @param qualifiedName of the TableauProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauProject)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TableauProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauProject's certificate
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauProject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauProject's announcement
     * @param qualifiedName of the TableauProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauProject)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TableauProject.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauProject's announcement
     * @param qualifiedName of the TableauProject
     * @param name of the TableauProject
     * @return the updated TableauProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauProject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauProject.
     *
     * @param qualifiedName for the TableauProject
     * @param name human-readable name of the TableauProject
     * @param terms the list of terms to replace on the TableauProject, or null to remove all terms from the TableauProject
     * @return the TableauProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauProject's assigned terms
     * @param qualifiedName for the TableauProject
     * @param name human-readable name of the TableauProject
     * @param terms the list of terms to replace on the TableauProject, or null to remove all terms from the TableauProject
     * @return the TableauProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauProject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauProject, without replacing existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to append to the TableauProject
     * @return the TableauProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TableauProject, without replacing existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauProject
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to append to the TableauProject
     * @return the TableauProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauProject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauProject, without replacing all existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to remove from the TableauProject, which must be referenced by GUID
     * @return the TableauProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauProject, without replacing all existing terms linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauProject
     * @param qualifiedName for the TableauProject
     * @param terms the list of terms to remove from the TableauProject, which must be referenced by GUID
     * @return the TableauProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauProject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauProject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauProject, without replacing existing Atlan tags linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauProject, without replacing existing Atlan tags linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauProject
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauProject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauProject, without replacing existing Atlan tags linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendAtlanTags(
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
     * Add Atlan tags to a TableauProject, without replacing existing Atlan tags linked to the TableauProject.
     * Note: this operation must make two API calls — one to retrieve the TableauProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauProject
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauProject
     */
    public static TableauProject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauProject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauProject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauProject
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauProject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauProject
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
     * Add Atlan tags to a TableauProject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauProject
     * @param qualifiedName of the TableauProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauProject
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
     * Remove an Atlan tag from a TableauProject.
     *
     * @param qualifiedName of the TableauProject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauProject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TableauProject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauProject
     * @param qualifiedName of the TableauProject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauProject
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
