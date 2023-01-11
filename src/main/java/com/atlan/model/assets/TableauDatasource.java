/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Tableau datasource in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TableauDatasource extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauDatasource";

    /** Fixed typeName for TableauDatasources. */
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
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    Boolean isPublished;

    /** TBC */
    @Attribute
    Boolean hasExtracts;

    /** TBC */
    @Attribute
    Boolean isCertified;

    /** TBC */
    @Attribute
    @Singular("putCertifier")
    Map<String, String> certifier;

    /** TBC */
    @Attribute
    String certificationNote;

    /** TBC */
    @Attribute
    String certifierDisplayName;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamTables;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamDatasources;

    /** TBC */
    @Attribute
    TableauWorkbook workbook;

    /** TBC */
    @Attribute
    TableauProject project;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauCalculatedField> fields;

    /** TBC
     * @Attribute
     * @Singular
     * SortedSet<TableauDatasourceField> fields; */

    /**
     * Reference to a TableauDatasource by GUID.
     *
     * @param guid the GUID of the TableauDatasource to reference
     * @return reference to a TableauDatasource that can be used for defining a relationship to a TableauDatasource
     */
    public static TableauDatasource refByGuid(String guid) {
        return TableauDatasource.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauDatasource by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauDatasource to reference
     * @return reference to a TableauDatasource that can be used for defining a relationship to a TableauDatasource
     */
    public static TableauDatasource refByQualifiedName(String qualifiedName) {
        return TableauDatasource.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the minimal request necessary to update the TableauDatasource, as a builder
     */
    public static TableauDatasourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauDatasource.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauDatasource, from a potentially
     * more-complete TableauDatasource object.
     *
     * @return the minimal object necessary to update the TableauDatasource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauDatasource are not found in the initial object
     */
    @Override
    public TableauDatasourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauDatasource", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauDatasource by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauDatasource to retrieve
     * @return the requested full TableauDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasource does not exist or the provided GUID is not a TableauDatasource
     */
    public static TableauDatasource retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauDatasource) {
            return (TableauDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauDatasource");
        }
    }

    /**
     * Retrieves a TableauDatasource by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauDatasource to retrieve
     * @return the requested full TableauDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasource does not exist
     */
    public static TableauDatasource retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauDatasource) {
            return (TableauDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauDatasource");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauDatasource to active.
     *
     * @param qualifiedName for the TableauDatasource
     * @return true if the TableauDatasource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the updated TableauDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasource) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the updated TableauDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasource) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the updated TableauDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasource) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDatasource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauDatasource) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the updated TableauDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasource) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauDatasource) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param name of the TableauDatasource
     * @return the updated TableauDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasource) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauDatasource
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauDatasource.
     *
     * @param qualifiedName of the TableauDatasource
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauDatasource
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauDatasource.
     *
     * @param qualifiedName for the TableauDatasource
     * @param name human-readable name of the TableauDatasource
     * @param terms the list of terms to replace on the TableauDatasource, or null to remove all terms from the TableauDatasource
     * @return the TableauDatasource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauDatasource) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauDatasource, without replacing existing terms linked to the TableauDatasource.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasource's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauDatasource
     * @param terms the list of terms to append to the TableauDatasource
     * @return the TableauDatasource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauDatasource) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDatasource, without replacing all existing terms linked to the TableauDatasource.
     * Note: this operation must make two API calls — one to retrieve the TableauDatasource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauDatasource
     * @param terms the list of terms to remove from the TableauDatasource, which must be referenced by GUID
     * @return the TableauDatasource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasource removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauDatasource) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
