/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QuickSightDatasetFieldType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Dataset field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class QuickSightDatasetField extends Asset
        implements IQuickSightDatasetField, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDatasetField";

    /** Fixed typeName for QuickSightDatasetFields. */
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
    IQuickSightDataset quickSightDataset;

    /** Datatype of column in the dataset */
    @Attribute
    QuickSightDatasetFieldType quickSightDatasetFieldType;

    /** Qualified name of the parent dataset */
    @Attribute
    String quickSightDatasetQualifiedName;

    /** TBC */
    @Attribute
    String quickSightId;

    /** TBC */
    @Attribute
    String quickSightSheetId;

    /** TBC */
    @Attribute
    String quickSightSheetName;

    /**
     * Reference to a QuickSightDatasetField by GUID.
     *
     * @param guid the GUID of the QuickSightDatasetField to reference
     * @return reference to a QuickSightDatasetField that can be used for defining a relationship to a QuickSightDatasetField
     */
    public static QuickSightDatasetField refByGuid(String guid) {
        return QuickSightDatasetField.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightDatasetField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightDatasetField to reference
     * @return reference to a QuickSightDatasetField that can be used for defining a relationship to a QuickSightDatasetField
     */
    public static QuickSightDatasetField refByQualifiedName(String qualifiedName) {
        return QuickSightDatasetField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightDatasetField by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist or the provided GUID is not a QuickSightDatasetField
     */
    public static QuickSightDatasetField retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightDatasetField) {
            return (QuickSightDatasetField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightDatasetField");
        }
    }

    /**
     * Retrieves a QuickSightDatasetField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist
     */
    public static QuickSightDatasetField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightDatasetField) {
            return (QuickSightDatasetField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightDatasetField");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDatasetField to active.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @return true if the QuickSightDatasetField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the minimal request necessary to update the QuickSightDatasetField, as a builder
     */
    public static QuickSightDatasetFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightDatasetField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightDatasetField, from a potentially
     * more-complete QuickSightDatasetField object.
     *
     * @return the minimal object necessary to update the QuickSightDatasetField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightDatasetField are not found in the initial object
     */
    @Override
    public QuickSightDatasetFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightDatasetField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDatasetField) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDatasetField) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDatasetField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (QuickSightDatasetField)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDatasetField) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QuickSightDatasetField)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDatasetField) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightDatasetField.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param name human-readable name of the QuickSightDatasetField
     * @param terms the list of terms to replace on the QuickSightDatasetField, or null to remove all terms from the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightDatasetField, without replacing existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to append to the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDatasetField, without replacing all existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to remove from the QuickSightDatasetField, which must be referenced by GUID
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
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
     * Remove an Atlan tag from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightDatasetField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
