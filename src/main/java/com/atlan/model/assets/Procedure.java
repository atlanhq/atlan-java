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
public class Procedure extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Procedure";

    /** Fixed typeName for Procedures. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String definition;

    /** TBC */
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
                    "Required field for updating Procedure is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Procedure by its GUID, complete with all of its relationships.
     *
     * @param guid of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    public static Procedure retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Procedure) {
            return (Procedure) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Procedure.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Procedure) {
            return (Procedure) entity;
        } else {
            throw new NotFoundException(
                    "No Procedure found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
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
     * Remove the system description from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Procedure)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
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
        return (Procedure) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
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
        return (Procedure)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
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
    public static Procedure updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
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
        return (Procedure)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (Procedure)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Procedure
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Procedure
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
}
