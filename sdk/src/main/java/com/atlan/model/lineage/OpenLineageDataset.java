/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.model.core.AtlanObject;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Base class for handling OpenLineage datasets, passing through to the OpenLineage Java SDK
 * but wrapping datasets such that they are handled appropriately in the Atlan Java SDK.
 */
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OpenLineageDataset extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Internal definition of the OpenLineage dataset. */
    @Getter(AccessLevel.PACKAGE)
    OpenLineage.Dataset _dataset;

    /** Producer definition for OpenLineage. */
    protected OpenLineage openLineage;

    /**
     * Name of the source where the dataset exists.
     * See: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md
     */
    private String namespace;

    /**
     * Name of the asset, by OpenLineage standards.
     * For example: DB.SCHEMA.TABLE
     */
    private String assetName;

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset.
     * Note: before you can use the dataset, you will need to wire it as either an input or output
     * using either {@code toInput()} or {@code toOutput()}.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @param producer a pre-configured OpenLineage producer
     * @return the minimal request necessary to create the dataset, as a builder
     */
    static OpenLineageDatasetBuilder<?, ?> creator(String namespace, String assetName, OpenLineage producer) {
        return _internal().openLineage(producer).namespace(namespace).assetName(assetName);
    }

    // TODO: provide some intuitive way to manage the facets of the dataset

    // TODO: provide some way to manage a schema, i.e. for partial assets
    // NOTE: If we want a structured partial asset, then we need the following lines, too
    //  (not needed for a "full" asset as they'll already be in Atlan)
    /*.dataSource(ol.newDatasourceDatasetFacetBuilder()
        .name("snowflake://qia75894.snowflakecomputing.com")
        .uri(URI.create("snowflake://qia75894.snowflakecomputing.com"))
        .build())
    .schema(ol.newSchemaDatasetFacetBuilder()
        .fields(listOf(
            ol.newSchemaDatasetFacetFieldsBuilder().name("ID").type("VARCHAR").build(),
            ol.newSchemaDatasetFacetFieldsBuilder().name("PARENT_ID").type("VARCHAR").build(),
            ol.newSchemaDatasetFacetFieldsBuilder().name("ORG_WIDE_EMAIL_ADDRESS_ID").type("VARCHAR").build(),
            ol.newSchemaDatasetFacetFieldsBuilder().name("SYSTEM_MODSTAMP").type("TIMESTAMPTZ").build(),
            ol.newSchemaDatasetFacetFieldsBuilder().name("_FIVETRAN_DELETED").type("BOOLEAN").build(),
            ol.newSchemaDatasetFacetFieldsBuilder().name("_FIVETRAN_SYNCED").type("TIMESTAMPTZ").build(),
        ))
        .build())
    .build())*/

    /** {@inheritDoc} */
    @Override
    public String toJson(AtlanClient client) {
        return OpenLineageClientUtils.toJson(get_dataset());
    }

    public abstract static class OpenLineageDatasetBuilder<
                    C extends OpenLineageDataset, B extends OpenLineageDataset.OpenLineageDatasetBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {

        /**
         * Create an input dataset from this builder.
         *
         * @return the dataset as an input dataset.
         */
        public OpenLineageInputDataset.OpenLineageInputDatasetBuilder<?, ?> toInput() {
            return OpenLineageInputDataset.creator(namespace, assetName, openLineage);
        }

        /**
         * Create an output dataset from this builder.
         *
         * @return the dataset as an output dataset.
         */
        public OpenLineageOutputDataset.OpenLineageOutputDatasetBuilder<?, ?> toOutput() {
            return OpenLineageOutputDataset.creator(namespace, assetName, openLineage);
        }
    }
}
