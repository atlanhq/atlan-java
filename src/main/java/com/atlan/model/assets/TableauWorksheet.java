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
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Tableau worksheet in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauWorksheet extends Asset
        implements ITableauWorksheet, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorksheet";

    /** Fixed typeName for TableauWorksheets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauCalculatedField> calculatedFields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauDashboard> dashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauDatasourceField> datasourceFields;

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

    /**
     * Start an asset filter that will return all TableauWorksheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauWorksheet assets will be included.
     *
     * @return an asset filter that includes all TableauWorksheet assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all TableauWorksheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauWorksheet assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all TableauWorksheet assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all TableauWorksheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) TableauWorksheets will be included
     * @return an asset filter that includes all TableauWorksheet assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all TableauWorksheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauWorksheets will be included
     * @return an asset filter that includes all TableauWorksheet assets
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
     * Reference to a TableauWorksheet by GUID.
     *
     * @param guid the GUID of the TableauWorksheet to reference
     * @return reference to a TableauWorksheet that can be used for defining a relationship to a TableauWorksheet
     */
    public static TableauWorksheet refByGuid(String guid) {
        return TableauWorksheet._internal().guid(guid).build();
    }

    /**
     * Reference to a TableauWorksheet by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauWorksheet to reference
     * @return reference to a TableauWorksheet that can be used for defining a relationship to a TableauWorksheet
     */
    public static TableauWorksheet refByQualifiedName(String qualifiedName) {
        return TableauWorksheet._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauWorksheet by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the TableauWorksheet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist or the provided GUID is not a TableauWorksheet
     */
    @JsonIgnore
    public static TableauWorksheet get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a TableauWorksheet by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorksheet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist or the provided GUID is not a TableauWorksheet
     */
    @JsonIgnore
    public static TableauWorksheet get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TableauWorksheet) {
                return (TableauWorksheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "TableauWorksheet");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof TableauWorksheet) {
                return (TableauWorksheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "TableauWorksheet");
            }
        }
    }

    /**
     * Retrieves a TableauWorksheet by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauWorksheet to retrieve
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist or the provided GUID is not a TableauWorksheet
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static TableauWorksheet retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a TableauWorksheet by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the TableauWorksheet to retrieve
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist or the provided GUID is not a TableauWorksheet
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static TableauWorksheet retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauWorksheet) {
            return (TableauWorksheet) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauWorksheet");
        }
    }

    /**
     * Retrieves a TableauWorksheet by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauWorksheet to retrieve
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static TableauWorksheet retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a TableauWorksheet by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the TableauWorksheet to retrieve
     * @return the requested full TableauWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheet does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static TableauWorksheet retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof TableauWorksheet) {
            return (TableauWorksheet) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauWorksheet");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauWorksheet to active.
     *
     * @param qualifiedName for the TableauWorksheet
     * @return true if the TableauWorksheet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TableauWorksheet to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauWorksheet
     * @return true if the TableauWorksheet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the minimal request necessary to update the TableauWorksheet, as a builder
     */
    public static TableauWorksheetBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauWorksheet._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauWorksheet, from a potentially
     * more-complete TableauWorksheet object.
     *
     * @return the minimal object necessary to update the TableauWorksheet, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauWorksheet are not found in the initial object
     */
    @Override
    public TableauWorksheetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauWorksheet", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorksheet's owners
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorksheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorksheet's certificate
     * @param qualifiedName of the TableauWorksheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorksheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauWorksheet)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorksheet's certificate
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorksheet's announcement
     * @param qualifiedName of the TableauWorksheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauWorksheet)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauWorksheet's announcement
     * @param qualifiedName of the TableauWorksheet
     * @param name of the TableauWorksheet
     * @return the updated TableauWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauWorksheet.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param name human-readable name of the TableauWorksheet
     * @param terms the list of terms to replace on the TableauWorksheet, or null to remove all terms from the TableauWorksheet
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauWorksheet's assigned terms
     * @param qualifiedName for the TableauWorksheet
     * @param name human-readable name of the TableauWorksheet
     * @param terms the list of terms to replace on the TableauWorksheet, or null to remove all terms from the TableauWorksheet
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauWorksheet) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauWorksheet, without replacing existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to append to the TableauWorksheet
     * @return the TableauWorksheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TableauWorksheet, without replacing existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauWorksheet
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to append to the TableauWorksheet
     * @return the TableauWorksheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorksheet) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorksheet, without replacing all existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to remove from the TableauWorksheet, which must be referenced by GUID
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorksheet, without replacing all existing terms linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauWorksheet
     * @param qualifiedName for the TableauWorksheet
     * @param terms the list of terms to remove from the TableauWorksheet, which must be referenced by GUID
     * @return the TableauWorksheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheet removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorksheet) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauWorksheet, without replacing existing Atlan tags linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheet
     */
    public static TableauWorksheet appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorksheet, without replacing existing Atlan tags linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorksheet
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheet
     */
    public static TableauWorksheet appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauWorksheet) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorksheet, without replacing existing Atlan tags linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheet
     */
    public static TableauWorksheet appendAtlanTags(
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
     * Add Atlan tags to a TableauWorksheet, without replacing existing Atlan tags linked to the TableauWorksheet.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorksheet
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheet
     */
    public static TableauWorksheet appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauWorksheet) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorksheet
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauWorksheet
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorksheet
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorksheet
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
     * Add Atlan tags to a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauWorksheet
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorksheet
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
     * Remove an Atlan tag from a TableauWorksheet.
     *
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauWorksheet
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TableauWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauWorksheet
     * @param qualifiedName of the TableauWorksheet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauWorksheet
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
