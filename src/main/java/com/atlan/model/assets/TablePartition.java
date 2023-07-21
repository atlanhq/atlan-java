/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a database table partition in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TablePartition extends Asset implements ITablePartition, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TablePartition";

    /** Fixed typeName for TablePartitions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String alias;

    /** TBC */
    @Attribute
    Long columnCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

    /** TBC */
    @Attribute
    String constraint;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
    @Attribute
    String databaseQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** TBC */
    @Attribute
    String externalLocation;

    /** TBC */
    @Attribute
    String externalLocationFormat;

    /** TBC */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isPartitioned;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** TBC */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    ITable parentTable;

    /** TBC */
    @Attribute
    Long partitionCount;

    /** TBC */
    @Attribute
    String partitionList;

    /** TBC */
    @Attribute
    String partitionStrategy;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    Long rowCount;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
    @Attribute
    Long sizeBytes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Start an asset filter that will return all TablePartition assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TablePartition assets will be included.
     *
     * @return an asset filter that includes all TablePartition assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all TablePartition assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) TablePartitions will be included
     * @return an asset filter that includes all TablePartition assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a TablePartition by GUID.
     *
     * @param guid the GUID of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByGuid(String guid) {
        return TablePartition.builder().guid(guid).build();
    }

    /**
     * Reference to a TablePartition by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByQualifiedName(String qualifiedName) {
        return TablePartition.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TablePartition by its GUID, complete with all of its relationships.
     *
     * @param guid of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    public static TablePartition retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a TablePartition by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    public static TablePartition retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TablePartition) {
            return (TablePartition) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TablePartition");
        }
    }

    /**
     * Retrieves a TablePartition by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist
     */
    public static TablePartition retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a TablePartition by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist
     */
    public static TablePartition retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof TablePartition) {
            return (TablePartition) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TablePartition");
        }
    }

    /**
     * Restore the archived (soft-deleted) TablePartition to active.
     *
     * @param qualifiedName for the TablePartition
     * @return true if the TablePartition is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TablePartition to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TablePartition
     * @return true if the TablePartition is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a table partition.
     *
     * @param name of the table partition
     * @param tableQualifiedName unique name of the table in which this table partition exists
     * @return the minimal request necessary to create the table partition, as a builder
     */
    public static TablePartitionBuilder<?, ?> creator(String name, String tableQualifiedName) {
        String[] tokens = tableQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String tableName = StringUtils.getNameFromQualifiedName(tableQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return TablePartition.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(name, tableQualifiedName))
                .connectorType(connectorType)
                .tableName(tableName)
                .tableQualifiedName(tableQualifiedName)
                .parentTable(Table.refByQualifiedName(tableQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique table partition name.
     *
     * @param name of the table partition
     * @param tableQualifiedName unique name of the table in which this partition exists
     * @return a unique name for the table partition
     */
    public static String generateQualifiedName(String name, String tableQualifiedName) {
        return tableQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the minimal request necessary to update the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> updater(String qualifiedName, String name) {
        return TablePartition.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TablePartition, from a potentially
     * more-complete TablePartition object.
     *
     * @return the minimal object necessary to update the TablePartition, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TablePartition are not found in the initial object
     */
    @Override
    public TablePartitionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TablePartition", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TablePartition's owners
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TablePartition, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to update the TablePartition's certificate
     * @param qualifiedName of the TablePartition
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TablePartition, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TablePartition)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TablePartition's certificate
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to update the TablePartition's announcement
     * @param qualifiedName of the TablePartition
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TablePartition)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TablePartition.
     *
     * @param client connectivity to the Atlan client from which to remove the TablePartition's announcement
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TablePartition.
     *
     * @param qualifiedName for the TablePartition
     * @param name human-readable name of the TablePartition
     * @param terms the list of terms to replace on the TablePartition, or null to remove all terms from the TablePartition
     * @return the TablePartition that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TablePartition's assigned terms
     * @param qualifiedName for the TablePartition
     * @param name human-readable name of the TablePartition
     * @param terms the list of terms to replace on the TablePartition, or null to remove all terms from the TablePartition
     * @return the TablePartition that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TablePartition) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TablePartition, without replacing existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to append to the TablePartition
     * @return the TablePartition that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TablePartition, without replacing existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TablePartition
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to append to the TablePartition
     * @return the TablePartition that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TablePartition) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TablePartition, without replacing all existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to remove from the TablePartition, which must be referenced by GUID
     * @return the TablePartition that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TablePartition, without replacing all existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TablePartition
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to remove from the TablePartition, which must be referenced by GUID
     * @return the TablePartition that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TablePartition) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     */
    public static TablePartition appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     */
    public static TablePartition appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TablePartition) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     */
    public static TablePartition appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     */
    public static TablePartition appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TablePartition) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TablePartition
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TablePartition
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TablePartition
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the TablePartition
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TablePartition
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TablePartition
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
