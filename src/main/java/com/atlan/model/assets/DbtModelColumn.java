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
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DbtModelColumn extends Dbt {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtModelColumn";

    /** Fixed typeName for DbtModelColumns. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtModelQualifiedName;

    /** TBC */
    @Attribute
    String dbtModelColumnDataType;

    /** TBC */
    @Attribute
    Integer dbtModelColumnOrder;

    /** TBC */
    @Attribute
    Column sqlColumn;

    /** TBC */
    @Attribute
    DbtModel dbtModel;

    /**
     * Reference to a DbtModelColumn by GUID.
     *
     * @param guid the GUID of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByGuid(String guid) {
        return DbtModelColumn.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtModelColumn by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByQualifiedName(String qualifiedName) {
        return DbtModelColumn.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the minimal request necessary to update the DbtModelColumn, as a builder
     */
    public static DbtModelColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtModelColumn.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtModelColumn, from a potentially
     * more-complete DbtModelColumn object.
     *
     * @return the minimal object necessary to update the DbtModelColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtModelColumn are not found in the initial object
     */
    @Override
    public DbtModelColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating DbtModelColumn is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a DbtModelColumn by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    public static DbtModelColumn retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof DbtModelColumn) {
            return (DbtModelColumn) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a DbtModelColumn.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a DbtModelColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist
     */
    public static DbtModelColumn retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof DbtModelColumn) {
            return (DbtModelColumn) entity;
        } else {
            throw new NotFoundException(
                    "No DbtModelColumn found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtModelColumn to active.
     *
     * @param qualifiedName for the DbtModelColumn
     * @return true if the DbtModelColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtModelColumn)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtModelColumn) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtModelColumn)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModelColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (DbtModelColumn) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtModelColumn)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtModelColumn) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtModelColumn)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the DbtModelColumn
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the DbtModelColumn
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the DbtModelColumn.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param name human-readable name of the DbtModelColumn
     * @param terms the list of terms to replace on the DbtModelColumn, or null to remove all terms from the DbtModelColumn
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DbtModelColumn) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtModelColumn, without replacing existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to append to the DbtModelColumn
     * @return the DbtModelColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtModelColumn) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModelColumn, without replacing all existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to remove from the DbtModelColumn, which must be referenced by GUID
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtModelColumn) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
