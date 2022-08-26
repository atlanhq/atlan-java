/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about SQL-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class SQLJ extends AssetJ {

    public static final String TYPE_NAME = "SQL";

    /**
     * TBC
     */
    @Attribute
    Long queryCount;

    /**
     * TBC
     */
    @Attribute
    Long queryUserCount;

    /**
     * Unused attribues.
     */
    @JsonIgnore
    Map<String, Long> queryUserMap;

    /**
     * Time (epoch) at which the query count was last updated, in milliseconds.
     */
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
}
