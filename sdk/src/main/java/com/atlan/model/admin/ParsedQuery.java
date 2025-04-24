/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Response containing the details of a parsed SQL query.
 */
@Getter
@Jacksonized
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class ParsedQuery extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** All the database objects detected in the query. */
    @JsonProperty("dbobjs")
    List<DatabaseObject> objects;

    /** All the relationship objects detected in the query. */
    List<Relationship> relationships;

    /** Any errors during parsing. */
    List<ParseError> errors;

    /** Details about a database object in a query. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class DatabaseObject extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /**
         * Fully-qualified name of the SQL object. (Only present on non-process objects.)
         */
        String displayName;

        /** Numeric identifier for the object. */
        String id;

        /** Name of the object (unqualified). */
        String name;

        /** Type of the object. */
        String type;

        /** Name of the database the object exists within. */
        String database;

        /** Name of the schema the object exists within. */
        String schema;

        /**
         * List of details about the columns queried within the object. (Only present on non-process objects.)
         */
        List<DatabaseColumn> columns;

        /**
         * Name of the procedure (only for process objects).
         */
        String procedureName;

        /**
         * Unique hash representing the query (only for process objects).
         */
        String queryHashId;
    }

    /** Details about relationships found in a query. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Relationship extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Numeric identifier for the relationship. */
        String id;

        /** Type of the relationship. */
        String type;

        /** Type of effect made by the query (for example, select vs insert). */
        String effectType;

        /** TBC */
        RelationshipEndpoint target;

        /** TBC */
        List<RelationshipEndpoint> sources;

        /** Numeric identifier for the procedure (if any) that manages this relationship. */
        String processId;

        /** Type of procedure (if any) that manages this relationship. */
        String processType;
    }

    /** Details about a column accessed by a query. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class DatabaseColumn extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Numeric identifier for the column. */
        String id;

        /** Name of the column (unqualified). */
        String name;

        /** TBC */
        String source;
    }

    /** Details about one end of a relationship in a query. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class RelationshipEndpoint extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Numeric identifier for the column referred to by this end of the relationship. */
        String id;

        /** Name of the column used by this end of the relationship. */
        String column;

        /** Numeric identifier of the parent object in which the column exists. */
        String parentId;

        /** Name of the parent object in which the column exists. */
        String parentName;
    }

    /** Details of any errors during query parsing. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ParseError extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Description of the error. */
        String errorMessage;

        /** Type of the error. */
        String errorType;

        /** TBC */
        List<Object> coordinates;
    }
}
