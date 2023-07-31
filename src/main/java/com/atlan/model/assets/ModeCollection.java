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
 * Instance of a Mode collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModeCollection extends Asset implements IModeCollection, IMode, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeCollection";

    /** Fixed typeName for ModeCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    String modeCollectionState;

    /** TBC */
    @Attribute
    String modeCollectionType;

    /** TBC */
    @Attribute
    String modeId;

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
    @Singular
    SortedSet<IModeReport> modeReports;

    /** TBC */
    @Attribute
    String modeToken;

    /** TBC */
    @Attribute
    IModeWorkspace modeWorkspace;

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
     * Start an asset filter that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeCollection assets will be included.
     *
     * @return an asset filter that includes all ModeCollection assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ModeCollection assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ModeCollections will be included
     * @return an asset filter that includes all ModeCollection assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModeCollections will be included
     * @return an asset filter that includes all ModeCollection assets
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
     * Reference to a ModeCollection by GUID.
     *
     * @param guid the GUID of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByGuid(String guid) {
        return ModeCollection._internal().guid(guid).build();
    }

    /**
     * Reference to a ModeCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByQualifiedName(String qualifiedName) {
        return ModeCollection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModeCollection) {
                return (ModeCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "ModeCollection");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof ModeCollection) {
                return (ModeCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "ModeCollection");
            }
        }
    }

    /**
     * Retrieves a ModeCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeCollection retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ModeCollection by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeCollection retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ModeCollection) {
            return (ModeCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ModeCollection");
        }
    }

    /**
     * Retrieves a ModeCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ModeCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeCollection retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof ModeCollection) {
            return (ModeCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ModeCollection");
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeCollection to active.
     *
     * @param qualifiedName for the ModeCollection
     * @return true if the ModeCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModeCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModeCollection
     * @return true if the ModeCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the minimal request necessary to update the ModeCollection, as a builder
     */
    public static ModeCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeCollection._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeCollection, from a potentially
     * more-complete ModeCollection object.
     *
     * @return the minimal object necessary to update the ModeCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeCollection are not found in the initial object
     */
    @Override
    public ModeCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeCollection's owners
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeCollection's certificate
     * @param qualifiedName of the ModeCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeCollection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeCollection's certificate
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeCollection's announcement
     * @param qualifiedName of the ModeCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModeCollection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ModeCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the ModeCollection's announcement
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModeCollection.
     *
     * @param qualifiedName for the ModeCollection
     * @param name human-readable name of the ModeCollection
     * @param terms the list of terms to replace on the ModeCollection, or null to remove all terms from the ModeCollection
     * @return the ModeCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModeCollection's assigned terms
     * @param qualifiedName for the ModeCollection
     * @param name human-readable name of the ModeCollection
     * @param terms the list of terms to replace on the ModeCollection, or null to remove all terms from the ModeCollection
     * @return the ModeCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModeCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeCollection, without replacing existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to append to the ModeCollection
     * @return the ModeCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ModeCollection, without replacing existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModeCollection
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to append to the ModeCollection
     * @return the ModeCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeCollection, without replacing all existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to remove from the ModeCollection, which must be referenced by GUID
     * @return the ModeCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeCollection, without replacing all existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModeCollection
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to remove from the ModeCollection, which must be referenced by GUID
     * @return the ModeCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     */
    public static ModeCollection appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     */
    public static ModeCollection appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModeCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     */
    public static ModeCollection appendAtlanTags(
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
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     */
    public static ModeCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModeCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeCollection
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
     * Add Atlan tags to a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeCollection
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
     * Remove an Atlan tag from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeCollection
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeCollection
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
