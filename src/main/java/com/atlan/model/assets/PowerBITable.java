/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.PowerBIEndorsementType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Power BI table in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PowerBITable extends Asset implements IPowerBITable, IPowerBI, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBITable";

    /** Fixed typeName for PowerBITables. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String datasetQualifiedName;

    /** TBC */
    @Attribute
    PowerBIEndorsementType powerBIEndorsement;

    /** TBC */
    @Attribute
    String powerBIFormatString;

    /** TBC */
    @Attribute
    Boolean powerBIIsHidden;

    /** TBC */
    @Attribute
    Long powerBITableColumnCount;

    /** TBC */
    @Attribute
    Long powerBITableMeasureCount;

    /** TBC */
    @Attribute
    String powerBITableQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> powerBITableSourceExpressions;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IPowerBIColumn> columns;

    /** TBC */
    @Attribute
    IPowerBIDataset dataset;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IPowerBIMeasure> measures;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a PowerBITable by GUID.
     *
     * @param guid the GUID of the PowerBITable to reference
     * @return reference to a PowerBITable that can be used for defining a relationship to a PowerBITable
     */
    public static PowerBITable refByGuid(String guid) {
        return PowerBITable.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBITable by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBITable to reference
     * @return reference to a PowerBITable that can be used for defining a relationship to a PowerBITable
     */
    public static PowerBITable refByQualifiedName(String qualifiedName) {
        return PowerBITable.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PowerBITable by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBITable to retrieve
     * @return the requested full PowerBITable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITable does not exist or the provided GUID is not a PowerBITable
     */
    public static PowerBITable retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBITable) {
            return (PowerBITable) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBITable");
        }
    }

    /**
     * Retrieves a PowerBITable by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBITable to retrieve
     * @return the requested full PowerBITable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITable does not exist
     */
    public static PowerBITable retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBITable) {
            return (PowerBITable) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBITable");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBITable to active.
     *
     * @param qualifiedName for the PowerBITable
     * @return true if the PowerBITable is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the minimal request necessary to update the PowerBITable, as a builder
     */
    public static PowerBITableBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBITable.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBITable, from a potentially
     * more-complete PowerBITable object.
     *
     * @return the minimal object necessary to update the PowerBITable, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBITable are not found in the initial object
     */
    @Override
    public PowerBITableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBITable", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBITable, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PowerBITable) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBITable) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PowerBITable.
     *
     * @param qualifiedName for the PowerBITable
     * @param name human-readable name of the PowerBITable
     * @param terms the list of terms to replace on the PowerBITable, or null to remove all terms from the PowerBITable
     * @return the PowerBITable that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PowerBITable) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBITable, without replacing existing terms linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBITable
     * @param terms the list of terms to append to the PowerBITable
     * @return the PowerBITable that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PowerBITable) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBITable, without replacing all existing terms linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBITable
     * @param terms the list of terms to remove from the PowerBITable, which must be referenced by GUID
     * @return the PowerBITable that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PowerBITable) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PowerBITable, without replacing existing Atlan tags linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBITable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PowerBITable
     */
    public static PowerBITable appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (PowerBITable) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBITable, without replacing existing Atlan tags linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBITable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PowerBITable
     */
    public static PowerBITable appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PowerBITable) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBITable
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBITable
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
     * Remove an Atlan tag from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PowerBITable
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
