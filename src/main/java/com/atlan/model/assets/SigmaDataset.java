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
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sigma dataset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SigmaDataset extends Asset implements ISigmaDataset, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataset";

    /** Fixed typeName for SigmaDatasets. */
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
    String sigmaDataElementName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** Number of columns that exist within this dataset. */
    @Attribute
    Long sigmaDatasetColumnCount;

    /** Columns that exist within this dataset. */
    @Attribute
    @Singular
    SortedSet<ISigmaDatasetColumn> sigmaDatasetColumns;

    /** TBC */
    @Attribute
    String sigmaPageName;

    /** TBC */
    @Attribute
    String sigmaPageQualifiedName;

    /** TBC */
    @Attribute
    String sigmaWorkbookName;

    /** TBC */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /**
     * Reference to a SigmaDataset by GUID.
     *
     * @param guid the GUID of the SigmaDataset to reference
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByGuid(String guid) {
        return SigmaDataset.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaDataset by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataset to reference
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByQualifiedName(String qualifiedName) {
        return SigmaDataset.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaDataset by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDataset to retrieve
     * @return the requested full SigmaDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist or the provided GUID is not a SigmaDataset
     */
    public static SigmaDataset retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaDataset) {
            return (SigmaDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaDataset");
        }
    }

    /**
     * Retrieves a SigmaDataset by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDataset to retrieve
     * @return the requested full SigmaDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist
     */
    public static SigmaDataset retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaDataset) {
            return (SigmaDataset) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaDataset");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataset to active.
     *
     * @param qualifiedName for the SigmaDataset
     * @return true if the SigmaDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the minimal request necessary to update the SigmaDataset, as a builder
     */
    public static SigmaDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataset, from a potentially
     * more-complete SigmaDataset object.
     *
     * @return the minimal object necessary to update the SigmaDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataset are not found in the initial object
     */
    @Override
    public SigmaDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataset) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataset) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataset) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaDataset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataset) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaDataset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataset) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDataset.
     *
     * @param qualifiedName for the SigmaDataset
     * @param name human-readable name of the SigmaDataset
     * @param terms the list of terms to replace on the SigmaDataset, or null to remove all terms from the SigmaDataset
     * @return the SigmaDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataset, without replacing existing terms linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDataset
     * @param terms the list of terms to append to the SigmaDataset
     * @return the SigmaDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataset, without replacing all existing terms linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDataset
     * @param terms the list of terms to remove from the SigmaDataset, which must be referenced by GUID
     * @return the SigmaDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDataset, without replacing existing Atlan tags linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataset
     */
    public static SigmaDataset appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SigmaDataset) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataset, without replacing existing Atlan tags linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataset
     */
    public static SigmaDataset appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDataset) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataset
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataset
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
     * Remove an Atlan tag from a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataset
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
