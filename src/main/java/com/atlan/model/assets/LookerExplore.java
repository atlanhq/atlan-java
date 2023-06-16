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
 * Instance of a Looker explore in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class LookerExplore extends Asset implements ILookerExplore, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerExplore";

    /** Fixed typeName for LookerExplores. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILookerField> fields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    ILookerModel model;

    /** TBC */
    @Attribute
    String modelName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    ILookerProject project;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    String sourceConnectionName;

    /** TBC */
    @Attribute
    String sqlTableName;

    /** TBC */
    @Attribute
    String viewName;

    /**
     * Reference to a LookerExplore by GUID.
     *
     * @param guid the GUID of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByGuid(String guid) {
        return LookerExplore.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerExplore by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByQualifiedName(String qualifiedName) {
        return LookerExplore.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerExplore by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerExplore to retrieve
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist or the provided GUID is not a LookerExplore
     */
    public static LookerExplore retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerExplore) {
            return (LookerExplore) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerExplore");
        }
    }

    /**
     * Retrieves a LookerExplore by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerExplore to retrieve
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist
     */
    public static LookerExplore retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerExplore) {
            return (LookerExplore) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerExplore");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerExplore to active.
     *
     * @param qualifiedName for the LookerExplore
     * @return true if the LookerExplore is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the minimal request necessary to update the LookerExplore, as a builder
     */
    public static LookerExploreBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerExplore.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerExplore, from a potentially
     * more-complete LookerExplore object.
     *
     * @return the minimal object necessary to update the LookerExplore, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerExplore are not found in the initial object
     */
    @Override
    public LookerExploreBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerExplore", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerExplore, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerExplore) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerExplore) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerExplore.
     *
     * @param qualifiedName for the LookerExplore
     * @param name human-readable name of the LookerExplore
     * @param terms the list of terms to replace on the LookerExplore, or null to remove all terms from the LookerExplore
     * @return the LookerExplore that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerExplore) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerExplore, without replacing existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to append to the LookerExplore
     * @return the LookerExplore that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerExplore) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerExplore, without replacing all existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to remove from the LookerExplore, which must be referenced by GUID
     * @return the LookerExplore that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerExplore) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerExplore) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerExplore) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerExplore
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerExplore
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
     * Remove an Atlan tag from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerExplore
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
