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
import com.atlan.model.structs.ColumnValueFrequencyMap;
import com.atlan.model.structs.Histogram;
import com.atlan.util.QueryFactory;
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
 * Instance of a column in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Column extends Asset implements IColumn, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Column";

    /** Fixed typeName for Columns. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Double columnAverage;

    /** Average length of values in a string column. */
    @Attribute
    Double columnAverageLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> columnDbtModelColumns;

    /** Level of nesting, used for STRUCT/NESTED columns */
    @Attribute
    Integer columnDepthLevel;

    /** Number of rows that contain distinct values. */
    @Attribute
    Integer columnDistinctValuesCount;

    /** TBC */
    @Attribute
    Long columnDistinctValuesCountLong;

    /** Number of rows that contain duplicate values. */
    @Attribute
    Integer columnDuplicateValuesCount;

    /** TBC */
    @Attribute
    Long columnDuplicateValuesCountLong;

    /** List of values in a histogram that represents the contents of the column. */
    @Attribute
    @Singular("addColumnHistogram")
    List<Histogram> columnHistogram;

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

    /** TBC */
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

    /** TBC */
    @Attribute
    @Singular
    List<ColumnValueFrequencyMap> columnTopValues;

    /** Number of rows in which a value in this column appears only once. */
    @Attribute
    Integer columnUniqueValuesCount;

    /** TBC */
    @Attribute
    Long columnUniqueValuesCountLong;

    /** Ratio indicating how unique data in the column is: 0 indicates that all values are the same, 100 indicates that all values in the column are unique. */
    @Attribute
    Double columnUniquenessPercentage;

    /** Calculated variance of the values in a numeric column. */
    @Attribute
    Double columnVariance;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> dataQualityMetricDimensions;

    /** Data type of values in the column. */
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

    /** Column this column refers to as a foreign key. */
    @Attribute
    IColumn foreignKeyFrom;

    /** All the columns that refer to this column as a foreign key. NOTE: when providing values to this relationship, isForeign must also be set to true. */
    @Attribute
    @Singular("addForeignKeyTo")
    SortedSet<IColumn> foreignKeyTo;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isClustered;

    /** TBC */
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

    /** TBC */
    @Attribute
    Boolean isPartition;

    /** TBC */
    @Attribute
    Boolean isPinned;

    /** When true, this column is the primary key for the table. */
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

    /** Materialized view in which this column exists, or empty if the column instead exists in a table or view. */
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

    /** TBC */
    @Attribute
    Integer nestedColumnCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> nestedColumns;

    /** Number of digits allowed to the right of the decimal point. */
    @Attribute
    Double numericScale;

    /** Order (position) in which the column appears in the table (starting at 1). */
    @Attribute
    Integer order;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
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

    /** Total number of digits allowed when the dataType is numeric. */
    @Attribute
    Integer precision;

    /** Queries that involve this column. */
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

    /** Table in which this column exists, or empty if the column instead exists in a view or materialized view. */
    @Attribute
    ITable table;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    ITablePartition tablePartition;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> validations;

    /** View in which this column exists, or empty if the column instead exists in a table or materialized view. */
    @Attribute
    IView view;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Start an asset filter that will return all Column assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Column assets will be included.
     *
     * @return an asset filter that includes all Column assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all Column assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Column assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all Column assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all Column assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Columns will be included
     * @return an asset filter that includes all Column assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all Column assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Columns will be included
     * @return an asset filter that includes all Column assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a Column by GUID.
     *
     * @param guid the GUID of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByGuid(String guid) {
        return Column._internal().guid(guid).build();
    }

    /**
     * Reference to a Column by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByQualifiedName(String qualifiedName) {
        return Column._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Column by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Column to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    @JsonIgnore
    public static Column get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
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
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (id.startsWith("default")) {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof Column) {
                return (Column) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "Column");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Column) {
                return (Column) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "Column");
            }
        }
    }

    /**
     * Retrieves a Column by its GUID, complete with all of its relationships.
     *
     * @param guid of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Column retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Column by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Column retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Column) {
            return (Column) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Column");
        }
    }

    /**
     * Retrieves a Column by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Column retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Column by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Column retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof Column) {
            return (Column) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Column");
        }
    }

    /**
     * Restore the archived (soft-deleted) Column to active.
     *
     * @param qualifiedName for the Column
     * @return true if the Column is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
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
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param parentQualifiedName unique name of the table / view / materialized view in which this Column exists
     * @param order the order the Column appears within its parent (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(String name, String parentType, String parentQualifiedName, int order) {
        String[] tokens = parentQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String tableName = null;
        String tableQualifiedName = null;
        String schemaQualifiedName;
        if (TablePartition.TYPE_NAME.equals(parentType)) {
            tableQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
            tableName = StringUtils.getNameFromQualifiedName(tableQualifiedName);
            schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        } else {
            schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        }
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        ColumnBuilder<?, ?> builder = Column._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, parentQualifiedName))
                .connectorType(connectorType)
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
                builder.tableName(tableName)
                        .tableQualifiedName(tableQualifiedName)
                        .tablePartition(TablePartition.refByQualifiedName(parentQualifiedName));
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
        return Column._internal().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Column", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Column.
     *
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

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
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the Column
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Column, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Column updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
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
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName of the Column
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Column updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
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
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the updated Column, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Column removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
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
     * @param qualifiedName for the Column
     * @param name human-readable name of the Column
     * @param terms the list of terms to replace on the Column, or null to remove all terms from the Column
     * @return the Column that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Column replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
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
     * @param qualifiedName for the Column
     * @param terms the list of terms to append to the Column
     * @return the Column that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Column appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
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
     */
    public static Column appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Column, without replacing all existing terms linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Column
     * @param terms the list of terms to remove from the Column, which must be referenced by GUID
     * @return the Column that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Column removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
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
     */
    public static Column removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Column, without replacing existing Atlan tags linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Column
     */
    public static Column appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
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
     */
    public static Column appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Column) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Column, without replacing existing Atlan tags linked to the Column.
     * Note: this operation must make two API calls — one to retrieve the Column's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Column
     */
    public static Column appendAtlanTags(
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
     */
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
     * Add Atlan tags to a Column.
     *
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Column
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Column.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Column
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Column
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Column.
     *
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Column
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
     * Add Atlan tags to a Column.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Column
     * @param qualifiedName of the Column
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Column
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
     * Remove an Atlan tag from a Column.
     *
     * @param qualifiedName of the Column
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Column
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Column.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Column
     * @param qualifiedName of the Column
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Column
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
