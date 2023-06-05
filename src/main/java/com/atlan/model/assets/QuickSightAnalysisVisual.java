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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Analysis sheet visual in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class QuickSightAnalysisVisual extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightAnalysisVisual";

    /** Fixed typeName for QuickSightAnalysisVisuals. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String quickSightAnalysisQualifiedName;

    /** TBC */
    @Attribute
    QuickSightAnalysis quickSightAnalysis;

    /**
     * Reference to a QuickSightAnalysisVisual by GUID.
     *
     * @param guid the GUID of the QuickSightAnalysisVisual to reference
     * @return reference to a QuickSightAnalysisVisual that can be used for defining a relationship to a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual refByGuid(String guid) {
        return QuickSightAnalysisVisual.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightAnalysisVisual by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightAnalysisVisual to reference
     * @return reference to a QuickSightAnalysisVisual that can be used for defining a relationship to a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual refByQualifiedName(String qualifiedName) {
        return QuickSightAnalysisVisual.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist or the provided GUID is not a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightAnalysisVisual) {
            return (QuickSightAnalysisVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightAnalysisVisual");
        }
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist
     */
    public static QuickSightAnalysisVisual retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightAnalysisVisual) {
            return (QuickSightAnalysisVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightAnalysisVisual");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightAnalysisVisual to active.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @return true if the QuickSightAnalysisVisual is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the minimal request necessary to update the QuickSightAnalysisVisual, as a builder
     */
    public static QuickSightAnalysisVisualBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightAnalysisVisual.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightAnalysisVisual, from a potentially
     * more-complete QuickSightAnalysisVisual object.
     *
     * @return the minimal object necessary to update the QuickSightAnalysisVisual, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightAnalysisVisual are not found in the initial object
     */
    @Override
    public QuickSightAnalysisVisualBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightAnalysisVisual", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightAnalysisVisual, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (QuickSightAnalysisVisual)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QuickSightAnalysisVisual)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightAnalysisVisual.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param name human-readable name of the QuickSightAnalysisVisual
     * @param terms the list of terms to replace on the QuickSightAnalysisVisual, or null to remove all terms from the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightAnalysisVisual, without replacing existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to append to the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightAnalysisVisual, without replacing all existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to remove from the QuickSightAnalysisVisual, which must be referenced by GUID
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
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
     * Remove an Atlan tag from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightAnalysisVisual
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
