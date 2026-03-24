/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.AssetHistogram;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an Iceberg column in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class IcebergColumn extends Asset
        implements IIcebergColumn, IColumn, IIceberg, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "IcebergColumn";

    /** Fixed typeName for IcebergColumns. */
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

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** Average value in this column. */
    @Attribute
    Double columnAverage;

    /** Average length of values in a string column. */
    @Attribute
    Double columnAverageLength;

    /** Average length of values in a string column. */
    @Attribute
    Double columnAverageLengthValue;

    /** Average value in this column. */
    @Attribute
    Double columnAverageValue;

    /** Compression type of this column. */
    @Attribute
    String columnCompression;

    /** Model columns related to this column. */
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

    /** Detailed information representing a histogram of values for a column. */
    @Attribute
    AssetHistogram columnDistributionHistogram;

    /** Number of rows that contain duplicate values. */
    @Attribute
    Integer columnDuplicateValuesCount;

    /** Number of rows that contain duplicate values. */
    @Attribute
    Long columnDuplicateValuesCountLong;

    /** Encoding type of this column. */
    @Attribute
    String columnEncoding;

    /** List of top-level upstream nested columns. */
    @Attribute
    @Singular("putColumnHierarchy")
    List<Map<String, String>> columnHierarchy;

    /** List of values in a histogram that represents the contents of this column. */
    @Attribute
    Histogram columnHistogram;

    /** When true, this column is of type measure/calculated. */
    @Attribute
    Boolean columnIsMeasure;

    /** Greatest value in a numeric column. */
    @Attribute
    Double columnMax;

    /** Greatest value in a numeric column. */
    @Attribute
    Double columnMaxValue;

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

    /** Arithmetic mean of the values in a numeric column. */
    @Attribute
    Double columnMeanValue;

    /** The type of measure/calculated column this is, eg: base, calculated, derived. */
    @Attribute
    String columnMeasureType;

    /** Calculated median of the values in a numeric column. */
    @Attribute
    Double columnMedian;

    /** Calculated median of the values in a numeric column. */
    @Attribute
    Double columnMedianValue;

    /** Least value in a numeric column. */
    @Attribute
    Double columnMin;

    /** Least value in a numeric column. */
    @Attribute
    Double columnMinValue;

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

    /** Calculated standard deviation of the values in a numeric column. */
    @Attribute
    Double columnStandardDeviationValue;

    /** Calculated sum of the values in a numeric column. */
    @Attribute
    Double columnSum;

    /** Calculated sum of the values in a numeric column. */
    @Attribute
    Double columnSumValue;

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

    /** Calculated variance of the values in a numeric column. */
    @Attribute
    Double columnVarianceValue;

    /** Cosmos collection in which this column exists. */
    @Attribute
    ICosmosMongoDBCollection cosmosMongoDBCollection;

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

    /** Metrics related to this model column. */
    @Attribute
    @Singular
    SortedSet<IDbtMetric> dbtMetrics;

    /** (Deprecated) Model columns related to this model column. */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> dbtModelColumns;

    /** (Deprecated) Model containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** DBT seeds that materialize the SQL asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSeed> dbtSeedAssets;

    /** Source containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** Tests related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** Default value for this column. */
    @Attribute
    String defaultValue;

    /** Rules that are applied on this column. */
    @Attribute
    @Singular
    SortedSet<IDataQualityRule> dqBaseColumnRules;

    /** Rules where this column is referenced. */
    @Attribute
    @Singular
    SortedSet<IDataQualityRule> dqReferenceColumnRules;

    /** Column this foreign key column refers to. */
    @Attribute
    IColumn foreignKeyFrom;

    /** Columns that use this column as a foreign key. */
    @Attribute
    @Singular("addForeignKeyTo")
    SortedSet<IColumn> foreignKeyTo;

    /** Ordered array of namespace assets with qualified name and name representing the complete namespace hierarchy path for this asset, from immediate parent to root namespace. */
    @Attribute
    @Singular("addIcebergNamespaceHierarchy")
    List<Map<String, String>> icebergNamespaceHierarchy;

    /** Unique name of the immediate parent namespace in which this asset exists. */
    @Attribute
    String icebergParentNamespaceQualifiedName;

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

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Collection in which the columns exist. */
    @Attribute
    IMongoDBCollection mongoDBCollection;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

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

    /** Raw data type definition of this column. */
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

    /** Semantic logical tables that reference this physical table or view. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeSemanticLogicalTable> snowflakeSemanticLogicalTables;

    /** Unique name of the context in which the model versions exist, or empty if it does not exist within an AI model context. */
    @Attribute
    String sqlAIModelContextQualifiedName;

    /** Sources related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** Assets related to the model. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

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
     * Builds the minimal object necessary to create a relationship to a IcebergColumn, from a potentially
     * more-complete IcebergColumn object.
     *
     * @return the minimal object necessary to relate to the IcebergColumn
     * @throws InvalidRequestException if any of the minimal set of required properties for a IcebergColumn relationship are not found in the initial object
     */
    @Override
    public IcebergColumn trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all IcebergColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) IcebergColumn assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all IcebergColumn assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all IcebergColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) IcebergColumns will be included
     * @return a fluent search that includes all IcebergColumn assets
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
     * Reference to a IcebergColumn by GUID. Use this to create a relationship to this IcebergColumn,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the IcebergColumn to reference
     * @return reference to a IcebergColumn that can be used for defining a relationship to a IcebergColumn
     */
    public static IcebergColumn refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a IcebergColumn by GUID. Use this to create a relationship to this IcebergColumn,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the IcebergColumn to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a IcebergColumn that can be used for defining a relationship to a IcebergColumn
     */
    public static IcebergColumn refByGuid(String guid, Reference.SaveSemantic semantic) {
        return IcebergColumn._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a IcebergColumn by qualifiedName. Use this to create a relationship to this IcebergColumn,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the IcebergColumn to reference
     * @return reference to a IcebergColumn that can be used for defining a relationship to a IcebergColumn
     */
    public static IcebergColumn refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a IcebergColumn by qualifiedName. Use this to create a relationship to this IcebergColumn,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the IcebergColumn to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a IcebergColumn that can be used for defining a relationship to a IcebergColumn
     */
    public static IcebergColumn refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return IcebergColumn._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a IcebergColumn by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergColumn to retrieve, either its GUID or its full qualifiedName
     * @return the requested full IcebergColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergColumn does not exist or the provided GUID is not a IcebergColumn
     */
    @JsonIgnore
    public static IcebergColumn get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a IcebergColumn by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergColumn to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full IcebergColumn, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergColumn does not exist or the provided GUID is not a IcebergColumn
     */
    @JsonIgnore
    public static IcebergColumn get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof IcebergColumn) {
                return (IcebergColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof IcebergColumn) {
                return (IcebergColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a IcebergColumn by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergColumn to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the IcebergColumn, including any relationships
     * @return the requested IcebergColumn, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergColumn does not exist or the provided GUID is not a IcebergColumn
     */
    @JsonIgnore
    public static IcebergColumn get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a IcebergColumn by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergColumn to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the IcebergColumn, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the IcebergColumn
     * @return the requested IcebergColumn, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergColumn does not exist or the provided GUID is not a IcebergColumn
     */
    @JsonIgnore
    public static IcebergColumn get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = IcebergColumn.select(client)
                    .where(IcebergColumn.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof IcebergColumn) {
                return (IcebergColumn) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = IcebergColumn.select(client)
                    .where(IcebergColumn.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof IcebergColumn) {
                return (IcebergColumn) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) IcebergColumn to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the IcebergColumn
     * @return true if the IcebergColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a IcebergColumn.
     *
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the minimal request necessary to update the IcebergColumn, as a builder
     */
    public static IcebergColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return IcebergColumn._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a IcebergColumn,
     * from a potentially more-complete IcebergColumn object.
     *
     * @return the minimal object necessary to update the IcebergColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a IcebergColumn are not present in the initial object
     */
    @Override
    public IcebergColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class IcebergColumnBuilder<C extends IcebergColumn, B extends IcebergColumnBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the updated IcebergColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergColumn) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the updated IcebergColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergColumn) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the IcebergColumn's owners
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the updated IcebergColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergColumn) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the IcebergColumn's certificate
     * @param qualifiedName of the IcebergColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated IcebergColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (IcebergColumn)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the IcebergColumn's certificate
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the updated IcebergColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergColumn) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the IcebergColumn's announcement
     * @param qualifiedName of the IcebergColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (IcebergColumn)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a IcebergColumn.
     *
     * @param client connectivity to the Atlan client from which to remove the IcebergColumn's announcement
     * @param qualifiedName of the IcebergColumn
     * @param name of the IcebergColumn
     * @return the updated IcebergColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergColumn) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant on which to replace the IcebergColumn's assigned terms
     * @param qualifiedName for the IcebergColumn
     * @param name human-readable name of the IcebergColumn
     * @param terms the list of terms to replace on the IcebergColumn, or null to remove all terms from the IcebergColumn
     * @return the IcebergColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static IcebergColumn replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (IcebergColumn) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the IcebergColumn, without replacing existing terms linked to the IcebergColumn.
     * Note: this operation must make two API calls — one to retrieve the IcebergColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the IcebergColumn
     * @param qualifiedName for the IcebergColumn
     * @param terms the list of terms to append to the IcebergColumn
     * @return the IcebergColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static IcebergColumn appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (IcebergColumn) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a IcebergColumn, without replacing all existing terms linked to the IcebergColumn.
     * Note: this operation must make two API calls — one to retrieve the IcebergColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the IcebergColumn
     * @param qualifiedName for the IcebergColumn
     * @param terms the list of terms to remove from the IcebergColumn, which must be referenced by GUID
     * @return the IcebergColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static IcebergColumn removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (IcebergColumn) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a IcebergColumn, without replacing existing Atlan tags linked to the IcebergColumn.
     * Note: this operation must make two API calls — one to retrieve the IcebergColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the IcebergColumn
     * @param qualifiedName of the IcebergColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated IcebergColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static IcebergColumn appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (IcebergColumn) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a IcebergColumn, without replacing existing Atlan tags linked to the IcebergColumn.
     * Note: this operation must make two API calls — one to retrieve the IcebergColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the IcebergColumn
     * @param qualifiedName of the IcebergColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated IcebergColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static IcebergColumn appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (IcebergColumn) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a IcebergColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a IcebergColumn
     * @param qualifiedName of the IcebergColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the IcebergColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
