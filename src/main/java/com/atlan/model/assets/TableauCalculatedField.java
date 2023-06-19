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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Tableau calculated field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauCalculatedField extends Asset
        implements ITableauCalculatedField, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauCalculatedField";

    /** Fixed typeName for TableauCalculatedFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dataCategory;

    /** TBC */
    @Attribute
    ITableauDatasource datasource;

    /** TBC */
    @Attribute
    String datasourceQualifiedName;

    /** TBC */
    @Attribute
    String formula;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String role;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String tableauDataType;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> upstreamFields;

    /** TBC */
    @Attribute
    String workbookQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauWorksheet> worksheets;

    /**
     * Reference to a TableauCalculatedField by GUID.
     *
     * @param guid the GUID of the TableauCalculatedField to reference
     * @return reference to a TableauCalculatedField that can be used for defining a relationship to a TableauCalculatedField
     */
    public static TableauCalculatedField refByGuid(String guid) {
        return TableauCalculatedField.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauCalculatedField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauCalculatedField to reference
     * @return reference to a TableauCalculatedField that can be used for defining a relationship to a TableauCalculatedField
     */
    public static TableauCalculatedField refByQualifiedName(String qualifiedName) {
        return TableauCalculatedField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauCalculatedField by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauCalculatedField to retrieve
     * @return the requested full TableauCalculatedField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauCalculatedField does not exist or the provided GUID is not a TableauCalculatedField
     */
    public static TableauCalculatedField retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauCalculatedField) {
            return (TableauCalculatedField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauCalculatedField");
        }
    }

    /**
     * Retrieves a TableauCalculatedField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauCalculatedField to retrieve
     * @return the requested full TableauCalculatedField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauCalculatedField does not exist
     */
    public static TableauCalculatedField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauCalculatedField) {
            return (TableauCalculatedField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauCalculatedField");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauCalculatedField to active.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @return true if the TableauCalculatedField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the minimal request necessary to update the TableauCalculatedField, as a builder
     */
    public static TableauCalculatedFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauCalculatedField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauCalculatedField, from a potentially
     * more-complete TableauCalculatedField object.
     *
     * @return the minimal object necessary to update the TableauCalculatedField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauCalculatedField are not found in the initial object
     */
    @Override
    public TableauCalculatedFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauCalculatedField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (TableauCalculatedField) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauCalculatedField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (TableauCalculatedField)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauCalculatedField)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param name of the TableauCalculatedField
     * @return the updated TableauCalculatedField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauCalculatedField) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauCalculatedField.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param name human-readable name of the TableauCalculatedField
     * @param terms the list of terms to replace on the TableauCalculatedField, or null to remove all terms from the TableauCalculatedField
     * @return the TableauCalculatedField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauCalculatedField, without replacing existing terms linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param terms the list of terms to append to the TableauCalculatedField
     * @return the TableauCalculatedField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauCalculatedField, without replacing all existing terms linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauCalculatedField
     * @param terms the list of terms to remove from the TableauCalculatedField, which must be referenced by GUID
     * @return the TableauCalculatedField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauCalculatedField removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauCalculatedField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauCalculatedField, without replacing existing Atlan tags linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauCalculatedField
     */
    public static TableauCalculatedField appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauCalculatedField) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauCalculatedField, without replacing existing Atlan tags linked to the TableauCalculatedField.
     * Note: this operation must make two API calls — one to retrieve the TableauCalculatedField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauCalculatedField
     */
    public static TableauCalculatedField appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauCalculatedField) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauCalculatedField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauCalculatedField
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
     * Remove an Atlan tag from a TableauCalculatedField.
     *
     * @param qualifiedName of the TableauCalculatedField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauCalculatedField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
