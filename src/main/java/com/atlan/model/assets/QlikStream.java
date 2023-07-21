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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Atlan Qlik Stream Asset. This is analogus to Space.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QlikStream extends Asset implements IQlikStream, IQlikSpace, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikStream";

    /** Fixed typeName for QlikStreams. */
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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQlikApp> qlikApps;

    /** TBC */
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

    /** TBC */
    @Attribute
    String qlikSpaceType;

    /**
     * Start an asset filter that will return all QlikStream assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikStream assets will be included.
     *
     * @return an asset filter that includes all QlikStream assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all QlikStream assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QlikStreams will be included
     * @return an asset filter that includes all QlikStream assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a QlikStream by GUID.
     *
     * @param guid the GUID of the QlikStream to reference
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByGuid(String guid) {
        return QlikStream.builder().guid(guid).build();
    }

    /**
     * Reference to a QlikStream by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikStream to reference
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByQualifiedName(String qualifiedName) {
        return QlikStream.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QlikStream by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikStream to retrieve
     * @return the requested full QlikStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    public static QlikStream retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QlikStream by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QlikStream to retrieve
     * @return the requested full QlikStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    public static QlikStream retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QlikStream) {
            return (QlikStream) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QlikStream");
        }
    }

    /**
     * Retrieves a QlikStream by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikStream to retrieve
     * @return the requested full QlikStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist
     */
    public static QlikStream retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QlikStream by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QlikStream to retrieve
     * @return the requested full QlikStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist
     */
    public static QlikStream retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QlikStream) {
            return (QlikStream) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QlikStream");
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikStream to active.
     *
     * @param qualifiedName for the QlikStream
     * @return true if the QlikStream is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikStream to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikStream
     * @return true if the QlikStream is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the minimal request necessary to update the QlikStream, as a builder
     */
    public static QlikStreamBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikStream.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikStream, from a potentially
     * more-complete QlikStream object.
     *
     * @return the minimal object necessary to update the QlikStream, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikStream are not found in the initial object
     */
    @Override
    public QlikStreamBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikStream", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikStream's owners
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (QlikStream) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikStream, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikStream's certificate
     * @param qualifiedName of the QlikStream
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikStream, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikStream) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikStream's certificate
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikStream's announcement
     * @param qualifiedName of the QlikStream
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikStream) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QlikStream.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikStream's announcement
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikStream.
     *
     * @param qualifiedName for the QlikStream
     * @param name human-readable name of the QlikStream
     * @param terms the list of terms to replace on the QlikStream, or null to remove all terms from the QlikStream
     * @return the QlikStream that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikStream's assigned terms
     * @param qualifiedName for the QlikStream
     * @param name human-readable name of the QlikStream
     * @param terms the list of terms to replace on the QlikStream, or null to remove all terms from the QlikStream
     * @return the QlikStream that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikStream) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikStream, without replacing existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to append to the QlikStream
     * @return the QlikStream that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QlikStream, without replacing existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikStream
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to append to the QlikStream
     * @return the QlikStream that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikStream) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikStream, without replacing all existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to remove from the QlikStream, which must be referenced by GUID
     * @return the QlikStream that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikStream, without replacing all existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikStream
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to remove from the QlikStream, which must be referenced by GUID
     * @return the QlikStream that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikStream) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     */
    public static QlikStream appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     */
    public static QlikStream appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikStream) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     */
    public static QlikStream appendAtlanTags(
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
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     */
    public static QlikStream appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikStream) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikStream
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikStream
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikStream
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
     * Add Atlan tags to a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikStream
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
     * Remove an Atlan tag from a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikStream
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikStream
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
