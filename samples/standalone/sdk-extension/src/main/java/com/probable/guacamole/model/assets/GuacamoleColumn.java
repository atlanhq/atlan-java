/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.probable.guacamole.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Date;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.IAsset;
import com.atlan.model.assets.IAtlanQuery;
import com.atlan.model.assets.ICalculationView;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IColumn;
import com.atlan.model.assets.ICosmosMongoDBCollection;
import com.atlan.model.assets.IDbtMetric;
import com.atlan.model.assets.IDbtModel;
import com.atlan.model.assets.IDbtModelColumn;
import com.atlan.model.assets.IDbtSource;
import com.atlan.model.assets.IDbtTest;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.IMaterializedView;
import com.atlan.model.assets.IMetric;
import com.atlan.model.assets.IModelAttribute;
import com.atlan.model.assets.IModelEntity;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.assets.ISQL;
import com.atlan.model.assets.ISnowflakeDynamicTable;
import com.atlan.model.assets.ISparkJob;
import com.atlan.model.assets.ITable;
import com.atlan.model.assets.ITablePartition;
import com.atlan.model.assets.IView;
import com.atlan.model.assets.Table;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Specialized form of a column specific to Guacamole.
 */
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class GuacamoleColumn extends Asset
        implements IGuacamoleColumn, IColumn, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GuacamoleColumn";

    /** Fixed typeName for GuacamoleColumns. */
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

    /** Compression type of this column. */
    @Attribute
    String columnCompression;

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

    /** Time (epoch) when this column was imagined, in milliseconds. */
    @Attribute
    @Date
    Long guacamoleConceptualized;

    /** Specialized table that contains this specialized column. */
    @Attribute
    IGuacamoleTable guacamoleTable;

    /** Maximum size of a Guacamole column. */
    @Attribute
    Long guacamoleWidth;

    /** TBC */
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

    /** TBC */
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
     * Builds the minimal object necessary to create a relationship to a GuacamoleColumn, from a potentially
     * more-complete GuacamoleColumn object.
     *
     * @return the minimal object necessary to relate to the GuacamoleColumn
     * @throws InvalidRequestException if any of the minimal set of required properties for a GuacamoleColumn relationship are not found in the initial object
     */
    @Override
    public GuacamoleColumn trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all GuacamoleColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GuacamoleColumn assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all GuacamoleColumn assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all GuacamoleColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GuacamoleColumns will be included
     * @return a fluent search that includes all GuacamoleColumn assets
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
     * Reference to a GuacamoleColumn by GUID. Use this to create a relationship to this GuacamoleColumn,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the GuacamoleColumn to reference
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GuacamoleColumn by GUID. Use this to create a relationship to this GuacamoleColumn,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the GuacamoleColumn to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByGuid(String guid, Reference.SaveSemantic semantic) {
        return GuacamoleColumn._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a GuacamoleColumn by qualifiedName. Use this to create a relationship to this GuacamoleColumn,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the GuacamoleColumn to reference
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GuacamoleColumn by qualifiedName. Use this to create a relationship to this GuacamoleColumn,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the GuacamoleColumn to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return GuacamoleColumn._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a GuacamoleColumn by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleColumn to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GuacamoleColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleColumn does not exist or the provided GUID is not a GuacamoleColumn
     */
    @JsonIgnore
    public static GuacamoleColumn get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a GuacamoleColumn by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleColumn to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GuacamoleColumn, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleColumn does not exist or the provided GUID is not a GuacamoleColumn
     */
    @JsonIgnore
    public static GuacamoleColumn get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a GuacamoleColumn by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleColumn to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GuacamoleColumn, including any relationships
     * @return the requested GuacamoleColumn, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleColumn does not exist or the provided GUID is not a GuacamoleColumn
     */
    @JsonIgnore
    public static GuacamoleColumn get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a GuacamoleColumn by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleColumn to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GuacamoleColumn, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the GuacamoleColumn
     * @return the requested GuacamoleColumn, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleColumn does not exist or the provided GUID is not a GuacamoleColumn
     */
    @JsonIgnore
    public static GuacamoleColumn get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = GuacamoleColumn.select(client)
                    .where(GuacamoleColumn.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = GuacamoleColumn.select(client)
                    .where(GuacamoleColumn.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) GuacamoleColumn to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GuacamoleColumn
     * @return true if the GuacamoleColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a GuacamoleColumn.
     *
     * @param name of the GuacamoleColumn
     * @param parentQualifiedName unique name of the GuacamoleTable in which this GuacamoleColumn exists
     * @param order the order the GuacamoleColumn appears within its parent (the GuacamoleColumn's position)
     * @return the minimal request necessary to create the GuacamoleColumn, as a builder
     */
    public static GuacamoleColumnBuilder<?, ?> creator(String name, String parentQualifiedName, int order) {
        String[] tokens = parentQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return GuacamoleColumn._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, parentQualifiedName))
                .connectorType(connectorType)
                .tableName(parentName)
                .tableQualifiedName(parentQualifiedName)
                .table(Table.refByQualifiedName(parentQualifiedName))
                .guacamoleTable(GuacamoleTable.refByQualifiedName(parentQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .order(order);
    }

    /**
     * Generate a unique GuacamoleColumn name.
     *
     * @param name of the GuacamoleColumn
     * @param parentQualifiedName unique name of the container in which this GuacamoleColumn exists
     * @return a unique name for the GuacamoleColumn
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GuacamoleColumn.
     *
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the minimal request necessary to update the GuacamoleColumn, as a builder
     */
    public static GuacamoleColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return GuacamoleColumn._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GuacamoleColumn, from a potentially
     * more-complete GuacamoleColumn object.
     *
     * @return the minimal object necessary to update the GuacamoleColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GuacamoleColumn are not found in the initial object
     */
    @Override
    public GuacamoleColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the updated GuacamoleColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the updated GuacamoleColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GuacamoleColumn's owners
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the updated GuacamoleColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the GuacamoleColumn's certificate
     * @param qualifiedName of the GuacamoleColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GuacamoleColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GuacamoleColumn)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GuacamoleColumn's certificate
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the updated GuacamoleColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the GuacamoleColumn's announcement
     * @param qualifiedName of the GuacamoleColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (GuacamoleColumn)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan client from which to remove the GuacamoleColumn's announcement
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the updated GuacamoleColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant on which to replace the GuacamoleColumn's assigned terms
     * @param qualifiedName for the GuacamoleColumn
     * @param name human-readable name of the GuacamoleColumn
     * @param terms the list of terms to replace on the GuacamoleColumn, or null to remove all terms from the GuacamoleColumn
     * @return the GuacamoleColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GuacamoleColumn replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (GuacamoleColumn) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the GuacamoleColumn, without replacing existing terms linked to the GuacamoleColumn.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the GuacamoleColumn
     * @param qualifiedName for the GuacamoleColumn
     * @param terms the list of terms to append to the GuacamoleColumn
     * @return the GuacamoleColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GuacamoleColumn appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GuacamoleColumn) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a GuacamoleColumn, without replacing all existing terms linked to the GuacamoleColumn.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the GuacamoleColumn
     * @param qualifiedName for the GuacamoleColumn
     * @param terms the list of terms to remove from the GuacamoleColumn, which must be referenced by GUID
     * @return the GuacamoleColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GuacamoleColumn removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GuacamoleColumn) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a GuacamoleColumn, without replacing existing Atlan tags linked to the GuacamoleColumn.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GuacamoleColumn
     * @param qualifiedName of the GuacamoleColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GuacamoleColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static GuacamoleColumn appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GuacamoleColumn) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GuacamoleColumn, without replacing existing Atlan tags linked to the GuacamoleColumn.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GuacamoleColumn
     * @param qualifiedName of the GuacamoleColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GuacamoleColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static GuacamoleColumn appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GuacamoleColumn) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a GuacamoleColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GuacamoleColumn
     * @param qualifiedName of the GuacamoleColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GuacamoleColumn
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
