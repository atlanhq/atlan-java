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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sigma workbook in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SigmaWorkbook extends Sigma {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaWorkbook";

    /** Fixed typeName for SigmaWorkbooks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of pages that exist within this workbook. */
    @Attribute
    Long sigmaPageCount;

    /** Pages that exist within this workbook. */
    @Attribute
    @Singular
    SortedSet<SigmaPage> sigmaPages;

    /**
     * Reference to a SigmaWorkbook by GUID.
     *
     * @param guid the GUID of the SigmaWorkbook to reference
     * @return reference to a SigmaWorkbook that can be used for defining a relationship to a SigmaWorkbook
     */
    public static SigmaWorkbook refByGuid(String guid) {
        return SigmaWorkbook.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaWorkbook by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaWorkbook to reference
     * @return reference to a SigmaWorkbook that can be used for defining a relationship to a SigmaWorkbook
     */
    public static SigmaWorkbook refByQualifiedName(String qualifiedName) {
        return SigmaWorkbook.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaWorkbook by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaWorkbook to retrieve
     * @return the requested full SigmaWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaWorkbook does not exist or the provided GUID is not a SigmaWorkbook
     */
    public static SigmaWorkbook retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaWorkbook) {
            return (SigmaWorkbook) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaWorkbook");
        }
    }

    /**
     * Retrieves a SigmaWorkbook by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaWorkbook to retrieve
     * @return the requested full SigmaWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaWorkbook does not exist
     */
    public static SigmaWorkbook retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaWorkbook) {
            return (SigmaWorkbook) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaWorkbook");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaWorkbook to active.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @return true if the SigmaWorkbook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the minimal request necessary to update the SigmaWorkbook, as a builder
     */
    public static SigmaWorkbookBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaWorkbook.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaWorkbook, from a potentially
     * more-complete SigmaWorkbook object.
     *
     * @return the minimal object necessary to update the SigmaWorkbook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaWorkbook are not found in the initial object
     */
    @Override
    public SigmaWorkbookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaWorkbook", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaWorkbook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaWorkbook) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaWorkbook) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaWorkbook.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param name human-readable name of the SigmaWorkbook
     * @param terms the list of terms to replace on the SigmaWorkbook, or null to remove all terms from the SigmaWorkbook
     * @return the SigmaWorkbook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaWorkbook) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaWorkbook, without replacing existing terms linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param terms the list of terms to append to the SigmaWorkbook
     * @return the SigmaWorkbook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaWorkbook) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaWorkbook, without replacing all existing terms linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param terms the list of terms to remove from the SigmaWorkbook, which must be referenced by GUID
     * @return the SigmaWorkbook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaWorkbook) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaWorkbook, without replacing existing Atlan tags linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaWorkbook
     */
    public static SigmaWorkbook appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SigmaWorkbook) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaWorkbook, without replacing existing Atlan tags linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaWorkbook
     */
    public static SigmaWorkbook appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaWorkbook) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaWorkbook
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaWorkbook
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
     * Remove an Atlan tag from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaWorkbook
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
