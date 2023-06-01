/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.IconType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a query collection in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class AtlanCollection extends Namespace {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Collection";

    /** Fixed typeName for AtlanCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String icon;

    /** TBC */
    @Attribute
    IconType iconType;

    /**
     * Reference to a AtlanCollection by GUID.
     *
     * @param guid the GUID of the AtlanCollection to reference
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByGuid(String guid) {
        return AtlanCollection.builder().guid(guid).build();
    }

    /**
     * Reference to a AtlanCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the AtlanCollection to reference
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByQualifiedName(String qualifiedName) {
        return AtlanCollection.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a AtlanCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the AtlanCollection to retrieve
     * @return the requested full AtlanCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist or the provided GUID is not a AtlanCollection
     */
    public static AtlanCollection retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof AtlanCollection) {
            return (AtlanCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "AtlanCollection");
        }
    }

    /**
     * Retrieves a AtlanCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the AtlanCollection to retrieve
     * @return the requested full AtlanCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist
     */
    public static AtlanCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof AtlanCollection) {
            return (AtlanCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "AtlanCollection");
        }
    }

    /**
     * Restore the archived (soft-deleted) AtlanCollection to active.
     *
     * @param qualifiedName for the AtlanCollection
     * @return true if the AtlanCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the minimal request necessary to update the AtlanCollection, as a builder
     */
    public static AtlanCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanCollection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanCollection, from a potentially
     * more-complete AtlanCollection object.
     *
     * @return the minimal object necessary to update the AtlanCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanCollection are not found in the initial object
     */
    @Override
    public AtlanCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "AtlanCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return (AtlanCollection) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (AtlanCollection) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return (AtlanCollection) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanCollection) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (AtlanCollection) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (AtlanCollection) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (AtlanCollection) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AtlanCollection.
     *
     * @param qualifiedName for the AtlanCollection
     * @param name human-readable name of the AtlanCollection
     * @param terms the list of terms to replace on the AtlanCollection, or null to remove all terms from the AtlanCollection
     * @return the AtlanCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (AtlanCollection) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AtlanCollection, without replacing existing terms linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AtlanCollection
     * @param terms the list of terms to append to the AtlanCollection
     * @return the AtlanCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanCollection) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AtlanCollection, without replacing all existing terms linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AtlanCollection
     * @param terms the list of terms to remove from the AtlanCollection, which must be referenced by GUID
     * @return the AtlanCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanCollection) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AtlanCollection, without replacing existing Atlan tags linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AtlanCollection
     */
    public static AtlanCollection appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AtlanCollection) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AtlanCollection, without replacing existing Atlan tags linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AtlanCollection
     */
    public static AtlanCollection appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AtlanCollection) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the AtlanCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the AtlanCollection
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
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AtlanCollection
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
