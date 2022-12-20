/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
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
public class SnowflakePipe extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakePipe";

    /** Fixed typeName for SnowflakePipes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String definition;

    /** TBC */
    @Attribute
    Boolean snowflakePipeIsAutoIngestEnabled;

    /** TBC */
    @Attribute
    String snowflakePipeNotificationChannelName;

    /** TBC */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /**
     * Reference to a SnowflakePipe by GUID.
     *
     * @param guid the GUID of the SnowflakePipe to reference
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByGuid(String guid) {
        return SnowflakePipe.builder().guid(guid).build();
    }

    /**
     * Reference to a SnowflakePipe by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SnowflakePipe to reference
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByQualifiedName(String qualifiedName) {
        return SnowflakePipe.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the minimal request necessary to update the SnowflakePipe, as a builder
     */
    public static SnowflakePipeBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakePipe.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakePipe, from a potentially
     * more-complete SnowflakePipe object.
     *
     * @return the minimal object necessary to update the SnowflakePipe, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakePipe are not found in the initial object
     */
    @Override
    public SnowflakePipeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating SnowflakePipe is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SnowflakePipe by its GUID, complete with all of its relationships.
     *
     * @param guid of the SnowflakePipe to retrieve
     * @return the requested full SnowflakePipe, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakePipe does not exist or the provided GUID is not a SnowflakePipe
     */
    public static SnowflakePipe retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof SnowflakePipe) {
            return (SnowflakePipe) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a SnowflakePipe.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a SnowflakePipe by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SnowflakePipe to retrieve
     * @return the requested full SnowflakePipe, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakePipe does not exist
     */
    public static SnowflakePipe retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SnowflakePipe) {
            return (SnowflakePipe) asset;
        } else {
            throw new NotFoundException(
                    "No SnowflakePipe found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakePipe to active.
     *
     * @param qualifiedName for the SnowflakePipe
     * @return true if the SnowflakePipe is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakePipe)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakePipe) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SnowflakePipe)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakePipe, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SnowflakePipe) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SnowflakePipe)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SnowflakePipe) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SnowflakePipe)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SnowflakePipe
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SnowflakePipe
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SnowflakePipe.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param name human-readable name of the SnowflakePipe
     * @param terms the list of terms to replace on the SnowflakePipe, or null to remove all terms from the SnowflakePipe
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakePipe) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakePipe, without replacing existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to append to the SnowflakePipe
     * @return the SnowflakePipe that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SnowflakePipe) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakePipe, without replacing all existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to remove from the SnowflakePipe, which must be referenced by GUID
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SnowflakePipe) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
