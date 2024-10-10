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
import com.atlan.model.assets.ModelVersion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Asset> assets = IModel.findByTime(client, time, prefix);
        ModelGraphBuilder builder = builder();
        Map<String, ModelEntityGraph.ModelEntityGraphBuilder> eg = new HashMap<>();
        assets.forEach(it -> {
            if (it instanceof ModelDataModel m) {
                builder.model(m);
            } else if (it instanceof ModelVersion v) {
                builder.version(v);
            } else if (it instanceof ModelEntity e) {
                if (!eg.containsKey(e.getModelVersionAgnosticQualifiedName())) {
                    eg.put(e.getModelVersionAgnosticQualifiedName(), ModelEntityGraph.builder());
                }
                eg.get(e.getModelVersionAgnosticQualifiedName()).details(e);
            } else if (it instanceof ModelAttribute a) {
                if (!eg.containsKey(a.getModelEntityQualifiedName())) {
                    eg.put(a.getModelEntityQualifiedName(), ModelEntityGraph.builder());
                }
                eg.get(a.getModelEntityQualifiedName()).attribute(a);
            }
        });
        ;
        return builder
            .entities(eg.values().stream().map(ModelEntityGraph.ModelEntityGraphBuilder::build).toList())
            .build();
    }
}
