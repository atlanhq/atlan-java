/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.graph;

import com.atlan.model.assets.ModelAttribute;
import com.atlan.model.assets.ModelEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

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
}
