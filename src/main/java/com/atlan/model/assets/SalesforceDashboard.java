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
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Salesforce dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SalesforceDashboard extends Asset
        implements ISalesforceDashboard, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceDashboard";

    /** Fixed typeName for SalesforceDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String apiName;

    /** Type of dashboard in Salesforce. */
    @Attribute
    String dashboardType;

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

    /** Number of reports linked to the dashboard in Salesforce. */
    @Attribute
    Long reportCount;

    /** Reports linked to the dashboard in Salesforce. */
    @Attribute
    @Singular
    SortedSet<ISalesforceReport> reports;

    /** ID of the dashboard in Salesforce. */
    @Attribute
    String sourceId;

    /**
     * Start an asset filter that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceDashboard assets will be included.
     *
     * @return an asset filter that includes all SalesforceDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all SalesforceDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SalesforceDashboards will be included
     * @return an asset filter that includes all SalesforceDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SalesforceDashboards will be included
     * @return an asset filter that includes all SalesforceDashboard assets
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
     * Reference to a SalesforceDashboard by GUID.
     *
     * @param guid the GUID of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByGuid(String guid) {
        return SalesforceDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByQualifiedName(String qualifiedName) {
        return SalesforceDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (id.startsWith("default")) {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "SalesforceDashboard");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "SalesforceDashboard");
            }
        }
    }

    /**
     * Retrieves a SalesforceDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SalesforceDashboard retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a SalesforceDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SalesforceDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SalesforceDashboard) {
            return (SalesforceDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SalesforceDashboard");
        }
    }

    /**
     * Retrieves a SalesforceDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SalesforceDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a SalesforceDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SalesforceDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof SalesforceDashboard) {
            return (SalesforceDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SalesforceDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceDashboard to active.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @return true if the SalesforceDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SalesforceDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceDashboard
     * @return true if the SalesforceDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the minimal request necessary to update the SalesforceDashboard, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceDashboard, from a potentially
     * more-complete SalesforceDashboard object.
     *
     * @return the minimal object necessary to update the SalesforceDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceDashboard are not found in the initial object
     */
    @Override
    public SalesforceDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SalesforceDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceDashboard's owners
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceDashboard's certificate
     * @param qualifiedName of the SalesforceDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceDashboard)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceDashboard's certificate
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceDashboard's announcement
     * @param qualifiedName of the SalesforceDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceDashboard)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceDashboard's announcement
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceDashboard.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param name human-readable name of the SalesforceDashboard
     * @param terms the list of terms to replace on the SalesforceDashboard, or null to remove all terms from the SalesforceDashboard
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceDashboard's assigned terms
     * @param qualifiedName for the SalesforceDashboard
     * @param name human-readable name of the SalesforceDashboard
     * @param terms the list of terms to replace on the SalesforceDashboard, or null to remove all terms from the SalesforceDashboard
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceDashboard, without replacing existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to append to the SalesforceDashboard
     * @return the SalesforceDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SalesforceDashboard, without replacing existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceDashboard
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to append to the SalesforceDashboard
     * @return the SalesforceDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceDashboard, without replacing all existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to remove from the SalesforceDashboard, which must be referenced by GUID
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceDashboard, without replacing all existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceDashboard
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to remove from the SalesforceDashboard, which must be referenced by GUID
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     */
    public static SalesforceDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     */
    public static SalesforceDashboard appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SalesforceDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     */
    public static SalesforceDashboard appendAtlanTags(
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
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     */
    public static SalesforceDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceDashboard
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
     * Add Atlan tags to a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceDashboard
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
     * Remove an Atlan tag from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
