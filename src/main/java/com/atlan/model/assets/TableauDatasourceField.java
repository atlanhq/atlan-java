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
 * Instance of a Tableau datasource field in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TableauDatasourceField extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauDatasourceField";

    /** Fixed typeName for TableauDatasourceFields. */
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
    String datasourceQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String fullyQualifiedName;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldDataCategory;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldRole;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldDataType;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamTables;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldFormula;

    /** TBC */
    @Attribute
    String tableauDatasourceFieldBinSize;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamColumns;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamFields;

    /** TBC */
    @Attribute
    String datasourceFieldType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauWorksheet> worksheets;

    /** TBC */
    @Attribute
    TableauDatasource datasource;

    /**
     * Reference to a TableauDatasourceField by GUID.
     *
     * @param guid the GUID of the TableauDatasourceField to reference
     * @return reference to a TableauDatasourceField that can be used for defining a relationship to a TableauDatasourceField
     */
    public static TableauDatasourceField refByGuid(String guid) {
        return TableauDatasourceField.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauDatasourceField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauDatasourceField to reference
     * @return reference to a TableauDatasourceField that can be used for defining a relationship to a TableauDatasourceField
     */
    public static TableauDatasourceField refByQualifiedName(String qualifiedName) {
        return TableauDatasourceField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the minimal request necessary to update the TableauDatasourceField, as a builder
     */
    public static TableauDatasourceFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauDatasourceField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauDatasourceField, from a potentially
     * more-complete TableauDatasourceField object.
     *
     * @return the minimal object necessary to update the TableauDatasourceField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauDatasourceField are not found in the initial object
     */
    @Override
    public TableauDatasourceFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauDatasourceField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauDatasourceField by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist or the provided GUID is not a TableauDatasourceField
     */
    public static TableauDatasourceField retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauDatasourceField) {
            return (TableauDatasourceField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauDatasourceField");
        }
    }

    /**
     * Retrieves a TableauDatasourceField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauDatasourceField to retrieve
     * @return the requested full TableauDatasourceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauDatasourceField does not exist
     */
    public static TableauDatasourceField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauDatasourceField) {
            return (TableauDatasourceField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauDatasourceField");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauDatasourceField to active.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @return true if the TableauDatasourceField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasourceField) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasourceField) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauDatasourceField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauDatasourceField)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasourceField) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauDatasourceField)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param name of the TableauDatasourceField
     * @return the updated TableauDatasourceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauDatasourceField) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauDatasourceField
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauDatasourceField.
     *
     * @param qualifiedName of the TableauDatasourceField
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauDatasourceField
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauDatasourceField.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param name human-readable name of the TableauDatasourceField
     * @param terms the list of terms to replace on the TableauDatasourceField, or null to remove all terms from the TableauDatasourceField
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauDatasourceField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauDatasourceField, without replacing existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls ??? one to retrieve the TableauDatasourceField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to append to the TableauDatasourceField
     * @return the TableauDatasourceField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauDatasourceField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauDatasourceField, without replacing all existing terms linked to the TableauDatasourceField.
     * Note: this operation must make two API calls ??? one to retrieve the TableauDatasourceField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauDatasourceField
     * @param terms the list of terms to remove from the TableauDatasourceField, which must be referenced by GUID
     * @return the TableauDatasourceField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauDatasourceField removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauDatasourceField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
