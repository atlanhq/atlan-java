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
 * Instance of a column in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class Column extends Asset implements IColumn, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Column";

    /** Fixed typeName for Columns. */
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
     * Builds the minimal object necessary to create a relationship to a Column, from a potentially
     * more-complete Column object.
     *
     * @return the minimal object necessary to relate to the Column
     * @throws InvalidRequestException if any of the minimal set of required properties for a Column relationship are not found in the initial object
     */
    @Override
    public Column trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Column assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Column assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Column assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Column assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Columns will be included
     * @return a fluent search that includes all Column assets
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
     * Reference to a Column by GUID. Use this to create a relationship to this Column,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Column by GUID. Use this to create a relationship to this Column,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Column to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Column._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Column by qualifiedName. Use this to create a relationship to this Column,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Column by qualifiedName. Use this to create a relationship to this Column,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Column to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Column._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Column by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Column to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    @JsonIgnore
    public static Column get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Column by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Column to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Column, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    @JsonIgnore
    public static Column get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Column) {
                return (Column) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Column) {
                return (Column) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Column by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Column to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Column, including any relationships
     * @return the requested Column, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    @JsonIgnore
    public static Column get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Column by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Column to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Column, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Column
     * @return the requested Column, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    @JsonIgnore
    public static Column get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Column.select(client)
                    .where(Column.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Column) {
                return (Column) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Column.select(client)
                    .where(Column.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Column) {
                return (Column) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Column to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Column
     * @return true if the Column is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Retrieve the parent of this Column, irrespective of its type.
     * @return the reference to this Column's parent
     */
    public ISQL getParent() {
        if (table != null) {
            return (ISQL) table;
        } else if (view != null) {
            return (ISQL) view;
        } else if (materializedView != null) {
            return (ISQL) materializedView;
        } else if (tablePartition != null) {
            return (ISQL) tablePartition;
        }
        return null;
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param table in which the Column should be created, which must have at least
     *              a qualifiedName
     * @param order the order the Column appears within its table (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the table provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, Table table, int order) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", table.getConnectionQualifiedName());
        map.put("databaseName", table.getDatabaseName());
        map.put("databaseQualifiedName", table.getDatabaseQualifiedName());
        map.put("schemaName", table.getSchemaName());
        map.put("name", table.getName());
        map.put("qualifiedName", table.getQualifiedName());
        validateRelationship(Table.TYPE_NAME, map);
        return creator(
                        name,
                        table.getConnectionQualifiedName(),
                        table.getDatabaseName(),
                        table.getDatabaseQualifiedName(),
                        table.getSchemaName(),
                        table.getSchemaQualifiedName(),
                        table.getName(),
                        table.getQualifiedName(),
                        Table.TYPE_NAME,
                        null,
                        null,
                        order)
                .table(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param partition in which the Column should be created, which must have at least
     *                  a qualifiedName
     * @param order the order the Column appears within its partition (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the partition provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, TablePartition partition, int order)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", partition.getConnectionQualifiedName());
        map.put("databaseName", partition.getDatabaseName());
        map.put("databaseQualifiedName", partition.getDatabaseQualifiedName());
        map.put("schemaName", partition.getSchemaName());
        map.put("schemaQualifiedName", partition.getSchemaQualifiedName());
        map.put("name", partition.getName());
        map.put("qualifiedName", partition.getQualifiedName());
        validateRelationship(TablePartition.TYPE_NAME, map);
        return creator(
                        name,
                        partition.getConnectionQualifiedName(),
                        partition.getDatabaseName(),
                        partition.getDatabaseQualifiedName(),
                        partition.getSchemaName(),
                        partition.getSchemaQualifiedName(),
                        partition.getName(),
                        partition.getQualifiedName(),
                        TablePartition.TYPE_NAME,
                        null,
                        null,
                        order)
                .tablePartition(partition.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param view in which the Column should be created, which must have at least
     *             a qualifiedName
     * @param order the order the Column appears within its view (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the view provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, View view, int order) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", view.getConnectionQualifiedName());
        map.put("databaseName", view.getDatabaseName());
        map.put("databaseQualifiedName", view.getDatabaseQualifiedName());
        map.put("schemaName", view.getSchemaName());
        map.put("schemaQualifiedName", view.getSchemaQualifiedName());
        map.put("name", view.getName());
        map.put("qualifiedName", view.getQualifiedName());
        validateRelationship(View.TYPE_NAME, map);
        return creator(
                        name,
                        view.getConnectionQualifiedName(),
                        view.getDatabaseName(),
                        view.getDatabaseQualifiedName(),
                        view.getSchemaName(),
                        view.getSchemaQualifiedName(),
                        view.getName(),
                        view.getQualifiedName(),
                        View.TYPE_NAME,
                        null,
                        null,
                        order)
                .view(view.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param view in which the Column should be created, which must have at least
     *             a qualifiedName
     * @param order the order the Column appears within its materialized view (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the materialized view provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, MaterializedView view, int order)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", view.getConnectionQualifiedName());
        map.put("databaseName", view.getDatabaseName());
        map.put("databaseQualifiedName", view.getDatabaseQualifiedName());
        map.put("schemaName", view.getSchemaName());
        map.put("schemaQualifiedName", view.getSchemaQualifiedName());
        map.put("name", view.getName());
        map.put("qualifiedName", view.getQualifiedName());
        validateRelationship(MaterializedView.TYPE_NAME, map);
        return creator(
                        name,
                        view.getConnectionQualifiedName(),
                        view.getDatabaseName(),
                        view.getDatabaseQualifiedName(),
                        view.getSchemaName(),
                        view.getSchemaQualifiedName(),
                        view.getName(),
                        view.getQualifiedName(),
                        MaterializedView.TYPE_NAME,
                        null,
                        null,
                        order)
                .materializedView(view.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param parentQualifiedName unique name of the table / view / materialized view in which this Column exists
     * @param order the order the Column appears within its parent (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(String name, String parentType, String parentQualifiedName, int order) {
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
                name,
                connectionQualifiedName,
                databaseName,
                databaseQualifiedName,
                schemaName,
                schemaQualifiedName,
                parentName,
                parentQualifiedName,
                parentType,
                null,
                null,
                order);
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param connectionQualifiedName unique name of the connection in which the Column should be created
     * @param databaseName simple name of the database in which the Column should be created
     * @param databaseQualifiedName unique name of the database in which the Column should be created
     * @param schemaName simple name of the schema in which the Column should be created
     * @param schemaQualifiedName unique name of the schema in which the Column should be created
     * @param parentName simple name of the table / view / materialized view in which the Column should be created
     * @param parentQualifiedName unique name of the table / view / materialized view in which this Column exists
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param tableName (deprecated - unused)
     * @param tableQualifiedName (deprecated - unused)
     * @param order the order the Column appears within its parent (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String schemaName,
            String schemaQualifiedName,
            String parentName,
            String parentQualifiedName,
            String parentType,
            String tableName,
            String tableQualifiedName,
            int order) {
        ColumnBuilder<?, ?> builder = Column._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, parentQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .order(order);
        switch (parentType) {
            case Table.TYPE_NAME:
                builder.tableName(parentName)
                        .tableQualifiedName(parentQualifiedName)
                        .table(Table.refByQualifiedName(parentQualifiedName));
                break;
            case View.TYPE_NAME:
                builder.viewName(parentName)
                        .viewQualifiedName(parentQualifiedName)
                        .view(View.refByQualifiedName(parentQualifiedName));
                break;
            case MaterializedView.TYPE_NAME:
                builder.viewName(parentName)
                        .viewQualifiedName(parentQualifiedName)
                        .materializedView(MaterializedView.refByQualifiedName(parentQualifiedName));
                break;
            case TablePartition.TYPE_NAME:
                builder.tableName(parentName)
                        .tableQualifiedName(parentQualifiedName)
                        .tablePartition(TablePartition.refByQualifiedName(parentQualifiedName));
                break;
            case SnowflakeDynamicTable.TYPE_NAME:
                builder.tableName(parentName)
                        .tableQualifiedName(parentQualifiedName)
                        .snowflakeDynamicTable(SnowflakeDynamicTable.refByQualifiedName(parentQualifiedName));
                break;
        }
        return builder;
    }

    /**
     * Generate a unique Column name.
     *
     * @param name of the Column
     * @param parentQualifiedName unique name of the container in which this Column exists
     * @return a unique name for the Column
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a Column.
     *
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the minimal request necessary to update the Column, as a builder
     */
    public static ColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return Column._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Column, from a potentially
     * more-complete Column object.
     *
     * @return the minimal object necessary to update the Column, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Column are not found in the initial object
     */
    @Override
    public ColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ColumnBuilder<C extends Column, B extends ColumnBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a Column.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Column) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Column.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Column) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Column.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Column's owners
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Column) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Column.
     *
     * @param client connectivity to the Atlan tenant on which to update the Column's certificate
     * @param qualifiedName of the Column
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Column, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Column updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Column) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Column.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Column's certificate
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Column) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Column.
     *
     * @param client connectivity to the Atlan tenant on which to update the Column's announcement
     * @param qualifiedName of the Column
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Column updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Column) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Column.
     *
     * @param client connectivity to the Atlan client from which to remove the Column's announcement
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Column) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Column.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Column's assigned terms
     * @param qualifiedName for the Column
     * @param name human-readable name of the Column
     * @param terms the list of terms to replace on the Column, or null to remove all terms from the Column
     * @return the Column that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Column replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Column, without replacing existing terms linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Column
     * @param qualifiedName for the Column
     * @param terms the list of terms to append to the Column
     * @return the Column that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Column appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Column, without replacing all existing terms linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Column
     * @param qualifiedName for the Column
     * @param terms the list of terms to remove from the Column, which must be referenced by GUID
     * @return the Column that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Column removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Column, without replacing existing Atlan tags linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Column
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Column
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Column appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Column) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Column, without replacing existing Atlan tags linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Column
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Column
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static Column appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Column) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Column.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Column
     * @param qualifiedName of the Column
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Column
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
