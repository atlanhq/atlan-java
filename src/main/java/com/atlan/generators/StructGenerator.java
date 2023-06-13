/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.StructDef;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class StructGenerator extends TypeGenerator {

    public static final String DIRECTORY = "structs";

    private final StructDef structDef;
    private List<Attribute> attributes;

    public StructGenerator(StructDef structDef, GeneratorConfig cfg) {
        super(structDef, cfg);
        this.structDef = structDef;
        resolveClassName();
        super.description = AttributeCSVCache.getTypeDescription(originalName);
        resolveAttributes();
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(getOriginalName());
    }

    private void resolveAttributes() {
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : structDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(className, attributeDef, cfg);
            if (className.equals("BadgeCondition") && attribute.getRenamed().equals("badgeConditionOperator")) {
                attribute.setType(MappedType.builder()
                        .type(MappedType.Type.ENUM)
                        .name("BadgeComparisonOperator")
                        .build());
            }
            attributes.add(attribute);
        }
    }

    @Getter
    public static final class Attribute extends AttributeGenerator {

        public Attribute(String className, AttributeDef attributeDef, GeneratorConfig cfg) {
            super(className, attributeDef, cfg);
        }

        @Override
        protected void resolveType(AttributeDef attributeDef) {
            super.resolveType(attributeDef);
            if (attributeDef.getTypeName().startsWith("array<")) {
                // Always use lists in structs, not sorted sets
                setType(getType().toBuilder().container("List<").build());
            }
        }
    }
}
