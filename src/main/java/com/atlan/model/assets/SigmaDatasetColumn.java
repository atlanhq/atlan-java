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
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sigma dataset column in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SigmaDatasetColumn extends Sigma {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDatasetColumn";

    /** Fixed typeName for SigmaDatasetColumns. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique name of the dataset that contains this column. */
    @Attribute
    String sigmaDatasetQualifiedName;

    /** Human-readable name of the dataset that contains this column. */
    @Attribute
    String sigmaDatasetName;

    /** Dataset that contains this column. */
    @Attribute
    SigmaDataset sigmaDataset;

    /**
     * Reference to a SigmaDatasetColumn by GUID.
     *
     * @param guid the GUID of the SigmaDatasetColumn to reference
     * @return reference to a SigmaDatasetColumn that can be used for defining a relationship to a SigmaDatasetColumn
     */
    public static SigmaDatasetColumn refByGuid(String guid) {
        return SigmaDatasetColumn.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaDatasetColumn by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaDatasetColumn to reference
     * @return reference to a SigmaDatasetColumn that can be used for defining a relationship to a SigmaDatasetColumn
     */
    public static SigmaDatasetColumn refByQualifiedName(String qualifiedName) {
        return SigmaDatasetColumn.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaDatasetColumn by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDatasetColumn to retrieve
     * @return the requested full SigmaDatasetColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDatasetColumn does not exist or the provided GUID is not a SigmaDatasetColumn
     */
    public static SigmaDatasetColumn retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaDatasetColumn) {
            return (SigmaDatasetColumn) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaDatasetColumn");
        }
    }

    /**
     * Retrieves a SigmaDatasetColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDatasetColumn to retrieve
     * @return the requested full SigmaDatasetColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDatasetColumn does not exist
     */
    public static SigmaDatasetColumn retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaDatasetColumn) {
            return (SigmaDatasetColumn) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaDatasetColumn");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaDatasetColumn to active.
     *
     * @param qualifiedName for the SigmaDatasetColumn
     * @return true if the SigmaDatasetColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the minimal request necessary to update the SigmaDatasetColumn, as a builder
     */
    public static SigmaDatasetColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDatasetColumn.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDatasetColumn, from a potentially
     * more-complete SigmaDatasetColumn object.
     *
     * @return the minimal object necessary to update the SigmaDatasetColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDatasetColumn are not found in the initial object
     */
    @Override
    public SigmaDatasetColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDatasetColumn", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the updated SigmaDatasetColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the updated SigmaDatasetColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the updated SigmaDatasetColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDatasetColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (SigmaDatasetColumn) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the updated SigmaDatasetColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaDatasetColumn) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param name of the SigmaDatasetColumn
     * @return the updated SigmaDatasetColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDatasetColumn.
     *
     * @param qualifiedName for the SigmaDatasetColumn
     * @param name human-readable name of the SigmaDatasetColumn
     * @param terms the list of terms to replace on the SigmaDatasetColumn, or null to remove all terms from the SigmaDatasetColumn
     * @return the SigmaDatasetColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDatasetColumn) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDatasetColumn, without replacing existing terms linked to the SigmaDatasetColumn.
     * Note: this operation must make two API calls — one to retrieve the SigmaDatasetColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDatasetColumn
     * @param terms the list of terms to append to the SigmaDatasetColumn
     * @return the SigmaDatasetColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaDatasetColumn) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDatasetColumn, without replacing all existing terms linked to the SigmaDatasetColumn.
     * Note: this operation must make two API calls — one to retrieve the SigmaDatasetColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDatasetColumn
     * @param terms the list of terms to remove from the SigmaDatasetColumn, which must be referenced by GUID
     * @return the SigmaDatasetColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDatasetColumn removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaDatasetColumn) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDatasetColumn, without replacing existing Atlan tags linked to the SigmaDatasetColumn.
     * Note: this operation must make two API calls — one to retrieve the SigmaDatasetColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDatasetColumn
     */
    public static SigmaDatasetColumn appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SigmaDatasetColumn) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDatasetColumn, without replacing existing Atlan tags linked to the SigmaDatasetColumn.
     * Note: this operation must make two API calls — one to retrieve the SigmaDatasetColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDatasetColumn
     */
    public static SigmaDatasetColumn appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDatasetColumn) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDatasetColumn
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDatasetColumn
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
     * Remove an Atlan tag from a SigmaDatasetColumn.
     *
     * @param qualifiedName of the SigmaDatasetColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDatasetColumn
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
