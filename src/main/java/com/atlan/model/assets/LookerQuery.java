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
 * Instance of a Looker query in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class LookerQuery extends Asset implements ILookerQuery, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerQuery";

    /** Fixed typeName for LookerQuerys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> fields;

    /** TBC */
    @Attribute
    String sourceDefinition;

    /** TBC */
    @Attribute
    String sourceDefinitionDatabase;

    /** TBC */
    @Attribute
    String sourceDefinitionSchema;

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
    ILookerModel model;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILookerTile> tiles;

    /**
     * Reference to a LookerQuery by GUID.
     *
     * @param guid the GUID of the LookerQuery to reference
     * @return reference to a LookerQuery that can be used for defining a relationship to a LookerQuery
     */
    public static LookerQuery refByGuid(String guid) {
        return LookerQuery.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerQuery by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerQuery to reference
     * @return reference to a LookerQuery that can be used for defining a relationship to a LookerQuery
     */
    public static LookerQuery refByQualifiedName(String qualifiedName) {
        return LookerQuery.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerQuery by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerQuery to retrieve
     * @return the requested full LookerQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerQuery does not exist or the provided GUID is not a LookerQuery
     */
    public static LookerQuery retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerQuery) {
            return (LookerQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerQuery");
        }
    }

    /**
     * Retrieves a LookerQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerQuery to retrieve
     * @return the requested full LookerQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerQuery does not exist
     */
    public static LookerQuery retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerQuery) {
            return (LookerQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerQuery");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerQuery to active.
     *
     * @param qualifiedName for the LookerQuery
     * @return true if the LookerQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the minimal request necessary to update the LookerQuery, as a builder
     */
    public static LookerQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerQuery.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerQuery, from a potentially
     * more-complete LookerQuery object.
     *
     * @return the minimal object necessary to update the LookerQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerQuery are not found in the initial object
     */
    @Override
    public LookerQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerQuery", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerQuery) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerQuery) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerQuery.
     *
     * @param qualifiedName for the LookerQuery
     * @param name human-readable name of the LookerQuery
     * @param terms the list of terms to replace on the LookerQuery, or null to remove all terms from the LookerQuery
     * @return the LookerQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerQuery) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerQuery, without replacing existing terms linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerQuery
     * @param terms the list of terms to append to the LookerQuery
     * @return the LookerQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerQuery) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerQuery, without replacing all existing terms linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerQuery
     * @param terms the list of terms to remove from the LookerQuery, which must be referenced by GUID
     * @return the LookerQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerQuery) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerQuery, without replacing existing Atlan tags linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerQuery
     */
    public static LookerQuery appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (LookerQuery) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerQuery, without replacing existing Atlan tags linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerQuery
     */
    public static LookerQuery appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerQuery) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerQuery
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerQuery
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
     * Remove an Atlan tag from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerQuery
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
