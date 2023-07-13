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
 * Instance of a Metabase collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MetabaseCollection extends Asset
        implements IMetabaseCollection, IMetabase, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseCollection";

    /** Fixed typeName for MetabaseCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    String metabaseCollectionName;

    /** TBC */
    @Attribute
    String metabaseCollectionQualifiedName;

    /** TBC */
    @Attribute
    String metabaseColor;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetabaseDashboard> metabaseDashboards;

    /** TBC */
    @Attribute
    Boolean metabaseIsPersonalCollection;

    /** TBC */
    @Attribute
    String metabaseNamespace;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetabaseQuestion> metabaseQuestions;

    /** TBC */
    @Attribute
    String metabaseSlug;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a MetabaseCollection by GUID.
     *
     * @param guid the GUID of the MetabaseCollection to reference
     * @return reference to a MetabaseCollection that can be used for defining a relationship to a MetabaseCollection
     */
    public static MetabaseCollection refByGuid(String guid) {
        return MetabaseCollection.builder().guid(guid).build();
    }

    /**
     * Reference to a MetabaseCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseCollection to reference
     * @return reference to a MetabaseCollection that can be used for defining a relationship to a MetabaseCollection
     */
    public static MetabaseCollection refByQualifiedName(String qualifiedName) {
        return MetabaseCollection.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MetabaseCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist or the provided GUID is not a MetabaseCollection
     */
    public static MetabaseCollection retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MetabaseCollection by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist or the provided GUID is not a MetabaseCollection
     */
    public static MetabaseCollection retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MetabaseCollection) {
            return (MetabaseCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MetabaseCollection");
        }
    }

    /**
     * Retrieves a MetabaseCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist
     */
    public static MetabaseCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MetabaseCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist
     */
    public static MetabaseCollection retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof MetabaseCollection) {
            return (MetabaseCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MetabaseCollection");
        }
    }

    /**
     * Restore the archived (soft-deleted) MetabaseCollection to active.
     *
     * @param qualifiedName for the MetabaseCollection
     * @return true if the MetabaseCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MetabaseCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MetabaseCollection
     * @return true if the MetabaseCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the minimal request necessary to update the MetabaseCollection, as a builder
     */
    public static MetabaseCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseCollection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseCollection, from a potentially
     * more-complete MetabaseCollection object.
     *
     * @return the minimal object necessary to update the MetabaseCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MetabaseCollection are not found in the initial object
     */
    @Override
    public MetabaseCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MetabaseCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseCollection's owners
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseCollection's certificate
     * @param qualifiedName of the MetabaseCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MetabaseCollection)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseCollection's certificate
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseCollection's announcement
     * @param qualifiedName of the MetabaseCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MetabaseCollection)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the MetabaseCollection's announcement
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MetabaseCollection.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param name human-readable name of the MetabaseCollection
     * @param terms the list of terms to replace on the MetabaseCollection, or null to remove all terms from the MetabaseCollection
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MetabaseCollection's assigned terms
     * @param qualifiedName for the MetabaseCollection
     * @param name human-readable name of the MetabaseCollection
     * @param terms the list of terms to replace on the MetabaseCollection, or null to remove all terms from the MetabaseCollection
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MetabaseCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseCollection, without replacing existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to append to the MetabaseCollection
     * @return the MetabaseCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MetabaseCollection, without replacing existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MetabaseCollection
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to append to the MetabaseCollection
     * @return the MetabaseCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseCollection, without replacing all existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to remove from the MetabaseCollection, which must be referenced by GUID
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseCollection, without replacing all existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MetabaseCollection
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to remove from the MetabaseCollection, which must be referenced by GUID
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MetabaseCollection, without replacing existing Atlan tags linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseCollection
     */
    public static MetabaseCollection appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseCollection, without replacing existing Atlan tags linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseCollection
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseCollection
     */
    public static MetabaseCollection appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MetabaseCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseCollection, without replacing existing Atlan tags linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseCollection
     */
    public static MetabaseCollection appendAtlanTags(
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
     * Add Atlan tags to a MetabaseCollection, without replacing existing Atlan tags linked to the MetabaseCollection.
     * Note: this operation must make two API calls — one to retrieve the MetabaseCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseCollection
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseCollection
     */
    public static MetabaseCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MetabaseCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseCollection
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseCollection
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
     * Add Atlan tags to a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseCollection
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseCollection
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
     * Remove an Atlan tag from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseCollection
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MetabaseCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MetabaseCollection
     * @param qualifiedName of the MetabaseCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseCollection
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
