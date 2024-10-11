/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.graph;

import com.atlan.model.assets.IModelEntity;
import com.atlan.model.assets.ModelAttribute;
import com.atlan.model.assets.ModelEntity;
import com.atlan.model.enums.ModelCardinalityType;
import java.util.List;
import java.util.SortedSet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

/**
 * Structure through which a model entity and its subcomponents (attributes) can be traversed.
 */
@Getter
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class ModelEntityGraph {
    private ModelEntity details;

    @Singular
    private List<ModelAttribute> attributes;

    @Singular
    private List<AssociatedEntity> associatedTos;

    @Singular
    private List<AssociatedEntity> associatedFroms;

    public SortedSet<IModelEntity> getMappedTo() {
        return details.getModelEntityMappedToEntities();
    }

    public SortedSet<IModelEntity> getMappedFrom() {
        return details.getModelEntityMappedFromEntities();
    }

    @Getter
    @Builder(access = AccessLevel.PACKAGE)
    @EqualsAndHashCode
    public static final class AssociatedEntity {
        private String label;
        private ModelEntity entity;
        private ModelCardinalityType cardinality;
    }
}
