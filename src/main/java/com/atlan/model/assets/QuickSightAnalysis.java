/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.QuickSightAnalysisStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Analysis asset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class QuickSightAnalysis extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightAnalysis";

    /** Fixed typeName for QuickSightAnalysiss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    QuickSightAnalysisStatus quickSightAnalysisStatus;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisCalculatedFields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisParameterDeclarations;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisFilterGroups;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightAnalysisVisual> quickSightAnalysisVisuals;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<QuickSightFolder> quickSightAnalysisFolders;

    /**
     * Reference to a QuickSightAnalysis by GUID.
     *
     * @param guid the GUID of the QuickSightAnalysis to reference
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByGuid(String guid) {
        return QuickSightAnalysis.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightAnalysis by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightAnalysis to reference
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByQualifiedName(String qualifiedName) {
        return QuickSightAnalysis.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightAnalysis by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightAnalysis to retrieve
     * @return the requested full QuickSightAnalysis, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist or the provided GUID is not a QuickSightAnalysis
     */
    public static QuickSightAnalysis retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightAnalysis) {
            return (QuickSightAnalysis) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightAnalysis");
        }
    }

    /**
     * Retrieves a QuickSightAnalysis by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightAnalysis to retrieve
     * @return the requested full QuickSightAnalysis, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist
     */
    public static QuickSightAnalysis retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightAnalysis) {
            return (QuickSightAnalysis) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightAnalysis");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightAnalysis to active.
     *
     * @param qualifiedName for the QuickSightAnalysis
     * @return true if the QuickSightAnalysis is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the minimal request necessary to update the QuickSightAnalysis, as a builder
     */
    public static QuickSightAnalysisBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightAnalysis.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightAnalysis, from a potentially
     * more-complete QuickSightAnalysis object.
     *
     * @return the minimal object necessary to update the QuickSightAnalysis, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightAnalysis are not found in the initial object
     */
    @Override
    public QuickSightAnalysisBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightAnalysis", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightAnalysis, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (QuickSightAnalysis) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QuickSightAnalysis) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightAnalysis.
     *
     * @param qualifiedName for the QuickSightAnalysis
     * @param name human-readable name of the QuickSightAnalysis
     * @param terms the list of terms to replace on the QuickSightAnalysis, or null to remove all terms from the QuickSightAnalysis
     * @return the QuickSightAnalysis that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightAnalysis, without replacing existing terms linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightAnalysis
     * @param terms the list of terms to append to the QuickSightAnalysis
     * @return the QuickSightAnalysis that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysis) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightAnalysis, without replacing all existing terms linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightAnalysis
     * @param terms the list of terms to remove from the QuickSightAnalysis, which must be referenced by GUID
     * @return the QuickSightAnalysis that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysis) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis, without replacing existing Atlan tags linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysis
     */
    public static QuickSightAnalysis appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis, without replacing existing Atlan tags linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysis
     */
    public static QuickSightAnalysis appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysis
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysis
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
     * Remove an Atlan tag from a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightAnalysis
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
