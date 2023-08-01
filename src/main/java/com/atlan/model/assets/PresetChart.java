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
import com.atlan.model.enums.AtlanConnectorType;
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
 * Instance of a Preset chart in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class PresetChart extends Asset implements IPresetChart, IPreset, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetChart";

    /** Fixed typeName for PresetCharts. */
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

    /** Markdown-based description of the chart. */
    @Attribute
    String presetChartDescriptionMarkdown;

    /** TBC */
    @Attribute
    @Singular("putPresetChartFormData")
    Map<String, String> presetChartFormData;

    /** Collection in which the chart exists. */
    @Attribute
    IPresetDashboard presetDashboard;

    /** TBC */
    @Attribute
    Long presetDashboardId;

    /** TBC */
    @Attribute
    String presetDashboardQualifiedName;

    /** TBC */
    @Attribute
    Long presetWorkspaceId;

    /** TBC */
    @Attribute
    String presetWorkspaceQualifiedName;

    /**
     * Start an asset filter that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) PresetChart assets will be included.
     *
     * @return an asset filter that includes all PresetChart assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) PresetChart assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all PresetChart assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) PresetCharts will be included
     * @return an asset filter that includes all PresetChart assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) PresetCharts will be included
     * @return an asset filter that includes all PresetChart assets
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
     * Reference to a PresetChart by GUID.
     *
     * @param guid the GUID of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByGuid(String guid) {
        return PresetChart._internal().guid(guid).build();
    }

    /**
     * Reference to a PresetChart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByQualifiedName(String qualifiedName) {
        return PresetChart._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full PresetChart, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof PresetChart) {
                return (PresetChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "PresetChart");
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof PresetChart) {
                return (PresetChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "PresetChart");
            }
        }
    }

    /**
     * Retrieves a PresetChart by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static PresetChart retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a PresetChart by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static PresetChart retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a PresetChart by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static PresetChart retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a PresetChart by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static PresetChart retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) PresetChart to active.
     *
     * @param qualifiedName for the PresetChart
     * @return true if the PresetChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) PresetChart to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the PresetChart
     * @return true if the PresetChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collectionQualifiedName unique name of the collection in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String[] tokens = collectionQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetChart._internal()
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetDashboardQualifiedName(collectionQualifiedName)
                .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the minimal request necessary to update the PresetChart, as a builder
     */
    public static PresetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetChart._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetChart, from a potentially
     * more-complete PresetChart object.
     *
     * @return the minimal object necessary to update the PresetChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetChart are not found in the initial object
     */
    @Override
    public PresetChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetChart", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetChart's owners
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetChart's certificate
     * @param qualifiedName of the PresetChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PresetChart)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetChart's certificate
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetChart's announcement
     * @param qualifiedName of the PresetChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (PresetChart)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a PresetChart.
     *
     * @param client connectivity to the Atlan client from which to remove the PresetChart's announcement
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PresetChart.
     *
     * @param qualifiedName for the PresetChart
     * @param name human-readable name of the PresetChart
     * @param terms the list of terms to replace on the PresetChart, or null to remove all terms from the PresetChart
     * @return the PresetChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to replace the PresetChart's assigned terms
     * @param qualifiedName for the PresetChart
     * @param name human-readable name of the PresetChart
     * @param terms the list of terms to replace on the PresetChart, or null to remove all terms from the PresetChart
     * @return the PresetChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetChart, without replacing existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to append to the PresetChart
     * @return the PresetChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the PresetChart, without replacing existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the PresetChart
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to append to the PresetChart
     * @return the PresetChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetChart, without replacing all existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to remove from the PresetChart, which must be referenced by GUID
     * @return the PresetChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetChart, without replacing all existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the PresetChart
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to remove from the PresetChart, which must be referenced by GUID
     * @return the PresetChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     */
    public static PresetChart appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     */
    public static PresetChart appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PresetChart) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     */
    public static PresetChart appendAtlanTags(
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
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     */
    public static PresetChart appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetChart) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetChart
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetChart
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetChart
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
     * Add Atlan tags to a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PresetChart
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
     * Remove an Atlan tag from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetChart
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetChart
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
