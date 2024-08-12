/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public abstract class RelationshipAttributes extends AtlanObject {
    private static final long serialVersionUID = 2L;

    protected RelationshipAttributes() {
        // Do nothing
    }

    @JsonIgnore
    public abstract Map<String, Object> getAll();
}
