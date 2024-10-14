/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.graph;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IModel;
import com.atlan.model.assets.ModelAttribute;
import com.atlan.model.assets.ModelDataModel;
import com.atlan.model.assets.ModelEntity;
import com.atlan.model.assets.ModelEntityAssociation;
import com.atlan.model.assets.ModelVersion;
import com.atlan.model.fields.AtlanField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

/**
 * Structure through which various model assets can all be traversed.
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ModelGraph {
    private ModelDataModel model;
    private ModelVersion version;

    @Singular
    private List<ModelEntityGraph> entities;

    private long asOfTime;

    private static final List<AtlanField> INCLUDES = List.of(
            ModelEntity.MODEL_ENTITY_MAPPED_TO_ENTITIES,
            ModelEntity.MODEL_ENTITY_MAPPED_FROM_ENTITIES,
            ModelAttribute.MODEL_ATTRIBUTE_DATA_TYPE,
            ModelAttribute.MODEL_ATTRIBUTE_IS_NULLABLE,
            ModelAttribute.MODEL_ATTRIBUTE_IS_PRIMARY,
            ModelAttribute.MODEL_ATTRIBUTE_IS_FOREIGN,
            ModelAttribute.MODEL_ATTRIBUTE_IS_DERIVED,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_CARDINALITY,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_LABEL,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_TO_QUALIFIED_NAME,
            ModelEntityAssociation.MODEL_ENTITY_ASSOCIATION_FROM_QUALIFIED_NAME);

    /**
     * Construct a model graph from the provided parameters.
     *
     * @param client connectivity to the Atlan tenant
     * @param time business date for which to retrieve the model graph
     * @param prefix (optional) scope of the model graph to retrieve
     * @return the model graph for the provided parameters
     * @throws AtlanException on any issues communicating with the underlying APIs
     */
    public static ModelGraph from(AtlanClient client, long time, String prefix) throws AtlanException {
        List<Asset> assets = IModel.findByTime(client, time, prefix, INCLUDES);
        ModelGraphBuilder builder = builder();
        Map<String, ModelEntityGraph.ModelEntityGraphBuilder> eg = new HashMap<>();
        assets.forEach(it -> {
            if (it instanceof ModelDataModel dm) {
                builder.model(dm);
            } else if (it instanceof ModelVersion mv) {
                builder.version(mv);
            } else if (it instanceof ModelEntity me) {
                if (!eg.containsKey(me.getModelVersionAgnosticQualifiedName())) {
                    eg.put(me.getModelVersionAgnosticQualifiedName(), ModelEntityGraph.builder());
                }
                eg.get(me.getModelVersionAgnosticQualifiedName()).details(me);
            } else if (it instanceof ModelAttribute ma) {
                if (!eg.containsKey(ma.getModelEntityQualifiedName())) {
                    eg.put(ma.getModelEntityQualifiedName(), ModelEntityGraph.builder());
                }
                eg.get(ma.getModelEntityQualifiedName()).attribute(ma);
            } else if (it instanceof ModelEntityAssociation mea) {
                String from = mea.getModelEntityAssociationFromQualifiedName();
                String to = mea.getModelEntityAssociationToQualifiedName();
                if (!eg.containsKey(from)) {
                    eg.put(from, ModelEntityGraph.builder());
                }
                if (!eg.containsKey(to)) {
                    eg.put(to, ModelEntityGraph.builder());
                }
                eg.get(from)
                        .associatedTo(ModelEntityGraph.AssociatedEntity.builder()
                                .entity(ModelEntity.refByQualifiedName(to))
                                .cardinality(mea.getModelEntityAssociationCardinality())
                                .label(mea.getModelEntityAssociationLabel())
                                .build());
                eg.get(to)
                        .associatedFrom(ModelEntityGraph.AssociatedEntity.builder()
                                .entity(ModelEntity.refByQualifiedName(from))
                                .cardinality(mea.getModelEntityAssociationCardinality())
                                .label(mea.getModelEntityAssociationLabel())
                                .build());
            }
        });
        return builder.entities(eg.values().stream()
                        .map(ModelEntityGraph.ModelEntityGraphBuilder::build)
                        .toList())
                .build();
    }
}
