/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Looker dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class LookerDashboard extends Asset implements ILookerDashboard, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerDashboard";

    /** Fixed typeName for LookerDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    ILookerFolder folder;

    /** TBC */
    @Attribute
    String folderName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILookerLook> looks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    Long sourceLastAccessedAt;

    /** TBC */
    @Attribute
    Long sourceLastViewedAt;

    /** TBC */
    @Attribute
    Integer sourceMetadataId;

    /** TBC */
    @Attribute
    Integer sourceUserId;

    /** TBC */
    @Attribute
    Integer sourceViewCount;

    /** TBC */
    @Attribute
    Integer sourcelastUpdaterId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILookerTile> tiles;

    /**
     * Start an asset filter that will return all LookerDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerDashboard assets will be included.
     *
     * @return an asset filter that includes all LookerDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all LookerDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all LookerDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all LookerDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) LookerDashboards will be included
     * @return an asset filter that includes all LookerDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all LookerDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LookerDashboards will be included
     * @return an asset filter that includes all LookerDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a LookerDashboard by GUID.
     *
     * @param guid the GUID of the LookerDashboard to reference
     * @return reference to a LookerDashboard that can be used for defining a relationship to a LookerDashboard
     */
    public static LookerDashboard refByGuid(String guid) {
        return LookerDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerDashboard to reference
     * @return reference to a LookerDashboard that can be used for defining a relationship to a LookerDashboard
     */
    public static LookerDashboard refByQualifiedName(String qualifiedName) {
        return LookerDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerDashboard to retrieve
     * @return the requested full LookerDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerDashboard does not exist or the provided GUID is not a LookerDashboard
     */
    public static LookerDashboard retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a LookerDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the LookerDashboard to retrieve
     * @return the requested full LookerDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerDashboard does not exist or the provided GUID is not a LookerDashboard
     */
    public static LookerDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerDashboard) {
            return (LookerDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerDashboard");
        }
    }

    /**
     * Retrieves a LookerDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerDashboard to retrieve
     * @return the requested full LookerDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerDashboard does not exist
     */
    public static LookerDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a LookerDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the LookerDashboard to retrieve
     * @return the requested full LookerDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerDashboard does not exist
     */
    public static LookerDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof LookerDashboard) {
            return (LookerDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerDashboard to active.
     *
     * @param qualifiedName for the LookerDashboard
     * @return true if the LookerDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) LookerDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LookerDashboard
     * @return true if the LookerDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the minimal request necessary to update the LookerDashboard, as a builder
     */
    public static LookerDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerDashboard, from a potentially
     * more-complete LookerDashboard object.
     *
     * @return the minimal object necessary to update the LookerDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerDashboard are not found in the initial object
     */
    @Override
    public LookerDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerDashboard's owners
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerDashboard's certificate
     * @param qualifiedName of the LookerDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerDashboard)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerDashboard's certificate
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerDashboard's announcement
     * @param qualifiedName of the LookerDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LookerDashboard)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a LookerDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the LookerDashboard's announcement
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerDashboard.
     *
     * @param qualifiedName for the LookerDashboard
     * @param name human-readable name of the LookerDashboard
     * @param terms the list of terms to replace on the LookerDashboard, or null to remove all terms from the LookerDashboard
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LookerDashboard's assigned terms
     * @param qualifiedName for the LookerDashboard
     * @param name human-readable name of the LookerDashboard
     * @param terms the list of terms to replace on the LookerDashboard, or null to remove all terms from the LookerDashboard
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerDashboard, without replacing existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to append to the LookerDashboard
     * @return the LookerDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the LookerDashboard, without replacing existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LookerDashboard
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to append to the LookerDashboard
     * @return the LookerDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerDashboard, without replacing all existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to remove from the LookerDashboard, which must be referenced by GUID
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerDashboard, without replacing all existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LookerDashboard
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to remove from the LookerDashboard, which must be referenced by GUID
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerDashboard, without replacing existing Atlan tags linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerDashboard
     */
    public static LookerDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerDashboard, without replacing existing Atlan tags linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerDashboard
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerDashboard
     */
    public static LookerDashboard appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerDashboard, without replacing existing Atlan tags linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerDashboard
     */
    public static LookerDashboard appendAtlanTags(
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
     * Add Atlan tags to a LookerDashboard, without replacing existing Atlan tags linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerDashboard
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerDashboard
     */
    public static LookerDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the LookerDashboard
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerDashboard
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
     * Add Atlan tags to a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the LookerDashboard
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerDashboard
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
     * Remove an Atlan tag from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a LookerDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LookerDashboard
     * @param qualifiedName of the LookerDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
