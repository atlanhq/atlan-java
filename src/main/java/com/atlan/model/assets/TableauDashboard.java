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
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TableauDashboard extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauDashboard";

    /** Fixed typeName for TableauDashboards. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    TableauWorkbook workbook;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauWorksheet> worksheets;

    /**
     * Reference to a TableauDashboard by GUID.
     *
     * @param guid the GUID of the TableauDashboard to reference
     * @return reference to a TableauDashboard that can be used for defining a relationship to a TableauDashboard
     */
    public static TableauDashboard refByGuid(String guid) {
        return TableauDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauDashboard to reference
     * @return reference to a TableauDashboard that can be used for defining a relationship to a TableauDashboard
     */
    public static TableauDashboard refByQualifiedName(String qualifiedName) {
        return TableauDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the minimal request necessary to update the TableauDashboard, as a builder
     */
    public static TableauDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauDashboard, from a potentially
     * more-complete TableauDashboard object.
     *
     * @return the minimal object necessary to update the TableauDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauDashboard are not found in the initial object
     */
    @Override
    public TableauDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating TableauDashboard is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist or the provided GUID is not a TableauDashboard
     */
    public static TableauDashboard retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof TableauDashboard) {
            return (TableauDashboard) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a TableauDashboard.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a TableauDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauDashboard to retrieve
     * @return the requested full TableauDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDashboard does not exist
     */
    public static TableauDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof TableauDashboard) {
            return (TableauDashboard) entity;
        } else {
            throw new NotFoundException(
                    "No TableauDashboard found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauDashboard to active.
     *
     * @param qualifiedName for the TableauDashboard
     * @return true if the TableauDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauDashboard)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauDashboard) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauDashboard)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param name of the TableauDashboard
     * @return the updated TableauDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauDashboard.
     *
     * @param qualifiedName of the TableauDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauDashboard.
     *
     * @param qualifiedName for the TableauDashboard
     * @param name human-readable name of the TableauDashboard
     * @param terms the list of terms to replace on the TableauDashboard, or null to remove all terms from the TableauDashboard
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauDashboard, without replacing existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to append to the TableauDashboard
     * @return the TableauDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDashboard, without replacing all existing terms linked to the TableauDashboard.
     * Note: this operation must make two API calls — one to retrieve the TableauDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauDashboard
     * @param terms the list of terms to remove from the TableauDashboard, which must be referenced by GUID
     * @return the TableauDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
