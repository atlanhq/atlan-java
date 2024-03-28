/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.TypeDef;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public abstract class TypeGenerator {

    protected static final Map<String, String> PRIMITIVE_MAPPINGS = Map.ofEntries(
            Map.entry("string", "String"),
            Map.entry("boolean", "Boolean"),
            Map.entry("int", "Integer"),
            Map.entry("long", "Long"),
            Map.entry("date", "Long"),
            Map.entry("float", "Double"),
            Map.entry("string,string", "String, String"),
            Map.entry("string,long", "String, Long"));

    protected String generatorName;
    protected String packageRoot;
    protected String originalName;
    protected String className;
    protected String description;
    protected GeneratorConfig cfg;
    protected ModelCache cache;

    protected TypeGenerator(GeneratorConfig cfg) {
        this.cfg = cfg;
        this.cache = ModelCache.getInstance();
        this.packageRoot = cfg.getPackageRoot();
        this.generatorName = cfg.getGeneratorName();
    }

    protected TypeGenerator(TypeDef typeDef, GeneratorConfig cfg) {
        this.originalName = typeDef.getDisplayName() == null ? typeDef.getName() : typeDef.getDisplayName();
        this.cfg = cfg;
        this.cache = ModelCache.getInstance();
        this.packageRoot = cfg.getPackageRoot();
        this.generatorName = cfg.getGeneratorName();
    }

    protected abstract void resolveClassName();

    protected MappedType getMappedType(String type) {
        // First look for contained types...
        String baseType = type;
        String container = null;
        if (type.contains("<")) {
            if (type.startsWith("array<")) {
                if (type.startsWith("array<map<")) {
                    baseType = getEmbeddedType(type.substring("array<".length(), type.length() - 1));
                    container = "List<Map<";
                } else {
                    baseType = getEmbeddedType(type);
                    container = "SortedSet<";
                }
            } else if (type.startsWith("map<")) {
                baseType = getEmbeddedType(type);
                container = "Map<";
            }
        }
        MappedType.MappedTypeBuilder builder = MappedType.builder().originalBase(baseType);
        // First try to map a primitive type
        String primitiveName = PRIMITIVE_MAPPINGS.getOrDefault(baseType, null);
        if (primitiveName != null) {
            builder.type(MappedType.Type.PRIMITIVE).name(primitiveName);
        } else {
            // Failing that, attempt to map to a cached type (enum, struct, etc)
            MappedType mappedType = cache.getCachedType(baseType);
            if (mappedType == null) {
                // Failing that, fall-back to just the name of the object
                builder.type(MappedType.Type.ASSET).name(baseType);
            } else {
                MappedType.Type baseTypeOfMapped = mappedType.getType();
                builder.type(baseTypeOfMapped).name(mappedType.getName());
                if (baseTypeOfMapped == MappedType.Type.STRUCT && container != null) {
                    // If the referred object is a struct, change the container to a list rather
                    // than a set
                    container = "List<";
                }
            }
        }
        if (container != null) {
            builder.container(container);
        }
        return builder.build();
    }

    /**
     * Determine the primitive type of the attribute when it's values are contained in an
     * array or map.
     *
     * @param attrType data type of the attribute
     * @return the primitive contained type of the attribute's values
     */
    private static String getEmbeddedType(String attrType) {
        return attrType.substring(attrType.indexOf("<") + 1, attrType.indexOf(">"));
    }

    @Getter
    @Builder(toBuilder = true)
    public static final class MappedType {

        public enum Type {
            PRIMITIVE,
            ENUM,
            STRUCT,
            ASSET
        }

        private String originalBase;
        private String name;
        private String container;
        private Type type;
    }
}
