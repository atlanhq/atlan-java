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
 * Instance of a Power BI workspace in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PowerBIWorkspace extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIWorkspace";

    /** Fixed typeName for PowerBIWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    Long reportCount;

    /** TBC */
    @Attribute
    Long dashboardCount;

    /** TBC */
    @Attribute
    Long datasetCount;

    /** TBC */
    @Attribute
    Long dataflowCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIReport> reports;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataset> datasets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDashboard> dashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataflow> dataflows;

    /**
     * Reference to a PowerBIWorkspace by GUID.
     *
     * @param guid the GUID of the PowerBIWorkspace to reference
     * @return reference to a PowerBIWorkspace that can be used for defining a relationship to a PowerBIWorkspace
     */
    public static PowerBIWorkspace refByGuid(String guid) {
        return PowerBIWorkspace.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIWorkspace by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIWorkspace to reference
     * @return reference to a PowerBIWorkspace that can be used for defining a relationship to a PowerBIWorkspace
     */
    public static PowerBIWorkspace refByQualifiedName(String qualifiedName) {
        return PowerBIWorkspace.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PowerBIWorkspace by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIWorkspace to retrieve
     * @return the requested full PowerBIWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIWorkspace does not exist or the provided GUID is not a PowerBIWorkspace
     */
    public static PowerBIWorkspace retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBIWorkspace) {
            return (PowerBIWorkspace) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBIWorkspace");
        }
    }

    /**
     * Retrieves a PowerBIWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIWorkspace to retrieve
     * @return the requested full PowerBIWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIWorkspace does not exist
     */
    public static PowerBIWorkspace retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBIWorkspace) {
            return (PowerBIWorkspace) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBIWorkspace");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIWorkspace to active.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @return true if the PowerBIWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the minimal request necessary to update the PowerBIWorkspace, as a builder
     */
    public static PowerBIWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIWorkspace.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIWorkspace, from a potentially
     * more-complete PowerBIWorkspace object.
     *
     * @return the minimal object necessary to update the PowerBIWorkspace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBIWorkspace are not found in the initial object
     */
    @Override
    public PowerBIWorkspaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBIWorkspace", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIWorkspace) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIWorkspace) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PowerBIWorkspace.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param name human-readable name of the PowerBIWorkspace
     * @param terms the list of terms to replace on the PowerBIWorkspace, or null to remove all terms from the PowerBIWorkspace
     * @return the PowerBIWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIWorkspace) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIWorkspace, without replacing existing terms linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param terms the list of terms to append to the PowerBIWorkspace
     * @return the PowerBIWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIWorkspace) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIWorkspace, without replacing all existing terms linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param terms the list of terms to remove from the PowerBIWorkspace, which must be referenced by GUID
     * @return the PowerBIWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PowerBIWorkspace, without replacing existing Atlan tags linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PowerBIWorkspace
     */
    public static PowerBIWorkspace appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PowerBIWorkspace) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIWorkspace, without replacing existing Atlan tags linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PowerBIWorkspace
     */
    public static PowerBIWorkspace appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PowerBIWorkspace) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIWorkspace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIWorkspace
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
     * Remove an Atlan tag from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PowerBIWorkspace
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
