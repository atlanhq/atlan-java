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
import com.atlan.model.assets.GlossaryTerm;
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
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@SuppressWarnings("cast")
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

    /** TBC */
    @Attribute
    Double columnAverage;

    /** TBC */
    @Attribute
    Double columnAverageLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> columnDbtModelColumns;

    /** TBC */
    @Attribute
    Integer columnDepthLevel;

    /** TBC */
    @Attribute
    Integer columnDistinctValuesCount;

    /** TBC */
    @Attribute
    Long columnDistinctValuesCountLong;

    /** TBC */
    @Attribute
    Integer columnDuplicateValuesCount;

    /** TBC */
    @Attribute
    Long columnDuplicateValuesCountLong;

    /** List of top-level upstream nested columns. */
    @Attribute
    @Singular("putColumnHierarchy")
    List<Map<String, String>> columnHierarchy;

    /** TBC */
    @Attribute
    Histogram columnHistogram;

    /** TBC */
    @Attribute
    Double columnMax;

    /** TBC */
    @Attribute
    Integer columnMaximumStringLength;

    /** TBC */
    @Attribute
    @Singular("addColumnMax")
    SortedSet<String> columnMaxs;

    /** TBC */
    @Attribute
    Double columnMean;

    /** TBC */
    @Attribute
    Double columnMedian;

    /** TBC */
    @Attribute
    Double columnMin;

    /** TBC */
    @Attribute
    Integer columnMinimumStringLength;

    /** TBC */
    @Attribute
    @Singular("addColumnMin")
    SortedSet<String> columnMins;

    /** TBC */
    @Attribute
    Integer columnMissingValuesCount;

    /** TBC */
    @Attribute
    Long columnMissingValuesCountLong;

    /** TBC */
    @Attribute
    Double columnMissingValuesPercentage;

    /** TBC */
    @Attribute
    Double columnStandardDeviation;

    /** TBC */
    @Attribute
    Double columnSum;

    /** TBC */
    @Attribute
    @Singular
    List<ColumnValueFrequencyMap> columnTopValues;

    /** TBC */
    @Attribute
    Integer columnUniqueValuesCount;

    /** TBC */
    @Attribute
    Long columnUniqueValuesCountLong;

    /** TBC */
    @Attribute
    Double columnUniquenessPercentage;

    /** TBC */
    @Attribute
    Double columnVariance;

    /** Cosmos collection in which this column exists. */
    @Attribute
    ICosmosMongoDBCollection cosmosMongoDBCollection;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> dataQualityMetricDimensions;

    /** TBC */
    @Attribute
    String dataType;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
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

    /** TBC */
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

    /** TBC */
    @Attribute
    Boolean isClustered;

    /** TBC */
    @Attribute
    Boolean isDist;

    /** TBC */
    @Attribute
    Boolean isForeign;

    /** TBC */
    @Attribute
    Boolean isIndexed;

    /** TBC */
    @Attribute
    Boolean isNullable;

    /** TBC */
    @Attribute
    Boolean isPartition;

    /** TBC */
    @Attribute
    Boolean isPinned;

    /** TBC */
    @Attribute
    Boolean isPrimary;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isSort;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Materialized view in which this column exists. */
    @Attribute
    @JsonProperty("materialisedView")
    IMaterializedView materializedView;

    /** TBC */
    @Attribute
    Long maxLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> metricTimestamps;

    /** TBC */
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

    /** TBC */
    @Attribute
    Double numericScale;

    /** TBC */
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

    /** TBC */
    @Attribute
    String parentColumnName;

    /** TBC */
    @Attribute
    String parentColumnQualifiedName;

    /** TBC */
    @Attribute
    Integer partitionOrder;

    /** TBC */
    @Attribute
    Long pinnedAt;

    /** TBC */
    @Attribute
    String pinnedBy;

    /** TBC */
    @Attribute
    Integer precision;

    /** Queries that access this column. */
    @Attribute
    @Singular
    SortedSet<IAtlanQuery> queries;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    String rawDataTypeDefinition;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
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

    /** TBC */
    @Attribute
    String subDataType;

    /** Table in which this column exists. */
    @Attribute
    ITable table;

    /** TBC */
    @Attribute
    String tableName;

    /** Table partition that contains this column. */
    @Attribute
    ITablePartition tablePartition;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> validations;

    /** View in which this column exists. */
    @Attribute
    IView view;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
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
     * Reference to a GuacamoleColumn by GUID.
     *
     * @param guid the GUID of the GuacamoleColumn to reference
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByGuid(String guid) {
        return GuacamoleColumn._internal().guid(guid).build();
    }

    /**
     * Reference to a GuacamoleColumn by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GuacamoleColumn to reference
     * @return reference to a GuacamoleColumn that can be used for defining a relationship to a GuacamoleColumn
     */
    public static GuacamoleColumn refByQualifiedName(String qualifiedName) {
        return GuacamoleColumn._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
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
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GuacamoleColumn, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleColumn does not exist or the provided GUID is not a GuacamoleColumn
     */
    @JsonIgnore
    public static GuacamoleColumn get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof GuacamoleColumn) {
                return (GuacamoleColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
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
