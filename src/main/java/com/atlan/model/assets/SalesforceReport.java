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
 * Instance of a Salesforce report in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class SalesforceReport extends Asset
        implements ISalesforceReport, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceReport";

    /** Fixed typeName for SalesforceReports. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String apiName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceDashboard> dashboards;

    /** List of column names on the report. */
    @Attribute
    @Singular
    SortedSet<String> detailColumns;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    ISalesforceOrganization organization;

    /** TBC */
    @Attribute
    String organizationQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Type of report in Salesforce. */
    @Attribute
    @Singular("putReportType")
    Map<String, String> reportType;

    /** ID of the report in Salesforce. */
    @Attribute
    String sourceId;

    /**
     * Reference to a SalesforceReport by GUID.
     *
     * @param guid the GUID of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByGuid(String guid) {
        return SalesforceReport.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByQualifiedName(String qualifiedName) {
        return SalesforceReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SalesforceReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    public static SalesforceReport retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a SalesforceReport by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    public static SalesforceReport retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SalesforceReport) {
            return (SalesforceReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SalesforceReport");
        }
    }

    /**
     * Retrieves a SalesforceReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist
     */
    public static SalesforceReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a SalesforceReport by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist
     */
    public static SalesforceReport retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof SalesforceReport) {
            return (SalesforceReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SalesforceReport");
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceReport to active.
     *
     * @param qualifiedName for the SalesforceReport
     * @return true if the SalesforceReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SalesforceReport to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceReport
     * @return true if the SalesforceReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the minimal request necessary to update the SalesforceReport, as a builder
     */
    public static SalesforceReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceReport, from a potentially
     * more-complete SalesforceReport object.
     *
     * @return the minimal object necessary to update the SalesforceReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceReport are not found in the initial object
     */
    @Override
    public SalesforceReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SalesforceReport", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceReport's owners
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceReport's certificate
     * @param qualifiedName of the SalesforceReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceReport)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceReport's certificate
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceReport's announcement
     * @param qualifiedName of the SalesforceReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceReport)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SalesforceReport.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceReport's announcement
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceReport.
     *
     * @param qualifiedName for the SalesforceReport
     * @param name human-readable name of the SalesforceReport
     * @param terms the list of terms to replace on the SalesforceReport, or null to remove all terms from the SalesforceReport
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceReport's assigned terms
     * @param qualifiedName for the SalesforceReport
     * @param name human-readable name of the SalesforceReport
     * @param terms the list of terms to replace on the SalesforceReport, or null to remove all terms from the SalesforceReport
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceReport) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceReport, without replacing existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to append to the SalesforceReport
     * @return the SalesforceReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SalesforceReport, without replacing existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceReport
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to append to the SalesforceReport
     * @return the SalesforceReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceReport) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceReport, without replacing all existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to remove from the SalesforceReport, which must be referenced by GUID
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceReport, without replacing all existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceReport
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to remove from the SalesforceReport, which must be referenced by GUID
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceReport) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     */
    public static SalesforceReport appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     */
    public static SalesforceReport appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SalesforceReport) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     */
    public static SalesforceReport appendAtlanTags(
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
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     */
    public static SalesforceReport appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceReport) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceReport
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceReport
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceReport
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
     * Add Atlan tags to a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceReport
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
     * Remove an Atlan tag from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceReport
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceReport
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
