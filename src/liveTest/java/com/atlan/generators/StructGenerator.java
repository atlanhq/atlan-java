/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.StructDef;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class StructGenerator extends TypeGenerator {

    public static final String DIRECTORY = ""
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "structs";

    private static final Map<String, String> CLASS_RENAMING = Map.ofEntries();

    private final StructDef structDef;
    private List<Attribute> attributes;

    public StructGenerator(StructDef structDef) {
        super(structDef);
        this.structDef = structDef;
        resolveClassName();
        super.description = AttributeCSVCache.getTypeDescription(originalName);
        resolveAttributes();
    }

    @Override
    protected void resolveClassName() {
        super.className = CLASS_RENAMING.containsKey(originalName)
                ? CLASS_RENAMING.get(originalName)
                : getUpperCamelCase(originalName);
    }

    private void resolveAttributes() {
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : structDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(className, attributeDef);
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
    public static final class Attribute {

        @Setter(AccessLevel.PRIVATE)
        private MappedType type;

        private final String originalName;
        private final String renamed;
        private final String description;

        public Attribute(String className, AttributeDef attributeDef) {
            this.type = getMappedType(attributeDef.getTypeName());
            this.originalName =
                    attributeDef.getDisplayName() == null ? attributeDef.getName() : attributeDef.getDisplayName();
            this.renamed = getLowerCamelCase(originalName);
            this.description = AttributeCSVCache.getAttributeDescription(className, originalName);
        }
    }
}
