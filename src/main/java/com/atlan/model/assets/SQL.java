/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about SQL-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TablePartition.class, name = TablePartition.TYPE_NAME),
    @JsonSubTypes.Type(value = Table.class, name = Table.TYPE_NAME),
    @JsonSubTypes.Type(value = AtlanQuery.class, name = AtlanQuery.TYPE_NAME),
    @JsonSubTypes.Type(value = Column.class, name = Column.TYPE_NAME),
    @JsonSubTypes.Type(value = Schema.class, name = Schema.TYPE_NAME),
    @JsonSubTypes.Type(value = Database.class, name = Database.TYPE_NAME),
    @JsonSubTypes.Type(value = SnowflakeStream.class, name = SnowflakeStream.TYPE_NAME),
    @JsonSubTypes.Type(value = SnowflakePipe.class, name = SnowflakePipe.TYPE_NAME),
    @JsonSubTypes.Type(value = Procedure.class, name = Procedure.TYPE_NAME),
    @JsonSubTypes.Type(value = View.class, name = View.TYPE_NAME),
    @JsonSubTypes.Type(value = MaterializedView.class, name = MaterializedView.TYPE_NAME),
})
@SuppressWarnings("cast")
public abstract class SQL extends Catalog {

    public static final String TYPE_NAME = "SQL";

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    Long queryCountUpdatedAt;

    /**
     * Name of the database in which this SQL asset exists, or null if it does not
     * exist within a database.
     */
    @Attribute
    String databaseName;

    /**
     * Unique name of the database in which this SQL asset exists, or null if it does not
     * exist within a database.
     */
    @Attribute
    String databaseQualifiedName;

    /**
     * Name of the schema in which this SQL asset exists, or null if it does not
     * exist within a schema.
     */
    @Attribute
    String schemaName;

    /**
     * Unique name of the schema in which this SQL asset exists, or null if it does not
     * exist within a schema.
     */
    @Attribute
    String schemaQualifiedName;

    /**
     * Name of the table in which this SQL asset exists, or null if it does not
     * exist within a table. Only tableName or viewName should be populated.
     */
    @Attribute
    String tableName;

    /**
     * Unique name of the table in which this SQL asset exists, or null if it does not
     * exist within a table. Only tableQualifiedName or viewQualifiedName should be populated.
     */
    @Attribute
    String tableQualifiedName;

    /**
     * Name of the view in which this SQL asset exists, or null if it does not
     * exist within a view. Only viewName or tableName should be populated.
     */
    @Attribute
    String viewName;

    /**
     * Unique name of the view in which this SQL asset exists, or null if it does not
     * exist within a view. Only viewQualifiedName or tableQualifiedName should be populated.
     */
    @Attribute
    String viewQualifiedName;

    /** Whether the asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Time (epoch) at which the asset was last profiled, in milliseconds. */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtSource> dbtSources;
}
