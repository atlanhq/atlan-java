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
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Mode chart in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModeChart extends Asset implements IModeChart, IMode, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeChart";

    /** Fixed typeName for ModeCharts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    String modeChartType;

    /** TBC */
    @Attribute
    String modeId;

    /** TBC */
    @Attribute
    IModeQuery modeQuery;

    /** TBC */
    @Attribute
    String modeQueryName;

    /** TBC */
    @Attribute
    String modeQueryQualifiedName;

    /** TBC */
    @Attribute
    String modeReportName;

    /** TBC */
    @Attribute
    String modeReportQualifiedName;

    /** TBC */
    @Attribute
    String modeToken;

    /** TBC */
    @Attribute
    String modeWorkspaceName;

    /** TBC */
    @Attribute
    String modeWorkspaceQualifiedName;

    /** TBC */
    @Attribute
    String modeWorkspaceUsername;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all ModeChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeChart assets will be included.
     *
     * @return an asset filter that includes all ModeChart assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ModeChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeChart assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ModeChart assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ModeChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ModeCharts will be included
     * @return an asset filter that includes all ModeChart assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ModeChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModeCharts will be included
     * @return an asset filter that includes all ModeChart assets
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
     * Reference to a ModeChart by GUID.
     *
     * @param guid the GUID of the ModeChart to reference
     * @return reference to a ModeChart that can be used for defining a relationship to a ModeChart
     */
    public static ModeChart refByGuid(String guid) {
        return ModeChart._internal().guid(guid).build();
    }

    /**
     * Reference to a ModeChart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeChart to reference
     * @return reference to a ModeChart that can be used for defining a relationship to a ModeChart
     */
    public static ModeChart refByQualifiedName(String qualifiedName) {
        return ModeChart._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ModeChart by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ModeChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     */
    @JsonIgnore
    public static ModeChart get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ModeChart by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     */
    @JsonIgnore
    public static ModeChart get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ModeChart by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeChart to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModeChart, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     */
    @JsonIgnore
    public static ModeChart get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModeChart) {
                return (ModeChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "ModeChart");
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ModeChart) {
                return (ModeChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "ModeChart");
            }
        }
    }

    /**
     * Retrieves a ModeChart by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeChart retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ModeChart by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeChart retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ModeChart by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeChart retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ModeChart by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeChart retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModeChart to active.
     *
     * @param qualifiedName for the ModeChart
     * @return true if the ModeChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModeChart to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModeChart
     * @return true if the ModeChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the minimal request necessary to update the ModeChart, as a builder
     */
    public static ModeChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeChart._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeChart, from a potentially
     * more-complete ModeChart object.
     *
     * @return the minimal object necessary to update the ModeChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeChart are not found in the initial object
     */
    @Override
    public ModeChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeChart", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeChart) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeChart) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ModeChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeChart's owners
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeChart's certificate
     * @param qualifiedName of the ModeChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeChart) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ModeChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeChart's certificate
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeChart) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeChart's announcement
     * @param qualifiedName of the ModeChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModeChart)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ModeChart.
     *
     * @param client connectivity to the Atlan client from which to remove the ModeChart's announcement
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeChart) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModeChart.
     *
     * @param qualifiedName for the ModeChart
     * @param name human-readable name of the ModeChart
     * @param terms the list of terms to replace on the ModeChart, or null to remove all terms from the ModeChart
     * @return the ModeChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModeChart's assigned terms
     * @param qualifiedName for the ModeChart
     * @param name human-readable name of the ModeChart
     * @param terms the list of terms to replace on the ModeChart, or null to remove all terms from the ModeChart
     * @return the ModeChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModeChart) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeChart, without replacing existing terms linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to append to the ModeChart
     * @return the ModeChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ModeChart, without replacing existing terms linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModeChart
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to append to the ModeChart
     * @return the ModeChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeChart) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeChart, without replacing all existing terms linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to remove from the ModeChart, which must be referenced by GUID
     * @return the ModeChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeChart, without replacing all existing terms linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModeChart
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to remove from the ModeChart, which must be referenced by GUID
     * @return the ModeChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeChart) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModeChart, without replacing existing Atlan tags linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeChart
     */
    public static ModeChart appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeChart, without replacing existing Atlan tags linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeChart
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeChart
     */
    public static ModeChart appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModeChart) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeChart, without replacing existing Atlan tags linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeChart
     */
    public static ModeChart appendAtlanTags(
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
     * Add Atlan tags to a ModeChart, without replacing existing Atlan tags linked to the ModeChart.
     * Note: this operation must make two API calls — one to retrieve the ModeChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeChart
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeChart
     */
    public static ModeChart appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModeChart) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeChart
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeChart
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeChart
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeChart
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
     * Add Atlan tags to a ModeChart.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeChart
     * @param qualifiedName of the ModeChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeChart
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
     * Remove an Atlan tag from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeChart
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ModeChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModeChart
     * @param qualifiedName of the ModeChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeChart
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
