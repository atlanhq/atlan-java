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
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a column within a dbt model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class DbtModelColumn extends Asset implements IDbtModelColumn, IDbt, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtModelColumn";

    /** Fixed typeName for DbtModelColumns. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtAccountName;

    /** TBC */
    @Attribute
    String dbtAlias;

    /** TBC */
    @Attribute
    String dbtConnectionContext;

    /** TBC */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    String dbtEnvironmentName;

    /** TBC */
    @Attribute
    Long dbtJobLastRun;

    /** TBC */
    @Attribute
    String dbtJobName;

    /** TBC */
    @Attribute
    Long dbtJobNextRun;

    /** TBC */
    @Attribute
    String dbtJobNextRunHumanized;

    /** TBC */
    @Attribute
    String dbtJobSchedule;

    /** TBC */
    @Attribute
    String dbtJobScheduleCronHumanized;

    /** TBC */
    @Attribute
    String dbtJobStatus;

    /** TBC */
    @Attribute
    String dbtMeta;

    /** TBC */
    @Attribute
    IDbtModel dbtModel;

    /** TBC */
    @Attribute
    String dbtModelColumnDataType;

    /** TBC */
    @Attribute
    Integer dbtModelColumnOrder;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> dbtModelColumnSqlColumns;

    /** TBC */
    @Attribute
    String dbtModelQualifiedName;

    /** TBC */
    @Attribute
    String dbtPackageName;

    /** TBC */
    @Attribute
    String dbtProjectName;

    /** TBC */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** TBC */
    @Attribute
    String dbtUniqueId;

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
    IColumn sqlColumn;

    /**
     * Reference to a DbtModelColumn by GUID.
     *
     * @param guid the GUID of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByGuid(String guid) {
        return DbtModelColumn.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtModelColumn by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByQualifiedName(String qualifiedName) {
        return DbtModelColumn.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtModelColumn by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    public static DbtModelColumn retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtModelColumn by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    public static DbtModelColumn retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtModelColumn) {
            return (DbtModelColumn) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtModelColumn");
        }
    }

    /**
     * Retrieves a DbtModelColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist
     */
    public static DbtModelColumn retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtModelColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist
     */
    public static DbtModelColumn retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof DbtModelColumn) {
            return (DbtModelColumn) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtModelColumn");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtModelColumn to active.
     *
     * @param qualifiedName for the DbtModelColumn
     * @return true if the DbtModelColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtModelColumn to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtModelColumn
     * @return true if the DbtModelColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the minimal request necessary to update the DbtModelColumn, as a builder
     */
    public static DbtModelColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtModelColumn.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtModelColumn, from a potentially
     * more-complete DbtModelColumn object.
     *
     * @return the minimal object necessary to update the DbtModelColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtModelColumn are not found in the initial object
     */
    @Override
    public DbtModelColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtModelColumn", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModelColumn's owners
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModelColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModelColumn's certificate
     * @param qualifiedName of the DbtModelColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModelColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtModelColumn)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModelColumn's certificate
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModelColumn's announcement
     * @param qualifiedName of the DbtModelColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtModelColumn)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtModelColumn's announcement
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtModelColumn.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param name human-readable name of the DbtModelColumn
     * @param terms the list of terms to replace on the DbtModelColumn, or null to remove all terms from the DbtModelColumn
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtModelColumn's assigned terms
     * @param qualifiedName for the DbtModelColumn
     * @param name human-readable name of the DbtModelColumn
     * @param terms the list of terms to replace on the DbtModelColumn, or null to remove all terms from the DbtModelColumn
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtModelColumn) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtModelColumn, without replacing existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to append to the DbtModelColumn
     * @return the DbtModelColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtModelColumn, without replacing existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtModelColumn
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to append to the DbtModelColumn
     * @return the DbtModelColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModelColumn, without replacing all existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to remove from the DbtModelColumn, which must be referenced by GUID
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModelColumn, without replacing all existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtModelColumn
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to remove from the DbtModelColumn, which must be referenced by GUID
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(
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
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
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
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
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
     * Remove an Atlan tag from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModelColumn
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModelColumn
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
