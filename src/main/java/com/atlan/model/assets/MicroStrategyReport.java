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
 * Instance of a MicroStrategy report in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MicroStrategyReport extends MicroStrategy {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyReport";

    /** Fixed typeName for MicroStrategyReports. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether the report is a Grid or Chart. */
    @Attribute
    String microStrategyReportType;

    /** Metrics used within the report. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyMetric> microStrategyMetrics;

    /** Project containing the report. */
    @Attribute
    MicroStrategyProject microStrategyProject;

    /** Attributes related to this report. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyAttribute> microStrategyAttributes;

    /**
     * Reference to a MicroStrategyReport by GUID.
     *
     * @param guid the GUID of the MicroStrategyReport to reference
     * @return reference to a MicroStrategyReport that can be used for defining a relationship to a MicroStrategyReport
     */
    public static MicroStrategyReport refByGuid(String guid) {
        return MicroStrategyReport.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyReport to reference
     * @return reference to a MicroStrategyReport that can be used for defining a relationship to a MicroStrategyReport
     */
    public static MicroStrategyReport refByQualifiedName(String qualifiedName) {
        return MicroStrategyReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyReport to retrieve
     * @return the requested full MicroStrategyReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyReport does not exist or the provided GUID is not a MicroStrategyReport
     */
    public static MicroStrategyReport retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyReport) {
            return (MicroStrategyReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyReport");
        }
    }

    /**
     * Retrieves a MicroStrategyReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyReport to retrieve
     * @return the requested full MicroStrategyReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyReport does not exist
     */
    public static MicroStrategyReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyReport) {
            return (MicroStrategyReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyReport");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyReport to active.
     *
     * @param qualifiedName for the MicroStrategyReport
     * @return true if the MicroStrategyReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the minimal request necessary to update the MicroStrategyReport, as a builder
     */
    public static MicroStrategyReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyReport, from a potentially
     * more-complete MicroStrategyReport object.
     *
     * @return the minimal object necessary to update the MicroStrategyReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyReport are not found in the initial object
     */
    @Override
    public MicroStrategyReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyReport", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the updated MicroStrategyReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyReport) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the updated MicroStrategyReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyReport) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the updated MicroStrategyReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyReport) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyReport) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the updated MicroStrategyReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyReport) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyReport)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param name of the MicroStrategyReport
     * @return the updated MicroStrategyReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyReport) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyReport.
     *
     * @param qualifiedName for the MicroStrategyReport
     * @param name human-readable name of the MicroStrategyReport
     * @param terms the list of terms to replace on the MicroStrategyReport, or null to remove all terms from the MicroStrategyReport
     * @return the MicroStrategyReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyReport) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyReport, without replacing existing terms linked to the MicroStrategyReport.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyReport
     * @param terms the list of terms to append to the MicroStrategyReport
     * @return the MicroStrategyReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyReport) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyReport, without replacing all existing terms linked to the MicroStrategyReport.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyReport
     * @param terms the list of terms to remove from the MicroStrategyReport, which must be referenced by GUID
     * @return the MicroStrategyReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyReport removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyReport) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyReport, without replacing existing Atlan tags linked to the MicroStrategyReport.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyReport
     */
    public static MicroStrategyReport appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyReport) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyReport, without replacing existing Atlan tags linked to the MicroStrategyReport.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyReport
     */
    public static MicroStrategyReport appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyReport) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyReport
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyReport
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
     * Remove an Atlan tag from a MicroStrategyReport.
     *
     * @param qualifiedName of the MicroStrategyReport
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyReport
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
