/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class AttributeGenerator extends TypeGenerator {

    private MappedType type;
    private String renamed;
    private String snakeCaseRenamed;
    private boolean retyped = false;

    protected AttributeGenerator(AtlanClient client, GeneratorConfig cfg) {
        super(client, cfg);
    }

    @SuppressWarnings("this-escape")
    public AttributeGenerator(AtlanClient client, String className, AttributeDef attributeDef, GeneratorConfig cfg) {
        super(client, cfg);
        super.className = className;
        this.originalName =
                attributeDef.getDisplayName() == null ? attributeDef.getName() : attributeDef.getDisplayName();
        this.description = cache.getAttributeDescription(className, originalName);
        resolveName();
        resolveType(attributeDef);
    }

    @Override
    protected void resolveClassName() {
        // Nothing to do, already set by constructor
    }

    protected void resolveName() {
        this.renamed = StringUtils.getLowerCamelCase(getOriginalName());
        this.snakeCaseRenamed = StringUtils.getLowerSnakeCase(getOriginalName());
    }

    protected void resolveType(AttributeDef attributeDef) {
        this.type = getMappedType(attributeDef.getTypeName());
    }

    public String getFullType() {
        String fullType;
        String container = type.getContainer();
        if (container != null) {
            long nestingCount = container.chars().filter(c -> c == '<').count();
            fullType = container + type.getName() + ">".repeat((int) nestingCount);
        } else {
            fullType = type.getName();
        }
        return fullType;
    }

    public String getReferenceType() {
        String refType;
        String container = type.getContainer();
        String baseName = type.getName();
        if (type.getType() == MappedType.Type.ASSET) {
            baseName = "I" + baseName;
        }
        if (container != null) {
            long nestingCount = container.chars().filter(c -> c == '<').count();
            if (type.getType() == MappedType.Type.STRUCT && container.equals("Map<")) {
                refType = container + "String, " + type.getName() + ">".repeat((int) nestingCount);
            } else {
                refType = container + baseName + ">".repeat((int) nestingCount);
            }
        } else {
            refType = baseName;
        }
        return refType;
    }

    public String getSingular() {
        if (getType().getContainer() != null) {
            return cfg.resolveSingular(getOriginalName());
        }
        return null;
    }
}
