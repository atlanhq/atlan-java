/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SnowflakeStream extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeStream";

    /** Fixed typeName for SnowflakeStreams. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
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
                    "Required field for updating SnowflakeStream is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SnowflakeStream by its GUID, complete with all of its relationships.
     *
     * @param guid of the SnowflakeStream to retrieve
     * @return the requested full SnowflakeStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    public static SnowflakeStream retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof SnowflakeStream) {
            return (SnowflakeStream) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a SnowflakeStream.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof SnowflakeStream) {
            return (SnowflakeStream) entity;
        } else {
            throw new NotFoundException(
                    "No SnowflakeStream found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
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
     * Remove the system description from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeStream)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
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
        return (SnowflakeStream) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
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
        return (SnowflakeStream)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
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
    public static SnowflakeStream updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
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
        return (SnowflakeStream)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (SnowflakeStream)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SnowflakeStream
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SnowflakeStream
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
}
