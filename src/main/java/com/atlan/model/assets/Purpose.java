/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Purpose access control object in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Purpose extends AccessControl {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Purpose";

    /** Fixed typeName for Purposes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tags on which this purpose is applied. */
    @Attribute
    @Singular
    @JsonProperty("purposeClassifications")
    SortedSet<String> purposeAtlanTags;

    /**
     * Reference to a Purpose by GUID.
     *
     * @param guid the GUID of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByGuid(String guid) {
        return Purpose.builder().guid(guid).build();
    }

    /**
     * Reference to a Purpose by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByQualifiedName(String qualifiedName) {
        return Purpose.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Purpose by its GUID, complete with all of its relationships.
     *
     * @param guid of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    public static Purpose retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Purpose) {
            return (Purpose) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Purpose");
        }
    }

    /**
     * Retrieves a Purpose by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist
     */
    public static Purpose retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Purpose) {
            return (Purpose) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Purpose");
        }
    }

    /**
     * Restore the archived (soft-deleted) Purpose to active.
     *
     * @param qualifiedName for the Purpose
     * @return true if the Purpose is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the minimal request necessary to update the Purpose, as a builder
     */
    public static PurposeBuilder<?, ?> updater(String qualifiedName, String name) {
        return Purpose.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Purpose, from a potentially
     * more-complete Purpose object.
     *
     * @return the minimal object necessary to update the Purpose, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Purpose are not found in the initial object
     */
    @Override
    public PurposeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Purpose", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Purpose) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Purpose) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Purpose) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Purpose, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Purpose updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Purpose) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Purpose) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Purpose updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Purpose) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Purpose) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Purpose.
     *
     * @param qualifiedName for the Purpose
     * @param name human-readable name of the Purpose
     * @param terms the list of terms to replace on the Purpose, or null to remove all terms from the Purpose
     * @return the Purpose that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Purpose replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Purpose) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Purpose, without replacing existing terms linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Purpose
     * @param terms the list of terms to append to the Purpose
     * @return the Purpose that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Purpose appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Purpose) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Purpose, without replacing all existing terms linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Purpose
     * @param terms the list of terms to remove from the Purpose, which must be referenced by GUID
     * @return the Purpose that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Purpose removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Purpose) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Purpose, without replacing existing Atlan tags linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     */
    public static Purpose appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (Purpose) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose, without replacing existing Atlan tags linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     */
    public static Purpose appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Purpose) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
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
     * Remove an Atlan tag from a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Purpose
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
