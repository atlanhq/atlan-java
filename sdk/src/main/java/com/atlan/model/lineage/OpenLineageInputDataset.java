/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import io.openlineage.client.OpenLineage;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Base class for handling OpenLineage datasets to be used as lineage sources (inputs),
 * passing through to the OpenLineage Java SDK but wrapping datasets such that they are
 * handled appropriately in the Atlan Java SDK.
 */
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OpenLineageInputDataset extends OpenLineageDataset {
    private static final long serialVersionUID = 2L;

    private OpenLineage.InputDatasetBuilder _builder;

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset use-able as a lineage source.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @param producer a pre-configured OpenLineage producer
     * @return the minimal request necessary to create the job, as a builder
     */
    static OpenLineageInputDatasetBuilder<?, ?> creator(String namespace, String assetName, OpenLineage producer) {
        return _internal()
                .openLineage(producer)
                ._builder(producer.newInputDatasetBuilder()
                        .namespace(namespace)
                        .name(assetName)
                        .facets(producer.newDatasetFacetsBuilder().build()));
    }

    // TODO: provide some intuitive way to manage the facets of the dataset

    /**
     * Retrieve the InputDataset in OpenLineage standard form.
     *
     * @return the OpenLineage standard for the InputDataset
     */
    @Override
    OpenLineage.InputDataset get_dataset() {
        if (_dataset == null) {
            _dataset = _builder.build();
        }
        return (OpenLineage.InputDataset) this._dataset;
    }

    /**
     * Create a new reference to a field within this input dataset.
     *
     * @param fieldName name of the field within the input dataset to reference
     * @return a reference to the field within this input dataset
     */
    public OpenLineage.InputFieldBuilder fromField(String fieldName) {
        return openLineage
                .newInputFieldBuilder()
                .namespace(get_dataset().getNamespace())
                .name(get_dataset().getName())
                .field(fieldName);
    }
}
