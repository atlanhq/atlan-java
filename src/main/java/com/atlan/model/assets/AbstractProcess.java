/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representation of a process in Atlan, with its detailed information. This class is not intended for direct
 * use, and is thus abstract.
 * @see LineageProcess for table-level processes
 * @see ColumnProcess for column-level processes
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BIProcess.class, name = BIProcess.TYPE_NAME),
    @JsonSubTypes.Type(value = DbtProcess.class, name = DbtProcess.TYPE_NAME),
    @JsonSubTypes.Type(value = ColumnProcess.class, name = ColumnProcess.TYPE_NAME),
    @JsonSubTypes.Type(value = LineageProcess.class, name = LineageProcess.TYPE_NAME),
})
public abstract class AbstractProcess extends Asset {

    public static final String TYPE_NAME = "Process";

    /** Code that ran within the process. */
    @Attribute
    String code;

    /** SQL query that ran to produce the outputs. */
    @Attribute
    String sql;

    /** TBC */
    @Attribute
    String ast;

    /** Assets that are outputs from this process. */
    @Attribute
    @Singular
    SortedSet<Catalog> outputs;

    /** Assets that are inputs to this process. */
    @Attribute
    @Singular
    SortedSet<Catalog> inputs;

    /** Processes that detail column-level lineage for this process. */
    @Attribute
    @Singular
    SortedSet<AbstractColumnProcess> columnProcesses;

    /**
     * Generate a unique qualifiedName for a process.
     * @param name of the process
     * @param connectorType type of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @return unique name for the process
     */
    public static String generateQualifiedName(
            String name,
            AtlanConnectorType connectorType,
            String connectionName,
            String connectionQualifiedName,
            List<Catalog> inputs,
            List<Catalog> outputs,
            LineageProcess parent) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(connectorType.getValue()).append(connectionName).append(connectionQualifiedName);
        if (parent != null) {
            appendRelationship(sb, parent);
        }
        appendRelationships(sb, inputs);
        appendRelationships(sb, outputs);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sb.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Unable to generate the qualifiedName for the process: MD5 algorithm does not exist on your platform!");
        }
    }

    /**
     * Append all the relationships into the provided string builder.
     * @param sb into which to append
     * @param relationships to append
     */
    private static void appendRelationships(StringBuilder sb, List<Catalog> relationships) {
        for (Catalog relationship : relationships) {
            appendRelationship(sb, relationship);
        }
    }

    /**
     * Append a single relationship into the provided string builder.
     * @param sb into which to append
     * @param relationship to append
     */
    private static void appendRelationship(StringBuilder sb, Asset relationship) {
        // TODO: if two calls are made for the same process, but one uses GUIDs for
        //  its references and the other uses qualifiedName, we'll end up with different
        //  hashes (duplicate processes)
        if (relationship.getGuid() != null) {
            sb.append(relationship.getGuid());
        } else if (relationship.getUniqueAttributes() != null
                && relationship.getUniqueAttributes().getQualifiedName() != null) {
            sb.append(relationship.getUniqueAttributes().getQualifiedName());
        }
    }
}
