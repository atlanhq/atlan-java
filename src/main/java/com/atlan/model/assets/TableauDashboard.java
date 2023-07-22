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
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Tableau dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauDashboard extends Asset
        implements ITableauDashboard, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauDashboard";

    /** Fixed typeName for TableauDashboards. */
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
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    ITableauWorkbook workbook;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauWorksheet> worksheets;

    /**
     * Start an asset filter that will return all TableauDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauDashboard assets will be included.
     *
     * @return an asset filter that includes all TableauDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all TableauDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all TableauDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all TableauDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) TableauDashboards will be included
     * @return an asset filter that includes all TableauDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all TableauDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauDashboards will be included
     * @return an asset filter that includes all TableauDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a TableauDashboard by GUID.
     *
     * @param guid the GUID of the TableauDashboard to reference
     * @return reference to a TableauDashboard that can be used for defining a relationship to a TableauDashboard
     */
    public static TableauDashboard refByGuid(String guid) {
        return TableauDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauDashboard to reference
     * @return reference to a TableauDashboard that can be used for defining a relationship to a TableauDashboard
     */
    public static TableauDashboard refByQualifiedName(String qualifiedName) {
        return TableauDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist or the provided GUID is not a TableauDashboard
     */
    public static TableauDashboard retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a TableauDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist or the provided GUID is not a TableauDashboard
     */
    public static TableauDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauDashboard) {
            return (TableauDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauDashboard");
        }
    }

    /**
     * Retrieves a TableauDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist
     */
    public static TableauDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a TableauDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist
     */
    public static TableauDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof TableauDashboard) {
            return (TableauDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauDashboard to active.
     *
     * @param qualifiedName for the TableauDashboard
     * @return true if the TableauDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TableauDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauDashboard
     * @return true if the TableauDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the minimal request necessary to update the TableauDashboard, as a builder
     */
    public static TableauDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauDashboard, from a potentially
     * more-complete TableauDashboard object.
     *
     * @return the minimal object necessary to update the TableauDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauDashboard are not found in the initial object
     */
    @Override
    public TableauDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauDashboard's owners
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauDashboard's certificate
     * @param qualifiedName of the TableauDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauDashboard)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauDashboard's certificate
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauDashboard's announcement
     * @param qualifiedName of the TableauDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauDashboard)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TableauDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauDashboard's announcement
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauDashboard.
     *
     * @param qualifiedName for the TableauDashboard
     * @param name human-readable name of the TableauDashboard
     * @param terms the list of terms to replace on the TableauDashboard, or null to remove all terms from the TableauDashboard
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauDashboard's assigned terms
     * @param qualifiedName for the TableauDashboard
     * @param name human-readable name of the TableauDashboard
     * @param terms the list of terms to replace on the TableauDashboard, or null to remove all terms from the TableauDashboard
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauDashboard, without replacing existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to append to the TableauDashboard
     * @return the TableauDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TableauDashboard, without replacing existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauDashboard
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to append to the TableauDashboard
     * @return the TableauDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDashboard, without replacing all existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to remove from the TableauDashboard, which must be referenced by GUID
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDashboard, without replacing all existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauDashboard
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to remove from the TableauDashboard, which must be referenced by GUID
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauDashboard, without replacing existing Atlan tags linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauDashboard
     */
    public static TableauDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDashboard, without replacing existing Atlan tags linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauDashboard
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauDashboard
     */
    public static TableauDashboard appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDashboard, without replacing existing Atlan tags linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauDashboard
     */
    public static TableauDashboard appendAtlanTags(
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
     * Add Atlan tags to a TableauDashboard, without replacing existing Atlan tags linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauDashboard
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauDashboard
     */
    public static TableauDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauDashboard
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDashboard
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
     * Add Atlan tags to a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauDashboard
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDashboard
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
     * Remove an Atlan tag from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TableauDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauDashboard
     * @param qualifiedName of the TableauDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
