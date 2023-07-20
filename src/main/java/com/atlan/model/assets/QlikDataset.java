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
 * Instance of a Qlik Dataset, Datafile, Datastore or Dataasset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QlikDataset extends Asset implements IQlikDataset, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikDataset";

    /** Fixed typeName for QlikDatasets. */
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

    /** Subtype of the dataset. */
    @Attribute
    String qlikDatasetSubtype;

    /** Technical name of the data asset. */
    @Attribute
    String qlikDatasetTechnicalName;

    /** Type of the data asset, for example: qix-df, snowflake, etc. */
    @Attribute
    String qlikDatasetType;

    /** URI of the dataset. */
    @Attribute
    String qlikDatasetUri;

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

    /** Space in which the dataset exists. */
    @Attribute
    IQlikSpace qlikSpace;

    /** TBC */
    @Attribute
    String qlikSpaceId;

    /** TBC */
    @Attribute
    String qlikSpaceQualifiedName;

    /**
     * Reference to a QlikDataset by GUID.
     *
     * @param guid the GUID of the QlikDataset to reference
     * @return reference to a QlikDataset that can be used for defining a relationship to a QlikDataset
     */
    public static QlikDataset refByGuid(String guid) {
        return QlikDataset.builder().guid(guid).build();
    }

    /**
     * Reference to a QlikDataset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikDataset to reference
     * @return reference to a QlikDataset that can be used for defining a relationship to a QlikDataset
     */
    public static QlikDataset refByQualifiedName(String qualifiedName) {
        return QlikDataset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QlikDataset by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist or the provided GUID is not a QlikDataset
     */
    public static QlikDataset retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QlikDataset by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist or the provided GUID is not a QlikDataset
     */
    public static QlikDataset retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QlikDataset) {
            return (QlikDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QlikDataset");
        }
    }

    /**
     * Retrieves a QlikDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist
     */
    public static QlikDataset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QlikDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QlikDataset to retrieve
     * @return the requested full QlikDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikDataset does not exist
     */
    public static QlikDataset retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QlikDataset) {
            return (QlikDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QlikDataset");
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikDataset to active.
     *
     * @param qualifiedName for the QlikDataset
     * @return true if the QlikDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikDataset
     * @return true if the QlikDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the minimal request necessary to update the QlikDataset, as a builder
     */
    public static QlikDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikDataset, from a potentially
     * more-complete QlikDataset object.
     *
     * @return the minimal object necessary to update the QlikDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikDataset are not found in the initial object
     */
    @Override
    public QlikDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikDataset's owners
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikDataset's certificate
     * @param qualifiedName of the QlikDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikDataset) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikDataset's certificate
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikDataset's announcement
     * @param qualifiedName of the QlikDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikDataset)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QlikDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikDataset's announcement
     * @param qualifiedName of the QlikDataset
     * @param name of the QlikDataset
     * @return the updated QlikDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikDataset.
     *
     * @param qualifiedName for the QlikDataset
     * @param name human-readable name of the QlikDataset
     * @param terms the list of terms to replace on the QlikDataset, or null to remove all terms from the QlikDataset
     * @return the QlikDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikDataset's assigned terms
     * @param qualifiedName for the QlikDataset
     * @param name human-readable name of the QlikDataset
     * @param terms the list of terms to replace on the QlikDataset, or null to remove all terms from the QlikDataset
     * @return the QlikDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikDataset, without replacing existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to append to the QlikDataset
     * @return the QlikDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QlikDataset, without replacing existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikDataset
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to append to the QlikDataset
     * @return the QlikDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikDataset, without replacing all existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to remove from the QlikDataset, which must be referenced by GUID
     * @return the QlikDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikDataset, without replacing all existing terms linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikDataset
     * @param qualifiedName for the QlikDataset
     * @param terms the list of terms to remove from the QlikDataset, which must be referenced by GUID
     * @return the QlikDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikDataset, without replacing existing Atlan tags linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikDataset
     */
    public static QlikDataset appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikDataset, without replacing existing Atlan tags linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikDataset
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikDataset
     */
    public static QlikDataset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikDataset, without replacing existing Atlan tags linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikDataset
     */
    public static QlikDataset appendAtlanTags(
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
     * Add Atlan tags to a QlikDataset, without replacing existing Atlan tags linked to the QlikDataset.
     * Note: this operation must make two API calls — one to retrieve the QlikDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikDataset
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikDataset
     */
    public static QlikDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikDataset
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikDataset
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikDataset
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikDataset
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
     * Add Atlan tags to a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikDataset
     * @param qualifiedName of the QlikDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikDataset
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
     * Remove an Atlan tag from a QlikDataset.
     *
     * @param qualifiedName of the QlikDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikDataset
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QlikDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikDataset
     * @param qualifiedName of the QlikDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikDataset
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
