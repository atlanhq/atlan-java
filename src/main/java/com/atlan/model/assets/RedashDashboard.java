/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
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
 * Instance of a Redash dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class RedashDashboard extends Asset implements IRedashDashboard, IRedash, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "RedashDashboard";

    /** Fixed typeName for RedashDashboards. */
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

    /** Number of widgets in the Redash dashboard. */
    @Attribute
    Long redashDashboardWidgetCount;

    /** TBC */
    @Attribute
    Boolean redashIsPublished;

    /**
     * Reference to a RedashDashboard by GUID.
     *
     * @param guid the GUID of the RedashDashboard to reference
     * @return reference to a RedashDashboard that can be used for defining a relationship to a RedashDashboard
     */
    public static RedashDashboard refByGuid(String guid) {
        return RedashDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a RedashDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the RedashDashboard to reference
     * @return reference to a RedashDashboard that can be used for defining a relationship to a RedashDashboard
     */
    public static RedashDashboard refByQualifiedName(String qualifiedName) {
        return RedashDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a RedashDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist or the provided GUID is not a RedashDashboard
     */
    public static RedashDashboard retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a RedashDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist or the provided GUID is not a RedashDashboard
     */
    public static RedashDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof RedashDashboard) {
            return (RedashDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "RedashDashboard");
        }
    }

    /**
     * Retrieves a RedashDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist
     */
    public static RedashDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a RedashDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist
     */
    public static RedashDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof RedashDashboard) {
            return (RedashDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "RedashDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) RedashDashboard to active.
     *
     * @param qualifiedName for the RedashDashboard
     * @return true if the RedashDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) RedashDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the RedashDashboard
     * @return true if the RedashDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the minimal request necessary to update the RedashDashboard, as a builder
     */
    public static RedashDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return RedashDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a RedashDashboard, from a potentially
     * more-complete RedashDashboard object.
     *
     * @return the minimal object necessary to update the RedashDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for RedashDashboard are not found in the initial object
     */
    @Override
    public RedashDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "RedashDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the RedashDashboard's owners
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the RedashDashboard's certificate
     * @param qualifiedName of the RedashDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (RedashDashboard)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the RedashDashboard's certificate
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the RedashDashboard's announcement
     * @param qualifiedName of the RedashDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (RedashDashboard)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a RedashDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the RedashDashboard's announcement
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (RedashDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the RedashDashboard.
     *
     * @param qualifiedName for the RedashDashboard
     * @param name human-readable name of the RedashDashboard
     * @param terms the list of terms to replace on the RedashDashboard, or null to remove all terms from the RedashDashboard
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the RedashDashboard's assigned terms
     * @param qualifiedName for the RedashDashboard
     * @param name human-readable name of the RedashDashboard
     * @param terms the list of terms to replace on the RedashDashboard, or null to remove all terms from the RedashDashboard
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (RedashDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the RedashDashboard, without replacing existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to append to the RedashDashboard
     * @return the RedashDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the RedashDashboard, without replacing existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the RedashDashboard
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to append to the RedashDashboard
     * @return the RedashDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (RedashDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashDashboard, without replacing all existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to remove from the RedashDashboard, which must be referenced by GUID
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashDashboard, without replacing all existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the RedashDashboard
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to remove from the RedashDashboard, which must be referenced by GUID
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (RedashDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a RedashDashboard, without replacing existing Atlan tags linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated RedashDashboard
     */
    public static RedashDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashDashboard, without replacing existing Atlan tags linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the RedashDashboard
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated RedashDashboard
     */
    public static RedashDashboard appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (RedashDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashDashboard, without replacing existing Atlan tags linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated RedashDashboard
     */
    public static RedashDashboard appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a RedashDashboard, without replacing existing Atlan tags linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the RedashDashboard
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated RedashDashboard
     */
    public static RedashDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (RedashDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the RedashDashboard
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashDashboard
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the RedashDashboard
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the RedashDashboard
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the RedashDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a RedashDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a RedashDashboard
     * @param qualifiedName of the RedashDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the RedashDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
