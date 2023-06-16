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
 * Instance of a Sigma data element in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SigmaDataElement extends Asset
        implements ISigmaDataElement, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataElement";

    /** Fixed typeName for SigmaDataElements. */
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

    /** Number of data element fields within this data element. */
    @Attribute
    Long sigmaDataElementFieldCount;

    /** Data element fields within this data element. */
    @Attribute
    @Singular
    SortedSet<ISigmaDataElementField> sigmaDataElementFields;

    /** TBC */
    @Attribute
    String sigmaDataElementName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** TBC */
    @Attribute
    String sigmaDataElementQuery;

    /** TBC */
    @Attribute
    String sigmaDataElementType;

    /** Page that contains this data element. */
    @Attribute
    ISigmaPage sigmaPage;

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
     * Reference to a SigmaDataElement by GUID.
     *
     * @param guid the GUID of the SigmaDataElement to reference
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByGuid(String guid) {
        return SigmaDataElement.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaDataElement by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataElement to reference
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByQualifiedName(String qualifiedName) {
        return SigmaDataElement.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaDataElement by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     */
    public static SigmaDataElement retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaDataElement) {
            return (SigmaDataElement) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaDataElement");
        }
    }

    /**
     * Retrieves a SigmaDataElement by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist
     */
    public static SigmaDataElement retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaDataElement) {
            return (SigmaDataElement) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaDataElement");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElement to active.
     *
     * @param qualifiedName for the SigmaDataElement
     * @return true if the SigmaDataElement is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the minimal request necessary to update the SigmaDataElement, as a builder
     */
    public static SigmaDataElementBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataElement.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataElement, from a potentially
     * more-complete SigmaDataElement object.
     *
     * @return the minimal object necessary to update the SigmaDataElement, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataElement are not found in the initial object
     */
    @Override
    public SigmaDataElementBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDataElement", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElement) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElement) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElement) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElement, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (SigmaDataElement) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElement) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaDataElement) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElement) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDataElement.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param name human-readable name of the SigmaDataElement
     * @param terms the list of terms to replace on the SigmaDataElement, or null to remove all terms from the SigmaDataElement
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElement) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataElement, without replacing existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to append to the SigmaDataElement
     * @return the SigmaDataElement that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataElement) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElement, without replacing all existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to remove from the SigmaDataElement, which must be referenced by GUID
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataElement) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SigmaDataElement) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDataElement) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
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
     * Remove an Atlan tag from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataElement
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
