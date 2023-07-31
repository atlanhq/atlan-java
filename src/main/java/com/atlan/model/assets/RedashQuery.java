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
 * Instance of a Redash query in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class RedashQuery extends Asset implements IRedashQuery, IRedash, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "RedashQuery";

    /** Fixed typeName for RedashQuerys. */
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
    Boolean redashIsPublished;

    /** Time when the Redash query was last executed. */
    @Attribute
    Long redashQueryLastExecutedAt;

    /** Elapsed time of the last run of the Redash query. */
    @Attribute
    Double redashQueryLastExecutionRuntime;

    /** Parameters for the Redash query. */
    @Attribute
    String redashQueryParameters;

    /** SQL code of the Redash query. */
    @Attribute
    String redashQuerySQL;

    /** Schedule of the Redash query. */
    @Attribute
    @Singular("putRedashQuerySchedule")
    Map<String, String> redashQuerySchedule;

    /** Human-readable schedule of the Redash query. */
    @Attribute
    String redashQueryScheduleHumanized;

    /** Visualizations of this Redash query. */
    @Attribute
    @Singular
    SortedSet<IRedashVisualization> redashVisualizations;

    /**
     * Start an asset filter that will return all RedashQuery assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) RedashQuery assets will be included.
     *
     * @return an asset filter that includes all RedashQuery assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all RedashQuery assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) RedashQuery assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all RedashQuery assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all RedashQuery assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) RedashQuerys will be included
     * @return an asset filter that includes all RedashQuery assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all RedashQuery assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) RedashQuerys will be included
     * @return an asset filter that includes all RedashQuery assets
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
     * Reference to a RedashQuery by GUID.
     *
     * @param guid the GUID of the RedashQuery to reference
     * @return reference to a RedashQuery that can be used for defining a relationship to a RedashQuery
     */
    public static RedashQuery refByGuid(String guid) {
        return RedashQuery._internal().guid(guid).build();
    }

    /**
     * Reference to a RedashQuery by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the RedashQuery to reference
     * @return reference to a RedashQuery that can be used for defining a relationship to a RedashQuery
     */
    public static RedashQuery refByQualifiedName(String qualifiedName) {
        return RedashQuery._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a RedashQuery by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the RedashQuery to retrieve, either its GUID or its full qualifiedName
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist or the provided GUID is not a RedashQuery
     */
    @JsonIgnore
    public static RedashQuery get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a RedashQuery by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the RedashQuery to retrieve, either its GUID or its full qualifiedName
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist or the provided GUID is not a RedashQuery
     */
    @JsonIgnore
    public static RedashQuery get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof RedashQuery) {
                return (RedashQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "RedashQuery");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof RedashQuery) {
                return (RedashQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "RedashQuery");
            }
        }
    }

    /**
     * Retrieves a RedashQuery by its GUID, complete with all of its relationships.
     *
     * @param guid of the RedashQuery to retrieve
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist or the provided GUID is not a RedashQuery
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static RedashQuery retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a RedashQuery by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the RedashQuery to retrieve
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist or the provided GUID is not a RedashQuery
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static RedashQuery retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof RedashQuery) {
            return (RedashQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "RedashQuery");
        }
    }

    /**
     * Retrieves a RedashQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the RedashQuery to retrieve
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static RedashQuery retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a RedashQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the RedashQuery to retrieve
     * @return the requested full RedashQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashQuery does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static RedashQuery retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof RedashQuery) {
            return (RedashQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "RedashQuery");
        }
    }

    /**
     * Restore the archived (soft-deleted) RedashQuery to active.
     *
     * @param qualifiedName for the RedashQuery
     * @return true if the RedashQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) RedashQuery to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the RedashQuery
     * @return true if the RedashQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the minimal request necessary to update the RedashQuery, as a builder
     */
    public static RedashQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return RedashQuery._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a RedashQuery, from a potentially
     * more-complete RedashQuery object.
     *
     * @return the minimal object necessary to update the RedashQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for RedashQuery are not found in the initial object
     */
    @Override
    public RedashQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "RedashQuery", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashQuery) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashQuery) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the RedashQuery's owners
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashQuery) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the RedashQuery's certificate
     * @param qualifiedName of the RedashQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (RedashQuery)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the RedashQuery's certificate
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashQuery) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the RedashQuery's announcement
     * @param qualifiedName of the RedashQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (RedashQuery)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a RedashQuery.
     *
     * @param client connectivity to the Atlan client from which to remove the RedashQuery's announcement
     * @param qualifiedName of the RedashQuery
     * @param name of the RedashQuery
     * @return the updated RedashQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashQuery) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the RedashQuery.
     *
     * @param qualifiedName for the RedashQuery
     * @param name human-readable name of the RedashQuery
     * @param terms the list of terms to replace on the RedashQuery, or null to remove all terms from the RedashQuery
     * @return the RedashQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to replace the RedashQuery's assigned terms
     * @param qualifiedName for the RedashQuery
     * @param name human-readable name of the RedashQuery
     * @param terms the list of terms to replace on the RedashQuery, or null to remove all terms from the RedashQuery
     * @return the RedashQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (RedashQuery) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the RedashQuery, without replacing existing terms linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the RedashQuery
     * @param terms the list of terms to append to the RedashQuery
     * @return the RedashQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the RedashQuery, without replacing existing terms linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the RedashQuery
     * @param qualifiedName for the RedashQuery
     * @param terms the list of terms to append to the RedashQuery
     * @return the RedashQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (RedashQuery) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashQuery, without replacing all existing terms linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the RedashQuery
     * @param terms the list of terms to remove from the RedashQuery, which must be referenced by GUID
     * @return the RedashQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashQuery, without replacing all existing terms linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the RedashQuery
     * @param qualifiedName for the RedashQuery
     * @param terms the list of terms to remove from the RedashQuery, which must be referenced by GUID
     * @return the RedashQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashQuery removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (RedashQuery) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a RedashQuery, without replacing existing Atlan tags linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated RedashQuery
     */
    public static RedashQuery appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashQuery, without replacing existing Atlan tags linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the RedashQuery
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated RedashQuery
     */
    public static RedashQuery appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (RedashQuery) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashQuery, without replacing existing Atlan tags linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated RedashQuery
     */
    public static RedashQuery appendAtlanTags(
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
     * Add Atlan tags to a RedashQuery, without replacing existing Atlan tags linked to the RedashQuery.
     * Note: this operation must make two API calls — one to retrieve the RedashQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the RedashQuery
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated RedashQuery
     */
    public static RedashQuery appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (RedashQuery) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashQuery
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the RedashQuery
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashQuery
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashQuery
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
     * Add Atlan tags to a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the RedashQuery
     * @param qualifiedName of the RedashQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashQuery
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
     * Remove an Atlan tag from a RedashQuery.
     *
     * @param qualifiedName of the RedashQuery
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the RedashQuery
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a RedashQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a RedashQuery
     * @param qualifiedName of the RedashQuery
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the RedashQuery
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
