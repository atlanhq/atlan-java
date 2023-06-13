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
 * Instance of a Looker dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class LookerDashboard extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerDashboard";

    /** Fixed typeName for LookerDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String folderName;

    /** TBC */
    @Attribute
    Integer sourceUserId;

    /** TBC */
    @Attribute
    Integer sourceViewCount;

    /** TBC */
    @Attribute
    Integer sourceMetadataId;

    /** TBC */
    @Attribute
    Integer sourcelastUpdaterId;

    /** TBC */
    @Attribute
    Long sourceLastAccessedAt;

    /** TBC */
    @Attribute
    Long sourceLastViewedAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerTile> tiles;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerLook> looks;

    /** TBC */
    @Attribute
    LookerFolder folder;

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
        Asset asset = Asset.retrieveFull(guid);
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (LookerDashboard) Asset.removeDescription(updater(qualifiedName, name));
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
        return (LookerDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (LookerDashboard) Asset.removeOwners(updater(qualifiedName, name));
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
        return (LookerDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (LookerDashboard) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (LookerDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (LookerDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static LookerDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static LookerDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static LookerDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
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
        return (LookerDashboard) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        return (LookerDashboard) Asset.appendAtlanTags(
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
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        Asset.addAtlanTags(
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
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
