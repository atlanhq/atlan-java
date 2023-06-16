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
 * Instance of a Tableau workbook in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauWorkbook extends Asset
        implements ITableauWorkbook, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorkbook";

    /** Fixed typeName for TableauWorkbooks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauDashboard> dashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauDatasource> datasources;

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
    ITableauProject project;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITableauWorksheet> worksheets;

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
     * Retrieves a TableauWorkbook by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauWorkbook to retrieve
     * @return the requested full TableauWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    public static TableauWorkbook retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauWorkbook) {
            return (TableauWorkbook) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauWorkbook");
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
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauWorkbook");
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
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauWorkbook", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
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
        return (TableauWorkbook) Asset.removeDescription(updater(qualifiedName, name));
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
        return (TableauWorkbook) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (TableauWorkbook) Asset.removeOwners(updater(qualifiedName, name));
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
    public static TableauWorkbook updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
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
        return (TableauWorkbook) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (TableauWorkbook) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static TableauWorkbook replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
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
    public static TableauWorkbook appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
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
    public static TableauWorkbook removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauWorkbook) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauWorkbook, without replacing existing Atlan tags linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauWorkbook
     */
    public static TableauWorkbook appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauWorkbook) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorkbook, without replacing existing Atlan tags linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauWorkbook
     */
    public static TableauWorkbook appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauWorkbook) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorkbook
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TableauWorkbook
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
     * Remove an Atlan tag from a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauWorkbook
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
