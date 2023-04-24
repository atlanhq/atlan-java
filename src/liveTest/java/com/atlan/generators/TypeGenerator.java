/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
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
            Map.entry("array<string>", "SortedSet<String>"),
            Map.entry("array<float>", "List<Double>"),
            Map.entry("map<string,string>", "Map<String, String>"),
            Map.entry("map<string,long>", "Map<String, Long>"),
            Map.entry("array<map<string,string>>", "List<Map<String, String>>"));

    protected String originalName;
    protected String className;
    protected String description;

    protected TypeGenerator(TypeDef typeDef) {
        this.originalName = typeDef.getDisplayName() == null ? typeDef.getName() : typeDef.getDisplayName();
    }

    protected abstract void resolveClassName();

    public boolean isRenamed() {
        return originalName != null && !originalName.equals(className);
    }

    protected static String getUpperCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            builder.append(word);
        }
        return builder.toString();
    }

    protected static String getLowerCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : Character.toLowerCase(word.charAt(0)) + word.substring(1);
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }
            builder.append(word);
        }
        return builder.toString();
    }

    protected static MappedType getMappedType(String type) {
        // First try to map a primitive type
        String primitiveName = PRIMITIVE_MAPPINGS.getOrDefault(type, null);
        if (primitiveName != null) {
            return MappedType.builder()
                    .type(MappedType.Type.PRIMITIVE)
                    .name(primitiveName)
                    .build();
        }
        // Failing that, attempt to map to a cached type (enum, struct, etc)
        MappedType mappedType = ModelGeneratorV2.getCachedType(type);
        if (mappedType == null) {
            // Failing that, fall-back to just the name of the object
            // TODO: will need more once we have entities, since they can even
            //  refer to themselves (before being cached)
            return MappedType.builder().type(MappedType.Type.ASSET).name(type).build();
        }
        return mappedType;
    }

    @Getter
    @Builder
    public static final class MappedType {

        public enum Type {
            PRIMITIVE,
            ENUM,
            STRUCT,
            ASSET
        }

        private String name;
        private Type type;
    }
}
