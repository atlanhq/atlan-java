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
 * Instance of a Monte Carlo Incident in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MCIncident extends MonteCarlo {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MCIncident";

    /** Fixed typeName for MCIncidents. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier for the incident. */
    @Attribute
    String mcIncidentId;

    /** Type of incident. */
    @Attribute
    String mcIncidentType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> mcIncidentSubTypes;

    /** Severity of the incident. */
    @Attribute
    String mcIncidentSeverity;

    /** Status of the incident, for example whether it is being investigated or is already fixed. */
    @Attribute
    String mcIncidentState;

    /** Name of the warehouse in which the incident occurred. */
    @Attribute
    String mcIncidentWarehouse;

    /** Assets impacted by the incident. */
    @Attribute
    @Singular
    SortedSet<Asset> mcIncidentAssets;

    /** Monitor through which this incident occurred. */
    @Attribute
    MCMonitor mcMonitor;

    /**
     * Reference to a MCIncident by GUID.
     *
     * @param guid the GUID of the MCIncident to reference
     * @return reference to a MCIncident that can be used for defining a relationship to a MCIncident
     */
    public static MCIncident refByGuid(String guid) {
        return MCIncident.builder().guid(guid).build();
    }

    /**
     * Reference to a MCIncident by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MCIncident to reference
     * @return reference to a MCIncident that can be used for defining a relationship to a MCIncident
     */
    public static MCIncident refByQualifiedName(String qualifiedName) {
        return MCIncident.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MCIncident by its GUID, complete with all of its relationships.
     *
     * @param guid of the MCIncident to retrieve
     * @return the requested full MCIncident, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCIncident does not exist or the provided GUID is not a MCIncident
     */
    public static MCIncident retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MCIncident) {
            return (MCIncident) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MCIncident");
        }
    }

    /**
     * Retrieves a MCIncident by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MCIncident to retrieve
     * @return the requested full MCIncident, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCIncident does not exist
     */
    public static MCIncident retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MCIncident) {
            return (MCIncident) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MCIncident");
        }
    }

    /**
     * Restore the archived (soft-deleted) MCIncident to active.
     *
     * @param qualifiedName for the MCIncident
     * @return true if the MCIncident is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the minimal request necessary to update the MCIncident, as a builder
     */
    public static MCIncidentBuilder<?, ?> updater(String qualifiedName, String name) {
        return MCIncident.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MCIncident, from a potentially
     * more-complete MCIncident object.
     *
     * @return the minimal object necessary to update the MCIncident, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MCIncident are not found in the initial object
     */
    @Override
    public MCIncidentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MCIncident", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the updated MCIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MCIncident) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the updated MCIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MCIncident) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the updated MCIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MCIncident) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MCIncident, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MCIncident) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the updated MCIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MCIncident) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MCIncident) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param name of the MCIncident
     * @return the updated MCIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MCIncident) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MCIncident.
     *
     * @param qualifiedName for the MCIncident
     * @param name human-readable name of the MCIncident
     * @param terms the list of terms to replace on the MCIncident, or null to remove all terms from the MCIncident
     * @return the MCIncident that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MCIncident replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MCIncident) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MCIncident, without replacing existing terms linked to the MCIncident.
     * Note: this operation must make two API calls — one to retrieve the MCIncident's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MCIncident
     * @param terms the list of terms to append to the MCIncident
     * @return the MCIncident that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MCIncident appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MCIncident) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MCIncident, without replacing all existing terms linked to the MCIncident.
     * Note: this operation must make two API calls — one to retrieve the MCIncident's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MCIncident
     * @param terms the list of terms to remove from the MCIncident, which must be referenced by GUID
     * @return the MCIncident that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MCIncident removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MCIncident) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MCIncident, without replacing existing Atlan tags linked to the MCIncident.
     * Note: this operation must make two API calls — one to retrieve the MCIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MCIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MCIncident
     */
    public static MCIncident appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MCIncident) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MCIncident, without replacing existing Atlan tags linked to the MCIncident.
     * Note: this operation must make two API calls — one to retrieve the MCIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MCIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MCIncident
     */
    public static MCIncident appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MCIncident) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MCIncident
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MCIncident
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
     * Remove an Atlan tag from a MCIncident.
     *
     * @param qualifiedName of the MCIncident
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MCIncident
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
