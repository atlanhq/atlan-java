/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.EnumDef;
import com.atlan.model.typedefs.StructDef;
import com.atlan.model.typedefs.TypeDef;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public abstract class TypeGenerator {

    protected static final Map<String, String> PRIMITIVE_MAPPINGS = Map.ofEntries(
            Map.entry("string", "String"),
            Map.entry("boolean", "Boolean"),
            Map.entry("int", "Integer"),
            Map.entry("long", "Long"),
            Map.entry("date", "Long"),
            Map.entry("float", "Double"),
            Map.entry("double", "Double"),
            Map.entry("string,string", "String, String"),
            Map.entry("string,long", "String, Long"));

    protected String generatorName;
    protected String packageRoot;
    protected String originalName;
    protected String className;
    protected String description;
    protected GeneratorConfig cfg;
    protected ModelCache cache;
    protected AtlanClient client;

    protected TypeGenerator(AtlanClient client, GeneratorConfig cfg) {
        this.client = client;
        this.cfg = cfg;
        this.cache = ModelCache.getInstance(client);
        this.packageRoot = cfg.getPackageRoot();
        this.generatorName = cfg.getGeneratorName();
    }

    protected TypeGenerator(AtlanClient client, TypeDef typeDef, GeneratorConfig cfg) {
        this.client = client;
        this.originalName = typeDef.getDisplayName() == null ? typeDef.getName() : typeDef.getDisplayName();
        this.cfg = cfg;
        this.cache = ModelCache.getInstance(client);
        this.packageRoot = cfg.getPackageRoot();
        this.generatorName = cfg.getGeneratorName();
    }

    protected abstract void resolveClassName();

    protected MappedType getMappedType(String type) {
        // First look for contained types...
        String baseType = AttributeDef.getBasicType(type);
        String container = AttributeDef.getContainerType(type);
        MappedType.MappedTypeBuilder builder = MappedType.builder().originalBase(baseType);
        // First try to map a primitive type
        String primitiveName = PRIMITIVE_MAPPINGS.getOrDefault(baseType, null);
        if (primitiveName != null) {
            // Note: this will also handle primitive maps (string,string)
            builder.type(MappedType.Type.PRIMITIVE).name(primitiveName);
        } else if (baseType.startsWith("string,") && container.equals("Map<")) {
            // If we're here, we have a map<string,SomethingElse> non-primitive map
            String valueType = baseType.substring(baseType.indexOf(",") + 1);
            builder.type(MappedType.Type.STRUCT).name(valueType);
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

    public String getClassTemplateFile() {
        try {
            return cfg.getFreemarkerConfig().getTemplate(className + ".ftl").getSourceName();
        } catch (TemplateNotFoundException e) {
            // Do nothing - no template to load or otherwise handle
        } catch (IOException e) {
            log.error("Error reading template: {}.ftl", className, e);
        }
        return null;
    }

    public boolean isBuiltIn(String orgName, String reTyped) {
        if (orgName != null) {
            EntityDef entity = cache.getEntityDefCache().get(orgName);
            if (entity != null) {
                return (entity.getServiceType() != null
                        && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(entity.getServiceType()));
            } else {
                StructDef struct = cache.getStructDefCache().get(orgName);
                if (struct != null) {
                    return (struct.getServiceType() != null
                                    && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(struct.getServiceType()))
                            || GeneratorConfig.BUILT_IN_STRUCTS.contains(orgName);
                } else {
                    EnumDef enumDef = cache.getEnumDefCache().get(orgName);
                    return (enumDef != null
                                    && enumDef.getServiceType() != null
                                    && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(enumDef.getServiceType()))
                            || GeneratorConfig.BUILT_IN_ENUMS.contains(orgName)
                            || (orgName.equals("string") && GeneratorConfig.BUILT_IN_ENUMS.contains(reTyped));
                }
            }
        }
        return false;
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

        private String originalBase; // string,string ; string,SomeObjectDef ; etc
        private String name; // String ; SomeObjectDef ; etc
        private String container; // Map< ; List< ; etc
        private Type type; // PRIMITIVE ; ENUM ; STRUCT ; ASSET
    }
}
