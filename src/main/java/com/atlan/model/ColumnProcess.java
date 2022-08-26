/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a column-level process in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnProcess extends AbstractProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ColumnProcess";

    /** Fixed typeName for column processes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Parent process through which this column-level process runs. */
    @Attribute
    Reference process;

    /**
     * Builds the minimal object necessary to create a column-level process.
     *
     * @param name of the column-level process
     * @param connectorName name of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs columns of data the process reads from
     * @param outputs columns of data the process writes to
     * @return the minimal object necessary to create the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> creator(
            String name,
            String connectorName,
            String connectionName,
            String connectionQualifiedName,
            List<Reference> inputs,
            List<Reference> outputs) {
        return ColumnProcess.builder()
                .qualifiedName(generateQualifiedName(
                        name, connectorName, connectionName, connectionQualifiedName, inputs, outputs, null))
                .name(name)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal object necessary to update a column-level process.
     *
     * @param qualifiedName unique name of the column-level process
     * @param name human-readable name of the column-level process
     * @return the minimal object necessary to update the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return ColumnProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a column-level process, from a potentially
     * more-complete column-level process object.
     *
     * @return the minimal object necessary to update the column-level process, as a builder
     */
    @Override
    protected ColumnProcessBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }
}
