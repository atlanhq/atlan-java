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
import com.atlan.model.structs.DbtMetricFilter;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a dbt metric in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DbtMetric extends Asset
        implements IDbtMetric, IDbt, IMetric, ICatalog, IAsset, IReferenceable, IDataQuality {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtMetric";

    /** Fixed typeName for DbtMetrics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAsset> assets;

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
    @Singular
    SortedSet<IColumn> dbtMetricFilterColumns;

    /** TBC */
    @Attribute
    @Singular
    List<DbtMetricFilter> dbtMetricFilters;

    /** TBC */
    @Attribute
    IDbtModel dbtModel;

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
    String dbtUniqueId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> metricDimensionColumns;

    /** TBC */
    @Attribute
    String metricFilters;

    /** TBC */
    @Attribute
    String metricSQL;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> metricTimeGrains;

    /** TBC */
    @Attribute
    IColumn metricTimestampColumn;

    /** TBC */
    @Attribute
    String metricType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all DbtMetric assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtMetric assets will be included.
     *
     * @return an asset filter that includes all DbtMetric assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DbtMetric assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtMetric assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DbtMetric assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DbtMetric assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DbtMetrics will be included
     * @return an asset filter that includes all DbtMetric assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DbtMetric assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtMetrics will be included
     * @return an asset filter that includes all DbtMetric assets
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
     * Reference to a DbtMetric by GUID.
     *
     * @param guid the GUID of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByGuid(String guid) {
        return DbtMetric.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtMetric by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByQualifiedName(String qualifiedName) {
        return DbtMetric.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtMetric by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist or the provided GUID is not a DbtMetric
     */
    public static DbtMetric retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtMetric by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist or the provided GUID is not a DbtMetric
     */
    public static DbtMetric retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtMetric) {
            return (DbtMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtMetric");
        }
    }

    /**
     * Retrieves a DbtMetric by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist
     */
    public static DbtMetric retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtMetric by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist
     */
    public static DbtMetric retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof DbtMetric) {
            return (DbtMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtMetric");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtMetric to active.
     *
     * @param qualifiedName for the DbtMetric
     * @return true if the DbtMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtMetric to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtMetric
     * @return true if the DbtMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the minimal request necessary to update the DbtMetric, as a builder
     */
    public static DbtMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtMetric.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtMetric, from a potentially
     * more-complete DbtMetric object.
     *
     * @return the minimal object necessary to update the DbtMetric, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtMetric are not found in the initial object
     */
    @Override
    public DbtMetricBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtMetric", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtMetric) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtMetric) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtMetric's owners
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtMetric's certificate
     * @param qualifiedName of the DbtMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtMetric) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtMetric's certificate
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtMetric) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtMetric's announcement
     * @param qualifiedName of the DbtMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtMetric) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtMetric.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtMetric's announcement
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtMetric) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtMetric.
     *
     * @param qualifiedName for the DbtMetric
     * @param name human-readable name of the DbtMetric
     * @param terms the list of terms to replace on the DbtMetric, or null to remove all terms from the DbtMetric
     * @return the DbtMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtMetric's assigned terms
     * @param qualifiedName for the DbtMetric
     * @param name human-readable name of the DbtMetric
     * @param terms the list of terms to replace on the DbtMetric, or null to remove all terms from the DbtMetric
     * @return the DbtMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtMetric) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtMetric, without replacing existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to append to the DbtMetric
     * @return the DbtMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtMetric, without replacing existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtMetric
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to append to the DbtMetric
     * @return the DbtMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtMetric) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtMetric, without replacing all existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to remove from the DbtMetric, which must be referenced by GUID
     * @return the DbtMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtMetric, without replacing all existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtMetric
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to remove from the DbtMetric, which must be referenced by GUID
     * @return the DbtMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtMetric) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtMetric, without replacing existing Atlan tags linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtMetric
     */
    public static DbtMetric appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtMetric, without replacing existing Atlan tags linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtMetric
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtMetric
     */
    public static DbtMetric appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtMetric) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtMetric, without replacing existing Atlan tags linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtMetric
     */
    public static DbtMetric appendAtlanTags(
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
     * Add Atlan tags to a DbtMetric, without replacing existing Atlan tags linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtMetric
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtMetric
     */
    public static DbtMetric appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtMetric) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtMetric
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtMetric
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtMetric
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtMetric
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
     * Add Atlan tags to a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtMetric
     * @param qualifiedName of the DbtMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtMetric
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
     * Remove an Atlan tag from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtMetric
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtMetric
     * @param qualifiedName of the DbtMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtMetric
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
