/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.ReferenceJ;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a process in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LineageProcessJ extends AbstractProcessJ {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Process";

    /** Fixed typeName for processes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Sub-processes giving column-level lineage for this process. */
    @Singular
    @Attribute
    List<ReferenceJ> columnProcesses;

    /**
     * Builds the minimal object necessary to create a process.
     *
     * @param name of the process
     * @param connectorName name of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @return the minimal object necessary to create the process, as a builder
     */
    public static LineageProcessJBuilder<?, ?> creator(
            String name,
            String connectorName,
            String connectionName,
            String connectionQualifiedName,
            List<ReferenceJ> inputs,
            List<ReferenceJ> outputs) {
        return LineageProcessJ.builder()
                .qualifiedName(generateQualifiedName(
                        name, connectorName, connectionName, connectionQualifiedName, inputs, outputs, null))
                .name(name)
                .connectorName(connectorName)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal object necessary to update a process.
     *
     * @param qualifiedName unique name of the process
     * @param name human-readable name of the process
     * @return the minimal object necessary to update the process, as a builder
     */
    public static LineageProcessJBuilder<?, ?> updater(String qualifiedName, String name) {
        return LineageProcessJ.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a connection, from a potentially
     * more-complete connection object.
     *
     * @return the minimal object necessary to update the connection, as a builder
     */
    @Override
    protected LineageProcessJBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }
}
