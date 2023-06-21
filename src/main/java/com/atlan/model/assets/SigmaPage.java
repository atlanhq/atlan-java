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
 * Instance of a Sigma page in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SigmaPage extends Asset implements ISigmaPage, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaPage";

    /** Fixed typeName for SigmaPages. */
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

    /** Number of data elements that exist within this page. */
    @Attribute
    Long sigmaDataElementCount;

    /** TBC */
    @Attribute
    String sigmaDataElementName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** Data elements within this page. */
    @Attribute
    @Singular
    SortedSet<ISigmaDataElement> sigmaDataElements;

    /** TBC */
    @Attribute
    String sigmaPageName;

    /** TBC */
    @Attribute
    String sigmaPageQualifiedName;

    /** Workbook that contains this page. */
    @Attribute
    ISigmaWorkbook sigmaWorkbook;

    /** TBC */
    @Attribute
    String sigmaWorkbookName;

    /** TBC */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /**
     * Reference to a SigmaPage by GUID.
     *
     * @param guid the GUID of the SigmaPage to reference
     * @return reference to a SigmaPage that can be used for defining a relationship to a SigmaPage
     */
    public static SigmaPage refByGuid(String guid) {
        return SigmaPage.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaPage by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaPage to reference
     * @return reference to a SigmaPage that can be used for defining a relationship to a SigmaPage
     */
    public static SigmaPage refByQualifiedName(String qualifiedName) {
        return SigmaPage.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaPage by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaPage to retrieve
     * @return the requested full SigmaPage, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaPage does not exist or the provided GUID is not a SigmaPage
     */
    public static SigmaPage retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaPage) {
            return (SigmaPage) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaPage");
        }
    }

    /**
     * Retrieves a SigmaPage by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaPage to retrieve
     * @return the requested full SigmaPage, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaPage does not exist
     */
    public static SigmaPage retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaPage) {
            return (SigmaPage) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaPage");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaPage to active.
     *
     * @param qualifiedName for the SigmaPage
     * @return true if the SigmaPage is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the minimal request necessary to update the SigmaPage, as a builder
     */
    public static SigmaPageBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaPage.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaPage, from a potentially
     * more-complete SigmaPage object.
     *
     * @return the minimal object necessary to update the SigmaPage, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaPage are not found in the initial object
     */
    @Override
    public SigmaPageBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaPage", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the updated SigmaPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaPage) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the updated SigmaPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaPage) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the updated SigmaPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaPage) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaPage, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaPage) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the updated SigmaPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaPage) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaPage) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param name of the SigmaPage
     * @return the updated SigmaPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaPage) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaPage.
     *
     * @param qualifiedName for the SigmaPage
     * @param name human-readable name of the SigmaPage
     * @param terms the list of terms to replace on the SigmaPage, or null to remove all terms from the SigmaPage
     * @return the SigmaPage that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaPage replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaPage) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaPage, without replacing existing terms linked to the SigmaPage.
     * Note: this operation must make two API calls — one to retrieve the SigmaPage's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaPage
     * @param terms the list of terms to append to the SigmaPage
     * @return the SigmaPage that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaPage appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaPage) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaPage, without replacing all existing terms linked to the SigmaPage.
     * Note: this operation must make two API calls — one to retrieve the SigmaPage's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaPage
     * @param terms the list of terms to remove from the SigmaPage, which must be referenced by GUID
     * @return the SigmaPage that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaPage removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaPage) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaPage, without replacing existing Atlan tags linked to the SigmaPage.
     * Note: this operation must make two API calls — one to retrieve the SigmaPage's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaPage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaPage
     */
    public static SigmaPage appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SigmaPage) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaPage, without replacing existing Atlan tags linked to the SigmaPage.
     * Note: this operation must make two API calls — one to retrieve the SigmaPage's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaPage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaPage
     */
    public static SigmaPage appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaPage) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaPage
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaPage
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
     * Remove an Atlan tag from a SigmaPage.
     *
     * @param qualifiedName of the SigmaPage
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaPage
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
