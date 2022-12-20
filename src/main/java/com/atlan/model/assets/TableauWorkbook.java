/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
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
public class TableauWorkbook extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorkbook";

    /** Fixed typeName for TableauWorkbooks. */
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
    String topLevelProjectName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauWorksheet> worksheets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauDatasource> datasources;

    /** TBC */
    @Attribute
    TableauProject project;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauDashboard> dashboards;

    /**
     * Reference to a TableauWorkbook by GUID.
     *
     * @param guid the GUID of the TableauWorkbook to reference
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByGuid(String guid) {
        return TableauWorkbook.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauWorkbook by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauWorkbook to reference
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByQualifiedName(String qualifiedName) {
        return TableauWorkbook.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the minimal request necessary to update the TableauWorkbook, as a builder
     */
    public static TableauWorkbookBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauWorkbook.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauWorkbook, from a potentially
     * more-complete TableauWorkbook object.
     *
     * @return the minimal object necessary to update the TableauWorkbook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauWorkbook are not found in the initial object
     */
    @Override
    public TableauWorkbookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating TableauWorkbook is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauWorkbook by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauWorkbook to retrieve
     * @return the requested full TableauWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    public static TableauWorkbook retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof TableauWorkbook) {
            return (TableauWorkbook) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a TableauWorkbook.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a TableauWorkbook by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauWorkbook to retrieve
     * @return the requested full TableauWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist
     */
    public static TableauWorkbook retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauWorkbook) {
            return (TableauWorkbook) asset;
        } else {
            throw new NotFoundException(
                    "No TableauWorkbook found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauWorkbook to active.
     *
     * @param qualifiedName for the TableauWorkbook
     * @return true if the TableauWorkbook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauWorkbook)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauWorkbook) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauWorkbook)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorkbook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauWorkbook) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauWorkbook)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauWorkbook) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauWorkbook)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauWorkbook
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauWorkbook
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauWorkbook.
     *
     * @param qualifiedName for the TableauWorkbook
     * @param name human-readable name of the TableauWorkbook
     * @param terms the list of terms to replace on the TableauWorkbook, or null to remove all terms from the TableauWorkbook
     * @return the TableauWorkbook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorkbook) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauWorkbook, without replacing existing terms linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauWorkbook
     * @param terms the list of terms to append to the TableauWorkbook
     * @return the TableauWorkbook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauWorkbook) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorkbook, without replacing all existing terms linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauWorkbook
     * @param terms the list of terms to remove from the TableauWorkbook, which must be referenced by GUID
     * @return the TableauWorkbook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauWorkbook) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
