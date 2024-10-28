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
import com.atlan.model.enums.CustomTemperatureType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
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
 * Instances of CustomField in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class CustomField extends Asset
        implements ICustomField, IColumn, ICustom, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CustomField";

    /** Fixed typeName for CustomFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Calculate view in which this column exists. */
    @Attribute
    ICalculationView calculationView;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Average value in this column. */
    @Attribute
    Double columnAverage;

    /** Average length of values in a string column. */
    @Attribute
    Double columnAverageLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> columnDbtModelColumns;

    /** Level of nesting of this column, used for STRUCT and NESTED columns. */
    @Attribute
    Integer columnDepthLevel;

    /** Number of rows that contain distinct values. */
    @Attribute
    Integer columnDistinctValuesCount;

    /** Number of rows that contain distinct values. */
    @Attribute
    Long columnDistinctValuesCountLong;

    /** Number of rows that contain duplicate values. */
    @Attribute
    Integer columnDuplicateValuesCount;

    /** Number of rows that contain duplicate values. */
    @Attribute
    Long columnDuplicateValuesCountLong;

    /** List of top-level upstream nested columns. */
    @Attribute
    @Singular("putColumnHierarchy")
    List<Map<String, String>> columnHierarchy;

    /** List of values in a histogram that represents the contents of this column. */
    @Attribute
    Histogram columnHistogram;

    /** Greatest value in a numeric column. */
    @Attribute
    Double columnMax;

    /** Length of the longest value in a string column. */
    @Attribute
    Integer columnMaximumStringLength;

    /** List of the greatest values in a column. */
    @Attribute
    @Singular("addColumnMax")
    SortedSet<String> columnMaxs;

    /** Arithmetic mean of the values in a numeric column. */
    @Attribute
    Double columnMean;

    /** Calculated median of the values in a numeric column. */
    @Attribute
    Double columnMedian;

    /** Least value in a numeric column. */
    @Attribute
    Double columnMin;

    /** Length of the shortest value in a string column. */
    @Attribute
    Integer columnMinimumStringLength;

    /** List of the least values in a column. */
    @Attribute
    @Singular("addColumnMin")
    SortedSet<String> columnMins;

    /** Number of rows in a column that do not contain content. */
    @Attribute
    Integer columnMissingValuesCount;

    /** Number of rows in a column that do not contain content. */
    @Attribute
    Long columnMissingValuesCountLong;

    /** Percentage of rows in a column that do not contain content. */
    @Attribute
    Double columnMissingValuesPercentage;

    /** Calculated standard deviation of the values in a numeric column. */
    @Attribute
    Double columnStandardDeviation;

    /** Calculated sum of the values in a numeric column. */
    @Attribute
    Double columnSum;

    /** List of top values in this column. */
    @Attribute
    @Singular
    List<ColumnValueFrequencyMap> columnTopValues;

    /** Number of rows in which a value in this column appears only once. */
    @Attribute
    Integer columnUniqueValuesCount;

    /** Number of rows in which a value in this column appears only once. */
    @Attribute
    Long columnUniqueValuesCountLong;

    /** Ratio indicating how unique data in this column is: 0 indicates that all values are the same, 100 indicates that all values in this column are unique. */
    @Attribute
    Double columnUniquenessPercentage;

    /** Calculated variance of the values in a numeric column. */
    @Attribute
    Double columnVariance;

    /** Cosmos collection in which this column exists. */
    @Attribute
    ICosmosMongoDBCollection cosmosMongoDBCollection;

    /** Simple name of the dataset in which this asset exists, or empty if it is itself a dataset. */
    @Attribute
    String customDatasetName;

    /** Unique name of the dataset in which this asset exists, or empty if it is itself a dataset. */
    @Attribute
    String customDatasetQualifiedName;

    /** Unique identifier for the Custom asset from the source system. */
    @Attribute
    String customSourceId;

    /** CustomTable asset containing this CustomField. */
    @Attribute
    ICustomTable customTable;

    /** Temperature of the CustomTable asset. */
    @Attribute
    CustomTemperatureType customTemperature;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> dataQualityMetricDimensions;

    /** Data type of values in this column. */
    @Attribute
    String dataType;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtMetric> dbtMetrics;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> dbtModelColumns;

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

    /** Default value for this column. */
    @Attribute
    String defaultValue;

    /** Column this foreign key column refers to. */
    @Attribute
    IColumn foreignKeyFrom;

    /** Columns that use this column as a foreign key. */
    @Attribute
    @Singular("addForeignKeyTo")
    SortedSet<IColumn> foreignKeyTo;

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

    /** Whether this column is a clustered column (true) or not (false). */
    @Attribute
    Boolean isClustered;

    /** Whether this column is a distribution column (true) or not (false). */
    @Attribute
    Boolean isDist;

    /** When true, this column is a foreign key to another table. NOTE: this must be true when using the foreignKeyTo relationship to specify columns that refer to this column as a foreign key. */
    @Attribute
    Boolean isForeign;

    /** When true, this column is indexed in the database. */
    @Attribute
    Boolean isIndexed;

    /** When true, the values in this column can be null. */
    @Attribute
    Boolean isNullable;

    /** Whether this column is a partition column (true) or not (false). */
    @Attribute
    Boolean isPartition;

    /** Whether this column is pinned (true) or not (false). */
    @Attribute
    Boolean isPinned;

    /** When true, this column is the primary key for the table. */
    @Attribute
    Boolean isPrimary;

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Whether this column is a sort column (true) or not (false). */
    @Attribute
    Boolean isSort;

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    @Attribute
    @Date
    Long lastProfiledAt;

    /** Materialized view in which this column exists. */
    @Attribute
    @JsonProperty("materialisedView")
    IMaterializedView materializedView;

    /** Maximum length of a value in this column. */
    @Attribute
    Long maxLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> metricTimestamps;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Number of columns nested within this (STRUCT or NESTED) column. */
    @Attribute
    Integer nestedColumnCount;

    /** Order (position) in which this column appears in the nested Column (nest level starts at 1). */
    @Attribute
    String nestedColumnOrder;

    /** Nested columns that exist within this column. */
    @Attribute
    @Singular
    SortedSet<IColumn> nestedColumns;

    /** Simple name of the cosmos/mongo collection in which this SQL asset (column) exists, or empty if it does not exist within a cosmos/mongo collection. */
    @Attribute
    String nosqlCollectionName;

    /** Unique name of the cosmos/mongo collection in which this SQL asset (column) exists, or empty if it does not exist within a cosmos/mongo collection. */
    @Attribute
    String nosqlCollectionQualifiedName;

    /** Number of digits allowed to the right of the decimal point. */
    @Attribute
    Double numericScale;

    /** Order (position) in which this column appears in the table (starting at 1). */
    @Attribute
    Integer order;

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

    /** Column in which this sub-column is nested. */
    @Attribute
    IColumn parentColumn;

    /** Simple name of the column this column is nested within, for STRUCT and NESTED columns. */
    @Attribute
    String parentColumnName;

    /** Unique name of the column this column is nested within, for STRUCT and NESTED columns. */
    @Attribute
    String parentColumnQualifiedName;

    /** Order (position) of this partition column in the table. */
    @Attribute
    Integer partitionOrder;

    /** Time (epoch) at which this column was pinned, in milliseconds. */
    @Attribute
    @Date
    Long pinnedAt;

    /** User who pinned this column. */
    @Attribute
    String pinnedBy;

    /** Total number of digits allowed, when the dataType is numeric. */
    @Attribute
    Integer precision;

    /** Queries that access this column. */
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

    /** Number of unique users who have queried this asset. */
    @Attribute
    Long queryUserCount;

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    String rawDataTypeDefinition;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Snowflake dynamic table in which this column exists. */
    @Attribute
    ISnowflakeDynamicTable snowflakeDynamicTable;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Sub-data type of this column. */
    @Attribute
    String subDataType;

    /** Table in which this column exists. */
    @Attribute
    ITable table;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Table partition that contains this column. */
    @Attribute
    ITablePartition tablePartition;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Validations for this column. */
    @Attribute
    @Singular
    Map<String, String> validations;

    /** View in which this column exists. */
    @Attribute
    IView view;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a CustomField, from a potentially
     * more-complete CustomField object.
     *
     * @return the minimal object necessary to relate to the CustomField
     * @throws InvalidRequestException if any of the minimal set of required properties for a CustomField relationship are not found in the initial object
     */
    @Override
    public CustomField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CustomField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CustomField assets will be included.
     *
     * @return a fluent search that includes all CustomField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all CustomField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CustomField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CustomField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CustomField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) CustomFields will be included
     * @return a fluent search that includes all CustomField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all CustomField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CustomFields will be included
     * @return a fluent search that includes all CustomField assets
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
     * Reference to a CustomField by GUID. Use this to create a relationship to this CustomField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CustomField to reference
     * @return reference to a CustomField that can be used for defining a relationship to a CustomField
     */
    public static CustomField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CustomField by GUID. Use this to create a relationship to this CustomField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CustomField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CustomField that can be used for defining a relationship to a CustomField
     */
    public static CustomField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CustomField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CustomField by qualifiedName. Use this to create a relationship to this CustomField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CustomField to reference
     * @return reference to a CustomField that can be used for defining a relationship to a CustomField
     */
    public static CustomField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CustomField by qualifiedName. Use this to create a relationship to this CustomField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CustomField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CustomField that can be used for defining a relationship to a CustomField
     */
    public static CustomField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CustomField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CustomField by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the CustomField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CustomField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomField does not exist or the provided GUID is not a CustomField
     */
    @JsonIgnore
    public static CustomField get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a CustomField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CustomField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CustomField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomField does not exist or the provided GUID is not a CustomField
     */
    @JsonIgnore
    public static CustomField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a CustomField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CustomField to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CustomField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CustomField does not exist or the provided GUID is not a CustomField
     */
    @JsonIgnore
    public static CustomField get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CustomField) {
                return (CustomField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof CustomField) {
                return (CustomField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CustomField to active.
     *
     * @param qualifiedName for the CustomField
     * @return true if the CustomField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) CustomField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CustomField
     * @return true if the CustomField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CustomField.
     *
     * @param name of the CustomField
     * @param table in which the CustomField should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the CustomField, as a builder
     * @throws InvalidRequestException if the table provided is without any required attributes
     */
    public static CustomFieldBuilder<?, ?> creator(String name, CustomTable table) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", table.getQualifiedName());
        map.put("name", table.getName());
        validateRelationship(CustomTable.TYPE_NAME, map);
        return creator(name, table.getQualifiedName(), table.getName()).customTable(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CustomField.
     *
     * @param name of the CustomField (must be unique within the table)
     * @param tableQualifiedName unique name of the CustomTable in which the CustomField exists
     * @param tableName simple human-readable name of the CustomTable in which the CustomField exists
     * @return the minimal object necessary to create the CustomField, as a builder
     */
    public static CustomFieldBuilder<?, ?> creator(String name, String tableQualifiedName, String tableName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(tableQualifiedName);
        String datasetQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        String datasetName = StringUtils.getNameFromQualifiedName(datasetQualifiedName);
        return CustomField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, tableQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .customDatasetQualifiedName(datasetQualifiedName)
                .customDatasetName(datasetName)
                .tableQualifiedName(tableQualifiedName)
                .tableName(tableName)
                .customTable(CustomTable.refByQualifiedName(tableQualifiedName));
    }

    /**
     * Generate a unique CustomField name.
     *
     * @param name of the CustomField
     * @param tableQualifiedName unique name of the CustomTable in which this CustomField exists
     * @return a unique name for the CustomField
     */
    public static String generateQualifiedName(String name, String tableQualifiedName) {
        return tableQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the minimal request necessary to update the CustomField, as a builder
     */
    public static CustomFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomField, from a potentially
     * more-complete CustomField object.
     *
     * @return the minimal object necessary to update the CustomField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomField are not found in the initial object
     */
    @Override
    public CustomFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a CustomField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a CustomField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a CustomField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CustomField's owners
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CustomField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomField updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a CustomField.
     *
     * @param client connectivity to the Atlan tenant on which to update the CustomField's certificate
     * @param qualifiedName of the CustomField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CustomField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CustomField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a CustomField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CustomField's certificate
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a CustomField.
     *
     * @param client connectivity to the Atlan tenant on which to update the CustomField's announcement
     * @param qualifiedName of the CustomField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CustomField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CustomField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a CustomField.
     *
     * @param client connectivity to the Atlan client from which to remove the CustomField's announcement
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the updated CustomField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CustomField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CustomField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CustomField.
     *
     * @param qualifiedName for the CustomField
     * @param name human-readable name of the CustomField
     * @param terms the list of terms to replace on the CustomField, or null to remove all terms from the CustomField
     * @return the CustomField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the CustomField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CustomField's assigned terms
     * @param qualifiedName for the CustomField
     * @param name human-readable name of the CustomField
     * @param terms the list of terms to replace on the CustomField, or null to remove all terms from the CustomField
     * @return the CustomField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CustomField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CustomField, without replacing existing terms linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the CustomField
     * @param terms the list of terms to append to the CustomField
     * @return the CustomField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the CustomField, without replacing existing terms linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CustomField
     * @param qualifiedName for the CustomField
     * @param terms the list of terms to append to the CustomField
     * @return the CustomField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CustomField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CustomField, without replacing all existing terms linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the CustomField
     * @param terms the list of terms to remove from the CustomField, which must be referenced by GUID
     * @return the CustomField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a CustomField, without replacing all existing terms linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CustomField
     * @param qualifiedName for the CustomField
     * @param terms the list of terms to remove from the CustomField, which must be referenced by GUID
     * @return the CustomField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CustomField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CustomField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CustomField, without replacing existing Atlan tags linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CustomField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CustomField
     */
    public static CustomField appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CustomField, without replacing existing Atlan tags linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CustomField
     * @param qualifiedName of the CustomField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CustomField
     */
    public static CustomField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CustomField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CustomField, without replacing existing Atlan tags linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CustomField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CustomField
     */
    public static CustomField appendAtlanTags(
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
     * Add Atlan tags to a CustomField, without replacing existing Atlan tags linked to the CustomField.
     * Note: this operation must make two API calls — one to retrieve the CustomField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CustomField
     * @param qualifiedName of the CustomField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CustomField
     */
    public static CustomField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CustomField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CustomField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a CustomField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CustomField
     * @param qualifiedName of the CustomField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CustomField
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
