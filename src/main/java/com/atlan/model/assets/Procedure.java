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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a stored procedure (routine) in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Procedure extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Procedure";

    /** Fixed typeName for Procedures. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Logic of the procedure. */
    @Attribute
    String definition;

    /** Schema in which the procedure is contained. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /**
     * Reference to a Procedure by GUID.
     *
     * @param guid the GUID of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByGuid(String guid) {
        return Procedure.builder().guid(guid).build();
    }

    /**
     * Reference to a Procedure by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByQualifiedName(String qualifiedName) {
        return Procedure.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Procedure by its GUID, complete with all of its relationships.
     *
     * @param guid of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    public static Procedure retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Procedure) {
            return (Procedure) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Procedure");
        }
    }

    /**
     * Retrieves a Procedure by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist
     */
    public static Procedure retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Procedure) {
            return (Procedure) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Procedure");
        }
    }

    /**
     * Restore the archived (soft-deleted) Procedure to active.
     *
     * @param qualifiedName for the Procedure
     * @return true if the Procedure is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the minimal request necessary to update the Procedure, as a builder
     */
    public static ProcedureBuilder<?, ?> updater(String qualifiedName, String name) {
        return Procedure.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Procedure, from a potentially
     * more-complete Procedure object.
     *
     * @return the minimal object necessary to update the Procedure, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Procedure are not found in the initial object
     */
    @Override
    public ProcedureBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Procedure", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Procedure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Procedure) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Procedure) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Procedure.
     *
     * @param qualifiedName for the Procedure
     * @param name human-readable name of the Procedure
     * @param terms the list of terms to replace on the Procedure, or null to remove all terms from the Procedure
     * @return the Procedure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Procedure) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Procedure, without replacing existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to append to the Procedure
     * @return the Procedure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Procedure) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Procedure, without replacing all existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to remove from the Procedure, which must be referenced by GUID
     * @return the Procedure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Procedure) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
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
     * Remove an Atlan tag from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Procedure
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
