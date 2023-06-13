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
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Snowflake Stream in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SnowflakeStream extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeStream";

    /** Fixed typeName for SnowflakeStreams. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String snowflakeStreamType;

    /** TBC */
    @Attribute
    String snowflakeStreamSourceType;

    /** TBC */
    @Attribute
    String snowflakeStreamMode;

    /** TBC */
    @Attribute
    Boolean snowflakeStreamIsStale;

    /** TBC */
    @Attribute
    Long snowflakeStreamStaleAfter;

    /** TBC */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /**
     * Reference to a SnowflakeStream by GUID.
     *
     * @param guid the GUID of the SnowflakeStream to reference
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByGuid(String guid) {
        return SnowflakeStream.builder().guid(guid).build();
    }

    /**
     * Reference to a SnowflakeStream by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeStream to reference
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByQualifiedName(String qualifiedName) {
        return SnowflakeStream.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SnowflakeStream by its GUID, complete with all of its relationships.
     *
     * @param guid of the SnowflakeStream to retrieve
     * @return the requested full SnowflakeStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    public static SnowflakeStream retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SnowflakeStream) {
            return (SnowflakeStream) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SnowflakeStream");
        }
    }

    /**
     * Retrieves a SnowflakeStream by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SnowflakeStream to retrieve
     * @return the requested full SnowflakeStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist
     */
    public static SnowflakeStream retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SnowflakeStream) {
            return (SnowflakeStream) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SnowflakeStream");
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeStream to active.
     *
     * @param qualifiedName for the SnowflakeStream
     * @return true if the SnowflakeStream is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the minimal request necessary to update the SnowflakeStream, as a builder
     */
    public static SnowflakeStreamBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeStream.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeStream, from a potentially
     * more-complete SnowflakeStream object.
     *
     * @return the minimal object necessary to update the SnowflakeStream, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakeStream are not found in the initial object
     */
    @Override
    public SnowflakeStreamBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SnowflakeStream", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeStream, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeStream) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SnowflakeStream) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeStream.
     *
     * @param qualifiedName for the SnowflakeStream
     * @param name human-readable name of the SnowflakeStream
     * @param terms the list of terms to replace on the SnowflakeStream, or null to remove all terms from the SnowflakeStream
     * @return the SnowflakeStream that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeStream) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeStream, without replacing existing terms linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SnowflakeStream
     * @param terms the list of terms to append to the SnowflakeStream
     * @return the SnowflakeStream that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SnowflakeStream) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeStream, without replacing all existing terms linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SnowflakeStream
     * @param terms the list of terms to remove from the SnowflakeStream, which must be referenced by GUID
     * @return the SnowflakeStream that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SnowflakeStream) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeStream, without replacing existing Atlan tags linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeStream
     */
    public static SnowflakeStream appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SnowflakeStream) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeStream, without replacing existing Atlan tags linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeStream
     */
    public static SnowflakeStream appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeStream) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SnowflakeStream
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SnowflakeStream
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
     * Remove an Atlan tag from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeStream
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
