/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.StructDef;
import com.atlan.util.StringUtils;
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
    private List<String> mapContainers = null;

    @SuppressWarnings("this-escape")
    public StructGenerator(AtlanClient client, StructDef structDef, GeneratorConfig cfg) {
        super(client, structDef, cfg);
        this.structDef = structDef;
        resolveClassName();
        super.description = cache.getTypeDescription(originalName);
        resolveAttributes();
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(getOriginalName());
    }

    private void resolveAttributes() {
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : structDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(client, className, attributeDef, cfg);
            if (className.equals("BadgeCondition") && attribute.getRenamed().equals("badgeConditionOperator")) {
                attribute.setType(MappedType.builder()
                        .type(MappedType.Type.ENUM)
                        .name("BadgeComparisonOperator")
                        .build());
            }
            attributes.add(attribute);
            checkAndAddMapContainer(attribute);
        }
    }

    private void checkAndAddMapContainer(Attribute attribute) {
        if (attribute.getType().getContainer() != null
                && attribute.getType().getContainer().contains("Map")) {
            if (mapContainers == null) {
                mapContainers = new ArrayList<>();
            }
            mapContainers.add(attribute.getRenamed());
        }
    }

    @Getter
    public static final class Attribute extends AttributeGenerator {

        public Attribute(AtlanClient client, String className, AttributeDef attributeDef, GeneratorConfig cfg) {
            super(client, className, attributeDef, cfg);
        }

        @Override
        protected void resolveName() {
            super.resolveName();
            setRenamed(cfg.resolveAttributeName(getOriginalName()));
            setSnakeCaseRenamed(StringUtils.getLowerSnakeCase(getRenamed()));
        }

        @Override
        protected void resolveType(AttributeDef attributeDef) {
            super.resolveType(attributeDef);
            if (attributeDef.getTypeName().startsWith("array<")) {
                // Always use lists in structs, not sorted sets
                String originalContainer = getType().getContainer();
                String revisedContainer = originalContainer.replaceAll("SortedSet<", "List<");
                setType(getType().toBuilder().container(revisedContainer).build());
            }
        }
    }
}
