/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class Column extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Column";

    /** Fixed typeName for Columns. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of the column. */
    @Attribute
    String dataType;

    /** TBC */
    @Attribute
    String subDataType;

    /** Order in which the column appears in the table (starting at 1). */
    @Attribute
    Integer order;

    /** TBC */
    @Attribute
    Boolean isPartition;

    /** TBC */
    @Attribute
    Integer partitionOrder;

    /** TBC */
    @Attribute
    Boolean isClustered;

    /** When true, this column is the primary key for the table. */
    @Attribute
    Boolean isPrimary;

    /** When true, this column is a foreign key to another table. */
    @Attribute
    Boolean isForeign;

    /** When true, this column is indexed in the database. */
    @Attribute
    Boolean isIndexed;

    /** TBC */
    @Attribute
    Boolean isSort;

    /** TBC */
    @Attribute
    Boolean isDist;

    /** TBC */
    @Attribute
    Boolean isPinned;

    /** TBC */
    @Attribute
    String pinnedBy;

    /** TBC */
    @Attribute
    Long pinnedAt;

    /** Total number of digits allowed when the {@link #dataType} is numeric. */
    @Attribute
    Integer precision;

    /** TBC */
    @Attribute
    String defaultValue;

    /** When true, the values in this column can be null. */
    @Attribute
    Boolean isNullable;

    /** TBC */
    @Attribute
    Double numericScale;

    /** Maximum length of a value in this column. */
    @Attribute
    Long maxLength;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> validations;

    /** TBC */
    @Attribute
    Integer columnDistinctValuesCount;

    /** TBC */
    @Attribute
    Long columnDistinctValuesCountLong;

    /** TBC */
    @Attribute
    Double columnMax;

    /** TBC */
    @Attribute
    Double columnMin;

    /** TBC */
    @Attribute
    Double columnMean;

    /** TBC */
    @Attribute
    Double columnSum;

    /** TBC */
    @Attribute
    Double columnMedian;

    /** TBC */
    @Attribute
    Double columnStandardDeviation;

    /** TBC */
    @Attribute
    Integer columnUniqueValuesCount;

    /** TBC */
    @Attribute
    Long columnUniqueValuesCountLong;

    /** TBC */
    @Attribute
    Double columnAverage;

    /** TBC */
    @Attribute
    Double columnAverageLength;

    /** TBC */
    @Attribute
    Integer columnDuplicateValuesCount;

    /** TBC */
    @Attribute
    Long columnDuplicateValuesCountLong;

    /** TBC */
    @Attribute
    Integer columnMaximumStringLength;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> columnMaxs;

    /** TBC */
    @Attribute
    Integer columnMinimumStringLength;

    /** TBC */
    @Attribute
    @Singular
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
    Double columnUniquenessPercentage;

    /** TBC */
    @Attribute
    Double columnVariance;

    /** Materialized view in which this column exists, or null if the column exists in a table or view. */
    @Attribute
    @JsonProperty("materialisedView")
    MaterializedView materializedView;

    /** Queries that involve this column. */
    @Attribute
    @Singular
    SortedSet<AtlanQuery> queries;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Metric> metricTimestamps;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtMetric> dbtMetrics;

    /** View in which this column exists, or null if the column exists in a table or materialized view. */
    @Attribute
    View view;

    /** TBC */
    @Attribute
    TablePartition tablePartition;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Metric> dataQualityMetricDimensions;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtModelColumn> dbtModelColumns;

    /** Table in which this column exists, or null if the column exists in a view. */
    @Attribute
    Table table;

    /**
     * Retrieve the parent of this column, irrespective of its type.
     * @return the reference to this column's parent
     */
    public SQL getParent() {
        if (table != null) {
            return table;
        } else if (view != null) {
            return view;
        } else if (materializedView != null) {
            return materializedView;
        }
        return null;
    }

    /**
     * Reference to a Column by GUID.
     *
     * @param guid the GUID of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByGuid(String guid) {
        return Column.builder().guid(guid).build();
    }

    /**
     * Reference to a Column by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Column to reference
     * @return reference to a Column that can be used for defining a relationship to a Column
     */
    public static Column refByQualifiedName(String qualifiedName) {
        return Column.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a column.
     *
     * @param name of the column
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param parentQualifiedName unique name of the table / view / materialized view in which this column exists
     * @param order the order the column appears within its parent (the column's position)
     * @return the minimal request necessary to create the column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(String name, String parentType, String parentQualifiedName, int order) {
        String[] tokens = parentQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        ColumnBuilder<?, ?> builder = Column.builder()
                .name(name)
                .qualifiedName(parentQualifiedName + "/" + name)
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .order(order);
        switch (parentType) {
            case Table.TYPE_NAME:
                builder = builder.tableName(parentName)
                        .tableQualifiedName(parentQualifiedName)
                        .table(Table.refByQualifiedName(parentQualifiedName));
                break;
            case View.TYPE_NAME:
                builder = builder.viewName(parentName)
                        .viewQualifiedName(parentQualifiedName)
                        .view(View.refByQualifiedName(parentQualifiedName));
                break;
            case MaterializedView.TYPE_NAME:
                builder = builder.viewName(parentName)
                        .viewQualifiedName(parentQualifiedName)
                        .materializedView(MaterializedView.refByQualifiedName(parentQualifiedName));
                break;
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update a Column.
     *
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the minimal request necessary to update the Column, as a builder
     */
    public static ColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return Column.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Column, from a potentially
     * more-complete Column object.
     *
     * @return the minimal object necessary to update the Column, as a builder
     */
    @Override
    protected ColumnBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Column by its GUID, complete with all of its relationships.
     *
     * @param guid of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist or the provided GUID is not a Column
     */
    public static Column retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Column) {
            return (Column) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Column.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Column by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Column to retrieve
     * @return the requested full Column, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Column does not exist
     */
    public static Column retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Column) {
            return (Column) entity;
        } else {
            throw new NotFoundException(
                    "No Column found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (Column)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
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
        return (Column) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
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
        return (Column)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
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
    public static Column updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Column) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (Column)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (Column) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (Column)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Column.
     *
     * @param qualifiedName of the Column
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Column
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Column.
     *
     * @param qualifiedName of the Column
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Column
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static Column replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Column) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static Column appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Column) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static Column removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Column) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
