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
 * Instance of a MicroStrategy cube in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MicroStrategyCube extends MicroStrategy {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyCube";

    /** Fixed typeName for MicroStrategyCubes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether the cube is an OLAP or MTDI cube. */
    @Attribute
    String microStrategyCubeType;

    /** Query used to create the cube. */
    @Attribute
    String microStrategyCubeQuery;

    /** Metrics where the cube is used. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyMetric> microStrategyMetrics;

    /** Project containing the cube. */
    @Attribute
    MicroStrategyProject microStrategyProject;

    /** Attributes used by the cube. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyAttribute> microStrategyAttributes;

    /**
     * Reference to a MicroStrategyCube by GUID.
     *
     * @param guid the GUID of the MicroStrategyCube to reference
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByGuid(String guid) {
        return MicroStrategyCube.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyCube by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyCube to reference
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByQualifiedName(String qualifiedName) {
        return MicroStrategyCube.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyCube by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyCube to retrieve
     * @return the requested full MicroStrategyCube, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyCube does not exist or the provided GUID is not a MicroStrategyCube
     */
    public static MicroStrategyCube retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyCube) {
            return (MicroStrategyCube) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyCube");
        }
    }

    /**
     * Retrieves a MicroStrategyCube by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyCube to retrieve
     * @return the requested full MicroStrategyCube, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyCube does not exist
     */
    public static MicroStrategyCube retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyCube) {
            return (MicroStrategyCube) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyCube");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyCube to active.
     *
     * @param qualifiedName for the MicroStrategyCube
     * @return true if the MicroStrategyCube is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the minimal request necessary to update the MicroStrategyCube, as a builder
     */
    public static MicroStrategyCubeBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyCube.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyCube, from a potentially
     * more-complete MicroStrategyCube object.
     *
     * @return the minimal object necessary to update the MicroStrategyCube, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyCube are not found in the initial object
     */
    @Override
    public MicroStrategyCubeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyCube", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyCube) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyCube) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyCube) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyCube, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyCube) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyCube) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyCube) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyCube) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyCube.
     *
     * @param qualifiedName for the MicroStrategyCube
     * @param name human-readable name of the MicroStrategyCube
     * @param terms the list of terms to replace on the MicroStrategyCube, or null to remove all terms from the MicroStrategyCube
     * @return the MicroStrategyCube that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyCube) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyCube, without replacing existing terms linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyCube
     * @param terms the list of terms to append to the MicroStrategyCube
     * @return the MicroStrategyCube that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyCube) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyCube, without replacing all existing terms linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyCube
     * @param terms the list of terms to remove from the MicroStrategyCube, which must be referenced by GUID
     * @return the MicroStrategyCube that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyCube) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube, without replacing existing Atlan tags linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyCube
     */
    public static MicroStrategyCube appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyCube) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube, without replacing existing Atlan tags linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyCube
     */
    public static MicroStrategyCube appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyCube) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyCube
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyCube
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
     * Remove an Atlan tag from a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyCube
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
