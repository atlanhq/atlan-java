/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.TableType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.CustomRatings;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instances of CustomTable in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class CustomTable extends Asset
        implements ICustomTable, ITable, ICustom, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CustomTable";

    /** Fixed typeName for CustomTables. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Alias for this table. */
    @Attribute
    String alias;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Number of columns in this table. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this table. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

    /** CustomDataset asset containing this CustomTable. */
    @Attribute
    ICustomDataset customDataset;

    /** Simple name of the dataset in which this asset exists, or empty if it is itself a dataset. */
    @Attribute
    String customDatasetName;

    /** Unique name of the dataset in which this asset exists, or empty if it is itself a dataset. */
    @Attribute
    String customDatasetQualifiedName;

    /** CustomField assets contained within this CustomTable. */
    @Attribute
    @Singular
    SortedSet<ICustomField> customFields;

    /** Ratings for the CustomTable asset from the source system. */
    @Attribute
    @Singular
    List<CustomRatings> customRatings;

    /** Unique identifier for the Custom asset from the source system. */
    @Attribute
    String customSourceId;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
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
    @Singular
    SortedSet<ITable> dimensions;

    /** External location of this table, for example: an S3 object location. */
    @Attribute
    String externalLocation;

    /** Format of the external location of this table, for example: JSON, CSV, PARQUET, etc. */
    @Attribute
    String externalLocationFormat;

    /** Region of the external location of this table, for example: S3 region. */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITable> facts;

    /** iceberg table catalog name (can be any user defined name) */
    @Attribute
    String icebergCatalogName;

    /** iceberg table catalog type (glue, polaris, snowflake) */
    @Attribute
    String icebergCatalogSource;

    /** catalog table name (actual table name on the catalog side). */
    @Attribute
    String icebergCatalogTableName;

    /** catalog table namespace (actual database name on the catalog side). */
    @Attribute
    String icebergCatalogTableNamespace;

    /** iceberg table base location inside the external volume. */
    @Attribute
    String icebergTableBaseLocation;

    /** iceberg table type (managed vs unmanaged) */
    @Attribute
    String icebergTableType;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Whether this table is partitioned (true) or not (false). */
    @Attribute
    Boolean isPartitioned;

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Whether preview queries are allowed for this table (true) or not (false). */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this table is a sharded table (true) or not (false). */
    @Attribute
    Boolean isSharded;

    /** Whether this table is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    @Attribute
    @Date
    Long lastProfiledAt;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /** Number of partitions in this table. */
    @Attribute
    Long partitionCount;

    /** List of partitions in this table. */
    @Attribute
    String partitionList;

    /** Partition strategy for this table. */
    @Attribute
    String partitionStrategy;

    /** Partitions that exist within this table. */
    @Attribute
    @Singular
    SortedSet<ITablePartition> partitions;

    /** Queries that access this table. */
    @Attribute
    @Singular
    SortedSet<IAtlanQuery> queries;

    /** Number of times this asset has been queried. */
    @Attribute
    Long queryCount;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** Configuration for preview queries. */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** Number of unique users who have queried this asset. */
    @Attribute
    Long queryUserCount;

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Number of rows in this table. */
    @Attribute
    Long rowCount;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Size of this table, in bytes. */
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

    /** external volume name for the table. */
    @Attribute
    String tableExternalVolumeName;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Data retention time in days. */
    @Attribute
    Long tableRetentionTime;

    /** Type of the table. */
    @Attribute
    TableType tableType;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a CustomTable, from a potentially
     * more-complete CustomTable object.
     *
     * @return the minimal object necessary to relate to the CustomTable
     * @throws InvalidRequestException if any of the minimal set of required properties for a CustomTable relationship are not found in the initial object
     */
    @Override
    public CustomTable trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all CustomTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CustomTable assets will be included.
     *
     * @return a fluent search that includes all CustomTable assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all CustomTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CustomTable assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CustomTable assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CustomTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) CustomTables will be included
     * @return a fluent search that includes all CustomTable assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all CustomTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CustomTables will be included
     * @return a fluent search that includes all CustomTable assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a CustomTable by GUID. Use this to create a relationship to this CustomTable,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CustomTable to reference
     * @return reference to a CustomTable that can be used for defining a relationship to a CustomTable
     */
    public static CustomTable refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CustomTable by GUID. Use this to create a relationship to this CustomTable,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CustomTable to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CustomTable that can be used for defining a relationship to a CustomTable
     */
    public static CustomTable refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CustomTable._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CustomTable by qualifiedName. Use this to create a relationship to this CustomTable,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CustomTable to reference
     * @return reference to a CustomTable that can be used for defining a relationship to a CustomTable
     */
    public static CustomTable refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CustomTable by qualifiedName. Use this to create a relationship to this CustomTable,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CustomTable to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CustomTable that can be used for defining a relationship to a CustomTable
     */
    public static CustomTable refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CustomTable._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CustomTable by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the CustomTable to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CustomTable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomTable does not exist or the provided GUID is not a CustomTable
     */
    @JsonIgnore
    public static CustomTable get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a CustomTable by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CustomTable to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CustomTable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomTable does not exist or the provided GUID is not a CustomTable
     */
    @JsonIgnore
    public static CustomTable get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a CustomTable by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CustomTable to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CustomTable, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomTable does not exist or the provided GUID is not a CustomTable
     */
    @JsonIgnore
    public static CustomTable get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CustomTable) {
                return (CustomTable) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof CustomTable) {
                return (CustomTable) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CustomTable to active.
     *
     * @param qualifiedName for the CustomTable
     * @return true if the CustomTable is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) CustomTable to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CustomTable
     * @return true if the CustomTable is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CustomTable.
     *
     * @param name of the CustomTable
     * @param dataset in which the CustomTable should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the CustomTable, as a builder
     * @throws InvalidRequestException if the dataset provided is without any required attributes
     */
    public static CustomTableBuilder<?, ?> creator(String name, CustomDataset dataset) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", dataset.getQualifiedName());
        map.put("name", dataset.getName());
        validateRelationship(CustomDataset.TYPE_NAME, map);
        return creator(name, dataset.getQualifiedName(), dataset.getName()).customDataset(dataset.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CustomTable.
     *
     * @param name of the CustomTable (must be unique within the dataset)
     * @param datasetQualifiedName unique name of the CustomDataset in which the CustomTable exists
     * @param datasetName simple human-readable name of the CustomDataset in which the CustomTable exists
     * @return the minimal object necessary to create the CustomTable, as a builder
     */
    public static CustomTableBuilder<?, ?> creator(String name, String datasetQualifiedName, String datasetName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(datasetQualifiedName);
        return CustomTable._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, datasetQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .customDatasetQualifiedName(datasetQualifiedName)
                .customDatasetName(datasetName)
                .customDataset(CustomDataset.refByQualifiedName(datasetQualifiedName));
    }

    /**
     * Generate a unique CustomTable name.
     *
     * @param name of the CustomTable
     * @param datasetQualifiedName unique name of the CustomDataset in which this CustomTable exists
     * @return a unique name for the CustomTable
     */
    public static String generateQualifiedName(String name, String datasetQualifiedName) {
        return datasetQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the minimal request necessary to update the CustomTable, as a builder
     */
    public static CustomTableBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomTable._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomTable, from a potentially
     * more-complete CustomTable object.
     *
     * @return the minimal object necessary to update the CustomTable, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomTable are not found in the initial object
     */
    @Override
    public CustomTableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a CustomTable.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomTable) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a CustomTable.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomTable) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a CustomTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CustomTable's owners
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomTable) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CustomTable, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a CustomTable.
     *
     * @param client connectivity to the Atlan tenant on which to update the CustomTable's certificate
     * @param qualifiedName of the CustomTable
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CustomTable, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CustomTable)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a CustomTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CustomTable's certificate
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomTable) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a CustomTable.
     *
     * @param client connectivity to the Atlan tenant on which to update the CustomTable's announcement
     * @param qualifiedName of the CustomTable
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CustomTable)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a CustomTable.
     *
     * @param client connectivity to the Atlan client from which to remove the CustomTable's announcement
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the updated CustomTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomTable) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CustomTable.
     *
     * @param qualifiedName for the CustomTable
     * @param name human-readable name of the CustomTable
     * @param terms the list of terms to replace on the CustomTable, or null to remove all terms from the CustomTable
     * @return the CustomTable that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the CustomTable.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CustomTable's assigned terms
     * @param qualifiedName for the CustomTable
     * @param name human-readable name of the CustomTable
     * @param terms the list of terms to replace on the CustomTable, or null to remove all terms from the CustomTable
     * @return the CustomTable that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CustomTable) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CustomTable, without replacing existing terms linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the CustomTable
     * @param terms the list of terms to append to the CustomTable
     * @return the CustomTable that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the CustomTable, without replacing existing terms linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CustomTable
     * @param qualifiedName for the CustomTable
     * @param terms the list of terms to append to the CustomTable
     * @return the CustomTable that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CustomTable) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CustomTable, without replacing all existing terms linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the CustomTable
     * @param terms the list of terms to remove from the CustomTable, which must be referenced by GUID
     * @return the CustomTable that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a CustomTable, without replacing all existing terms linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CustomTable
     * @param qualifiedName for the CustomTable
     * @param terms the list of terms to remove from the CustomTable, which must be referenced by GUID
     * @return the CustomTable that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CustomTable removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CustomTable) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CustomTable, without replacing existing Atlan tags linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CustomTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CustomTable
     */
    public static CustomTable appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CustomTable, without replacing existing Atlan tags linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CustomTable
     * @param qualifiedName of the CustomTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CustomTable
     */
    public static CustomTable appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CustomTable) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CustomTable, without replacing existing Atlan tags linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CustomTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CustomTable
     */
    public static CustomTable appendAtlanTags(
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
     * Add Atlan tags to a CustomTable, without replacing existing Atlan tags linked to the CustomTable.
     * Note: this operation must make two API calls — one to retrieve the CustomTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CustomTable
     * @param qualifiedName of the CustomTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CustomTable
     */
    public static CustomTable appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CustomTable) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CustomTable
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a CustomTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CustomTable
     * @param qualifiedName of the CustomTable
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CustomTable
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
