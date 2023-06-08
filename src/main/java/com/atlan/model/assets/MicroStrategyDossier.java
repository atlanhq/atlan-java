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
 * Instance of a MicroStrategy dossier in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MicroStrategyDossier extends MicroStrategy {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyDossier";

    /** Fixed typeName for MicroStrategyDossiers. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** List of names of the dossier chapters. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyDossierChapterNames;

    /** Project containing the dossier. */
    @Attribute
    MicroStrategyProject microStrategyProject;

    /** Visualizations used within the dossier. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyVisualization> microStrategyVisualizations;

    /**
     * Reference to a MicroStrategyDossier by GUID.
     *
     * @param guid the GUID of the MicroStrategyDossier to reference
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByGuid(String guid) {
        return MicroStrategyDossier.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyDossier by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyDossier to reference
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByQualifiedName(String qualifiedName) {
        return MicroStrategyDossier.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyDossier by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyDossier to retrieve
     * @return the requested full MicroStrategyDossier, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist or the provided GUID is not a MicroStrategyDossier
     */
    public static MicroStrategyDossier retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyDossier) {
            return (MicroStrategyDossier) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyDossier");
        }
    }

    /**
     * Retrieves a MicroStrategyDossier by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyDossier to retrieve
     * @return the requested full MicroStrategyDossier, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist
     */
    public static MicroStrategyDossier retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyDossier) {
            return (MicroStrategyDossier) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyDossier");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyDossier to active.
     *
     * @param qualifiedName for the MicroStrategyDossier
     * @return true if the MicroStrategyDossier is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the minimal request necessary to update the MicroStrategyDossier, as a builder
     */
    public static MicroStrategyDossierBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyDossier.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyDossier, from a potentially
     * more-complete MicroStrategyDossier object.
     *
     * @return the minimal object necessary to update the MicroStrategyDossier, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyDossier are not found in the initial object
     */
    @Override
    public MicroStrategyDossierBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyDossier", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyDossier) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyDossier) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyDossier) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyDossier, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyDossier)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyDossier) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyDossier)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyDossier) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyDossier.
     *
     * @param qualifiedName for the MicroStrategyDossier
     * @param name human-readable name of the MicroStrategyDossier
     * @param terms the list of terms to replace on the MicroStrategyDossier, or null to remove all terms from the MicroStrategyDossier
     * @return the MicroStrategyDossier that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyDossier, without replacing existing terms linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyDossier
     * @param terms the list of terms to append to the MicroStrategyDossier
     * @return the MicroStrategyDossier that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyDossier, without replacing all existing terms linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyDossier
     * @param terms the list of terms to remove from the MicroStrategyDossier, which must be referenced by GUID
     * @return the MicroStrategyDossier that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier, without replacing existing Atlan tags linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDossier
     */
    public static MicroStrategyDossier appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier, without replacing existing Atlan tags linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDossier
     */
    public static MicroStrategyDossier appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDossier
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDossier
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
     * Remove an Atlan tag from a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyDossier
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
