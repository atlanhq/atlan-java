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
 * Instance of a Power BI report in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PowerBIReport extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIReport";

    /** Fixed typeName for PowerBIReports. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String datasetQualifiedName;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    Long pageCount;

    /** TBC */
    @Attribute
    PowerBIWorkspace workspace;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBITile> tiles;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIPage> pages;

    /** TBC */
    @Attribute
    PowerBIDataset dataset;

    /**
     * Reference to a PowerBIReport by GUID.
     *
     * @param guid the GUID of the PowerBIReport to reference
     * @return reference to a PowerBIReport that can be used for defining a relationship to a PowerBIReport
     */
    public static PowerBIReport refByGuid(String guid) {
        return PowerBIReport.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIReport to reference
     * @return reference to a PowerBIReport that can be used for defining a relationship to a PowerBIReport
     */
    public static PowerBIReport refByQualifiedName(String qualifiedName) {
        return PowerBIReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PowerBIReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIReport to retrieve
     * @return the requested full PowerBIReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIReport does not exist or the provided GUID is not a PowerBIReport
     */
    public static PowerBIReport retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBIReport) {
            return (PowerBIReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBIReport");
        }
    }

    /**
     * Retrieves a PowerBIReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIReport to retrieve
     * @return the requested full PowerBIReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIReport does not exist
     */
    public static PowerBIReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBIReport) {
            return (PowerBIReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBIReport");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIReport to active.
     *
     * @param qualifiedName for the PowerBIReport
     * @return true if the PowerBIReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the minimal request necessary to update the PowerBIReport, as a builder
     */
    public static PowerBIReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIReport, from a potentially
     * more-complete PowerBIReport object.
     *
     * @return the minimal object necessary to update the PowerBIReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBIReport are not found in the initial object
     */
    @Override
    public PowerBIReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBIReport", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PowerBIReport) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIReport) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PowerBIReport.
     *
     * @param qualifiedName for the PowerBIReport
     * @param name human-readable name of the PowerBIReport
     * @param terms the list of terms to replace on the PowerBIReport, or null to remove all terms from the PowerBIReport
     * @return the PowerBIReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIReport) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIReport, without replacing existing terms linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIReport
     * @param terms the list of terms to append to the PowerBIReport
     * @return the PowerBIReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIReport) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIReport, without replacing all existing terms linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIReport
     * @param terms the list of terms to remove from the PowerBIReport, which must be referenced by GUID
     * @return the PowerBIReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIReport) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PowerBIReport, without replacing existing Atlan tags linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PowerBIReport
     */
    public static PowerBIReport appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PowerBIReport) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIReport, without replacing existing Atlan tags linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PowerBIReport
     */
    public static PowerBIReport appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PowerBIReport) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIReport
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIReport
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
     * Remove an Atlan tag from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PowerBIReport
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
