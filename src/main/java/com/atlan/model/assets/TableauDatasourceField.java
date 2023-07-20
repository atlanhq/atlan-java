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
 * Instance of a Tableau datasource field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauDatasourceField extends Asset
        implements ITableauField, ITableauDatasourceField, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauDatasourceField";

    /** Fixed typeName for TableauDatasourceFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    ITableauDatasource datasource;

    /** TBC */
    @Attribute
    String datasourceFieldType;

    /** TBC */
    @Attribute
    String datasourceQualifiedName;

    /** TBC */
    @Attribute
    String fullyQualifiedName;

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
    String tableauDatasourceFieldBinSize;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldDataCategory;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldDataType;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldFormula;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldRole;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamColumns;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamFields;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamTables;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauWorksheet> worksheets;

    /**
     * Reference to a TableauDatasourceField by GUID.
     *
     * @param guid the GUID of the TableauDatasourceField to reference
     * @return reference to a TableauDatasourceField that can be used for defining a relationship to a TableauDatasourceField
     */
    public static TableauDatasourceField refByGuid(String guid) {
        return TableauDatasourceField.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauDatasourceField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauDatasourceField to reference
     * @return reference to a TableauDatasourceField that can be used for defining a relationship to a TableauDatasourceField
     */
    public static TableauDatasourceField refByQualifiedName(String qualifiedName) {
        return TableauDatasourceField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauDatasourceField by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist or the provided GUID is not a TableauDatasourceField
     */
    public static TableauDatasourceField retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a TableauDatasourceField by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist or the provided GUID is not a TableauDatasourceField
     */
    public static TableauDatasourceField retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauDatasourceField) {
            return (TableauDatasourceField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauDatasourceField");
        }
    }

    /**
     * Retrieves a TableauDatasourceField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist
     */
    public static TableauDatasourceField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a TableauDatasourceField by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist
     */
    public static TableauDatasourceField retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof TableauDatasourceField) {
            return (TableauDatasourceField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauDatasourceField");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauDatasourceField to active.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @return true if the TableauDatasourceField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TableauDatasourceField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauDatasourceField
     * @return true if the TableauDatasourceField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the minimal request necessary to update the TableauDatasourceField, as a builder
     */
    public static TableauDatasourceFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauDatasourceField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauDatasourceField, from a potentially
     * more-complete TableauDatasourceField object.
     *
     * @return the minimal object necessary to update the TableauDatasourceField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauDatasourceField are not found in the initial object
     */
    @Override
    public TableauDatasourceFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauDatasourceField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauDatasourceField's owners
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDatasourceField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauDatasourceField's certificate
     * @param qualifiedName of the TableauDatasourceField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDatasourceField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauDatasourceField)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauDatasourceField's certificate
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauDatasourceField's announcement
     * @param qualifiedName of the TableauDatasourceField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauDatasourceField)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauDatasourceField's announcement
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauDatasourceField.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param name human-readable name of the TableauDatasourceField
     * @param terms the list of terms to replace on the TableauDatasourceField, or null to remove all terms from the TableauDatasourceField
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauDatasourceField's assigned terms
     * @param qualifiedName for the TableauDatasourceField
     * @param name human-readable name of the TableauDatasourceField
     * @param terms the list of terms to replace on the TableauDatasourceField, or null to remove all terms from the TableauDatasourceField
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauDatasourceField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauDatasourceField, without replacing existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to append to the TableauDatasourceField
     * @return the TableauDatasourceField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TableauDatasourceField, without replacing existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauDatasourceField
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to append to the TableauDatasourceField
     * @return the TableauDatasourceField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauDatasourceField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDatasourceField, without replacing all existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to remove from the TableauDatasourceField, which must be referenced by GUID
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDatasourceField, without replacing all existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauDatasourceField
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to remove from the TableauDatasourceField, which must be referenced by GUID
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauDatasourceField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField, without replacing existing Atlan tags linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauDatasourceField
     */
    public static TableauDatasourceField appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField, without replacing existing Atlan tags linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauDatasourceField
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauDatasourceField
     */
    public static TableauDatasourceField appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (TableauDatasourceField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField, without replacing existing Atlan tags linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauDatasourceField
     */
    public static TableauDatasourceField appendAtlanTags(
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
     * Add Atlan tags to a TableauDatasourceField, without replacing existing Atlan tags linked to the TableauDatasourceField.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasourceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauDatasourceField
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauDatasourceField
     */
    public static TableauDatasourceField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauDatasourceField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDatasourceField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauDatasourceField
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDatasourceField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDatasourceField
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
     * Add Atlan tags to a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TableauDatasourceField
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauDatasourceField
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
     * Remove an Atlan tag from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauDatasourceField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TableauDatasourceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauDatasourceField
     * @param qualifiedName of the TableauDatasourceField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauDatasourceField
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
