/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
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
public class LineageProcess extends AbstractProcess {
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
    List<Reference> columnProcesses;

    /**
     * Builds the minimal object necessary to create a process.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling
     * additional methods to add metadata followed by {@link LineageProcessBuilder#build()}.
     *
     * @param name of the process
     * @param connectorName name of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @return the minimal object necessary to create the process
     */
    public static LineageProcess toCreate(
            String name,
            String connectorName,
            String connectionName,
            String connectionQualifiedName,
            List<Reference> inputs,
            List<Reference> outputs) {
        return LineageProcess.builder()
                .qualifiedName(generateQualifiedName(
                        name, connectorName, connectionName, connectionQualifiedName, inputs, outputs, null))
                .name(name)
                .connectorName(connectorName)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs)
                .build();
    }
}
