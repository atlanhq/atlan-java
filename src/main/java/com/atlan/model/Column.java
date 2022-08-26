/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a column in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Column extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Column";

    /** Fixed typeName for tables. */
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
    Float numericScale;

    /** Maximum length of a value in this column. */
    @Attribute
    Long maxLength;

    /** Unused attributes. */
    @JsonIgnore
    Map<String, String> validations;

    /** Table in which this column exists, or null if the column exists in a view. */
    @Attribute
    Reference table;

    /** Table partition in which this column exists, if any. */
    @Attribute
    Reference tablePartition;

    /** View in which this column exists, or null if the column exists in a table or materialized view. */
    @Attribute
    Reference view;

    /** Materialized view in which this column exists, or null if the column exists in a table or view. */
    @Attribute
    @JsonProperty("materialisedView")
    Reference materializedView;

    /** Queries that involve this column. */
    @Singular
    @Attribute
    Set<Reference> queries;

    /**
     * Retrieve the parent of this column, irrespective of its type.
     * @return the reference to this column's parent
     */
    public Reference getParent() {
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
     * Builds the minimal object necessary to create a column.
     *
     * @param name of the column
     * @param connectorName name of the connector (software / system) that hosts the column
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param parentName name of parent table / view / materialized view in which this column exists
     * @param parentQualifiedName unique name of the table / view / materialized view in which this column exists
     * @param schemaName name of schema in which this column exists
     * @param schemaQualifiedName unique name of the schema in which this column exists
     * @param databaseName name of database in which this column exists
     * @param databaseQualifiedName unique name of the database in which this column exists
     * @param connectionQualifiedName unique name of the specific instance of the software / system that hosts the column
     * @return the minimal request necessary to create the column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(
            String name,
            String connectorName,
            String parentType,
            String parentName,
            String parentQualifiedName,
            String schemaName,
            String schemaQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String connectionQualifiedName) {
        // TODO: can we simplify the argument list by just taking qualifiedNames and extracting the
        //  non-qualifiedNames from these?
        ColumnBuilder<?, ?> builder = Column.builder()
                .name(name)
                .qualifiedName(parentQualifiedName + "/" + name)
                .connectorName(connectorName)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
        if (parentType.equals(Table.TYPE_NAME)) {
            builder = builder.tableName(parentName).tableQualifiedName(parentQualifiedName);
        }
        // TODO: handle other parent types (view, materialized view)
        return builder;
    }

    /**
     * Builds the minimal object necessary to update a column.
     *
     * @param qualifiedName of the column
     * @param name of the column
     * @return the minimal request necessary to update the column, as a builder
     */
    public static ColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return Column.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a column, from a potentially
     * more-complete column object.
     *
     * @return the minimal object necessary to update the column, as a builder
     */
    @Override
    protected ColumnBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }
}
