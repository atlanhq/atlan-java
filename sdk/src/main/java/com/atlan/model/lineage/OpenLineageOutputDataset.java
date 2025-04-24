/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import io.openlineage.client.OpenLineage;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Base class for handling OpenLineage datasets to be used as lineage targets (outputs),
 * passing through to the OpenLineage Java SDK but wrapping datasets such that they are
 * handled appropriately in the Atlan Java SDK.
 */
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class OpenLineageOutputDataset extends OpenLineageDataset {
    private static final long serialVersionUID = 2L;

    private OpenLineage.OutputDatasetBuilder _builder;

    /**
     * Column-level lineage.
     * Each entry should be keyed by the name of a column in this output dataset, and the value
     * should be a list of all input datasets' fields that are used as input into the output column.
     */
    @Singular
    Map<String, List<OpenLineage.InputField>> toFields;

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset use-able as a lineage target.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @param producer a pre-configured OpenLineage producer
     * @return the minimal request necessary to create the job, as a builder
     */
    static OpenLineageOutputDatasetBuilder<?, ?> creator(String namespace, String assetName, OpenLineage producer) {
        return _internal()
                .openLineage(producer)
                ._builder(producer.newOutputDatasetBuilder()
                        .namespace(namespace)
                        .name(assetName)
                        .facets(producer.newDatasetFacetsBuilder().build()));
    }

    // TODO: provide some intuitive way to manage the facets of the dataset

    /**
     * Retrieve the OutputDataset in OpenLineage standard form.
     *
     * @return the OpenLineage standard for the OutputDataset
     */
    @Override
    OpenLineage.OutputDataset get_dataset() {
        if (toFields == null || toFields.isEmpty()) {
            return this._builder.build();
        } else {
            OpenLineage.ColumnLineageDatasetFacetFieldsBuilder fieldsBuilder =
                    openLineage.newColumnLineageDatasetFacetFieldsBuilder();
            for (Map.Entry<String, List<OpenLineage.InputField>> entry : toFields.entrySet()) {
                fieldsBuilder.put(
                        entry.getKey(),
                        openLineage
                                .newColumnLineageDatasetFacetFieldsAdditionalBuilder()
                                .inputFields(entry.getValue())
                                .build());
            }
            // TODO: below code will clobber any pre-existing facets
            return this._builder
                    .facets(openLineage
                            .newDatasetFacetsBuilder()
                            .columnLineage(openLineage
                                    .newColumnLineageDatasetFacetBuilder()
                                    .fields(fieldsBuilder.build())
                                    .build())
                            .build())
                    .build();
        }
    }
}
