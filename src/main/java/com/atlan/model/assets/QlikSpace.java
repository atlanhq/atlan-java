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
 * Instance of a Qlik Space in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QlikSpace extends Asset implements IQlikSpace, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikSpace";

    /** Fixed typeName for QlikSpaces. */
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
    String qlikAppId;

    /** TBC */
    @Attribute
    String qlikAppQualifiedName;

    /** Apps contained within the space. */
    @Attribute
    @Singular
    SortedSet<IQlikApp> qlikApps;

    /** Datasets contained within the space. */
    @Attribute
    @Singular
    SortedSet<IQlikDataset> qlikDatasets;

    /** TBC */
    @Attribute
    String qlikId;

    /** TBC */
    @Attribute
    Boolean qlikIsPublished;

    /** TBC */
    @Attribute
    String qlikOwnerId;

    /** TBC */
    @Attribute
    String qlikQRI;

    /** TBC */
    @Attribute
    String qlikSpaceId;

    /** TBC */
    @Attribute
    String qlikSpaceQualifiedName;

    /** Type of space, for example: Private, Shared, etc. */
    @Attribute
    String qlikSpaceType;

    /**
     * Start an asset filter that will return all QlikSpace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSpace assets will be included.
     *
     * @return an asset filter that includes all QlikSpace assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all QlikSpace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSpace assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all QlikSpace assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all QlikSpace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QlikSpaces will be included
     * @return an asset filter that includes all QlikSpace assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all QlikSpace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QlikSpaces will be included
     * @return an asset filter that includes all QlikSpace assets
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
     * Reference to a QlikSpace by GUID.
     *
     * @param guid the GUID of the QlikSpace to reference
     * @return reference to a QlikSpace that can be used for defining a relationship to a QlikSpace
     */
    public static QlikSpace refByGuid(String guid) {
        return QlikSpace._internal().guid(guid).build();
    }

    /**
     * Reference to a QlikSpace by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikSpace to reference
     * @return reference to a QlikSpace that can be used for defining a relationship to a QlikSpace
     */
    public static QlikSpace refByQualifiedName(String qualifiedName) {
        return QlikSpace._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QlikSpace by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the QlikSpace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist or the provided GUID is not a QlikSpace
     */
    @JsonIgnore
    public static QlikSpace get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a QlikSpace by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikSpace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist or the provided GUID is not a QlikSpace
     */
    @JsonIgnore
    public static QlikSpace get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a QlikSpace by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikSpace to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full QlikSpace, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist or the provided GUID is not a QlikSpace
     */
    @JsonIgnore
    public static QlikSpace get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QlikSpace) {
                return (QlikSpace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "QlikSpace");
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof QlikSpace) {
                return (QlikSpace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "QlikSpace");
            }
        }
    }

    /**
     * Retrieves a QlikSpace by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikSpace to retrieve
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist or the provided GUID is not a QlikSpace
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QlikSpace retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QlikSpace by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QlikSpace to retrieve
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist or the provided GUID is not a QlikSpace
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QlikSpace retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a QlikSpace by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikSpace to retrieve
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QlikSpace retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QlikSpace by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QlikSpace to retrieve
     * @return the requested full QlikSpace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSpace does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QlikSpace retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikSpace to active.
     *
     * @param qualifiedName for the QlikSpace
     * @return true if the QlikSpace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikSpace to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikSpace
     * @return true if the QlikSpace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the minimal request necessary to update the QlikSpace, as a builder
     */
    public static QlikSpaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikSpace._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikSpace, from a potentially
     * more-complete QlikSpace object.
     *
     * @return the minimal object necessary to update the QlikSpace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikSpace are not found in the initial object
     */
    @Override
    public QlikSpaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikSpace", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSpace) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSpace) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikSpace's owners
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (QlikSpace) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikSpace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikSpace's certificate
     * @param qualifiedName of the QlikSpace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikSpace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikSpace) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikSpace's certificate
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSpace) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikSpace's announcement
     * @param qualifiedName of the QlikSpace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikSpace)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QlikSpace.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikSpace's announcement
     * @param qualifiedName of the QlikSpace
     * @param name of the QlikSpace
     * @return the updated QlikSpace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSpace) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikSpace.
     *
     * @param qualifiedName for the QlikSpace
     * @param name human-readable name of the QlikSpace
     * @param terms the list of terms to replace on the QlikSpace, or null to remove all terms from the QlikSpace
     * @return the QlikSpace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikSpace's assigned terms
     * @param qualifiedName for the QlikSpace
     * @param name human-readable name of the QlikSpace
     * @param terms the list of terms to replace on the QlikSpace, or null to remove all terms from the QlikSpace
     * @return the QlikSpace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikSpace) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikSpace, without replacing existing terms linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikSpace
     * @param terms the list of terms to append to the QlikSpace
     * @return the QlikSpace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QlikSpace, without replacing existing terms linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikSpace
     * @param qualifiedName for the QlikSpace
     * @param terms the list of terms to append to the QlikSpace
     * @return the QlikSpace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikSpace) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikSpace, without replacing all existing terms linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikSpace
     * @param terms the list of terms to remove from the QlikSpace, which must be referenced by GUID
     * @return the QlikSpace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikSpace, without replacing all existing terms linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikSpace
     * @param qualifiedName for the QlikSpace
     * @param terms the list of terms to remove from the QlikSpace, which must be referenced by GUID
     * @return the QlikSpace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSpace removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikSpace) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikSpace, without replacing existing Atlan tags linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikSpace
     */
    public static QlikSpace appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSpace, without replacing existing Atlan tags linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikSpace
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikSpace
     */
    public static QlikSpace appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikSpace) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSpace, without replacing existing Atlan tags linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikSpace
     */
    public static QlikSpace appendAtlanTags(
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
     * Add Atlan tags to a QlikSpace, without replacing existing Atlan tags linked to the QlikSpace.
     * Note: this operation must make two API calls — one to retrieve the QlikSpace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikSpace
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikSpace
     */
    public static QlikSpace appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikSpace) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSpace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikSpace
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSpace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSpace
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
     * Add Atlan tags to a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikSpace
     * @param qualifiedName of the QlikSpace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSpace
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
     * Remove an Atlan tag from a QlikSpace.
     *
     * @param qualifiedName of the QlikSpace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikSpace
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QlikSpace.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikSpace
     * @param qualifiedName of the QlikSpace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikSpace
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
