/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelCache {

    @Getter
    private final Map<String, EnumDef> enumDefCache;

    @Getter
    private final Map<String, StructDef> structDefCache;

    @Getter
    private final Map<String, EntityDef> entityDefCache;

    @Getter
    private final Map<String, RelationshipDef> relationshipDefCache;

    private final Map<String, EnumGenerator> enumCache = new HashMap<>();
    private final Map<String, StructGenerator> structCache = new HashMap<>();
    private final Map<String, AssetGenerator> assetCache = new HashMap<>();
    private final Map<String, Set<SearchFieldGenerator.Field>> searchCache = new HashMap<>();

    private final Map<String, Set<String>> uniqueRelationshipsForType = new ConcurrentHashMap<>();
    private final Map<String, String> subTypeToSuperType = new ConcurrentHashMap<>();
    private final Map<String, List<String>> subTypeToSuperTypes = new ConcurrentHashMap<>();

    private GeneratorConfig config = null;
    private static ModelCache INSTANCE = null;

    private ModelCache() throws AtlanException {
        enumDefCache = new ConcurrentHashMap<>();
        for (EnumDef enumDef :
                TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENUM).getEnumDefs()) {
            enumDefCache.put(enumDef.getName(), enumDef);
        }
        structDefCache = new ConcurrentHashMap<>();
        for (StructDef structDef :
                TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.STRUCT).getStructDefs()) {
            structDefCache.put(structDef.getName(), structDef);
        }
        entityDefCache = new ConcurrentHashMap<>();
        for (EntityDef entityDef :
                TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY).getEntityDefs()) {
            entityDefCache.put(entityDef.getName(), entityDef);
        }
        relationshipDefCache = new ConcurrentHashMap<>();
        for (RelationshipDef relationshipDef :
                TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.RELATIONSHIP).getRelationshipDefs()) {
            relationshipDefCache.put(relationshipDef.getName(), relationshipDef);
        }
    }

    private static ModelCache createInstance(GeneratorConfig cfg) {
        try {
            ModelCache cache = new ModelCache();
            cache.config = cfg;
            cache.cacheRelationshipsForInheritance(cache.getEntityDefCache().values());
            return cache;
        } catch (AtlanException e) {
            log.error("Unable to refresh typedef caches.", e);
            return null;
        }
    }

    public static ModelCache getInstance(GeneratorConfig cfg) {
        if (INSTANCE == null) {
            INSTANCE = createInstance(cfg);
        }
        return INSTANCE;
    }

    public EnumGenerator addEnumGenerator(String name, EnumGenerator generator) {
        return enumCache.put(name, generator);
    }

    public StructGenerator addStructGenerator(String name, StructGenerator generator) {
        return structCache.put(name, generator);
    }

    public AssetGenerator addAssetGenerator(String name, AssetGenerator generator) {
        return assetCache.put(name, generator);
    }

    public List<String> getStructNames() {
        return structCache.keySet().stream().sorted().collect(Collectors.toList());
    }

    public Collection<AssetGenerator> getAssetGenerators() {
        return assetCache.values();
    }

    public AssetGenerator getAssetGenerator(String entityDefName) {
        return assetCache.get(entityDefName);
    }

    public TypeGenerator.MappedType getCachedType(String typeName) {
        if (enumCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.ENUM)
                    .name(enumCache.get(typeName).getClassName())
                    .build();
        } else if (structCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.STRUCT)
                    .name(structCache.get(typeName).getClassName())
                    .build();
        } else if (assetCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.ASSET)
                    .name(assetCache.get(typeName).getClassName())
                    .build();
        }
        return null;
    }

    public AssetGenerator getCachedAssetType(String typeName) {
        return assetCache.get(typeName);
    }

    public Set<String> getUniqueRelationshipsForType(String originalName) {
        return uniqueRelationshipsForType.get(originalName);
    }

    public String getTypeDescription(String originalName) {
        String fromTypeDef = null;
        String fromCSV = AttributeCSVCache.getTypeDescription(originalName);
        TypeDef def = enumDefCache.getOrDefault(originalName, null);
        if (def == null) {
            def = structDefCache.getOrDefault(originalName, null);
        }
        if (def == null) {
            def = entityDefCache.getOrDefault(originalName, null);
        }
        if (def != null) {
            fromTypeDef = def.getDescription();
        }
        return getPreferredDescription(fromTypeDef, fromCSV, AttributeCSVCache.DEFAULT_CLASS_DESCRIPTION);
    }

    public String getAttributeDescription(String objectName, String attrName) {
        String fromTypeDef = null;
        String fromCSV = AttributeCSVCache.getAttributeDescription(objectName, attrName);
        TypeDef def = enumDefCache.getOrDefault(objectName, null);
        if (def == null) {
            def = structDefCache.getOrDefault(objectName, null);
        }
        if (def == null) {
            def = entityDefCache.getOrDefault(objectName, null);
        }
        if (def != null) {
            for (AttributeDef attr : def.getAttributeDefs()) {
                if (attrName.equals(attr.getName())) {
                    fromTypeDef = attr.getDescription();
                    break;
                }
            }
            if (fromTypeDef == null && def instanceof EntityDef) {
                for (RelationshipAttributeDef attr : ((EntityDef) def).getRelationshipAttributeDefs()) {
                    String relnDefName = attr.getRelationshipTypeName();
                    RelationshipDef relnDef = relationshipDefCache.get(relnDefName);
                    if (relnDef != null) {
                        if (attrName.equals(relnDef.getEndDef1().getName())) {
                            fromTypeDef = relnDef.getEndDef1().getDescription();
                            break;
                        } else if (attrName.equals(relnDef.getEndDef2().getName())) {
                            fromTypeDef = relnDef.getEndDef2().getDescription();
                            break;
                        }
                    }
                }
            }
        }
        return getPreferredDescription(fromTypeDef, fromCSV, AttributeCSVCache.DEFAULT_ATTR_DESCRIPTION);
    }

    private String getPreferredDescription(String fromTypeDef, String fromCSV, String fallback) {
        final String typeDefIfExists = (fromTypeDef != null && fromTypeDef.length() > 0) ? fromTypeDef : fromCSV;
        if (config.getPreferTypeDefDescriptions()) {
            return typeDefIfExists;
        } else {
            if (!fromCSV.equals(fallback)) {
                return fromCSV;
            } else {
                return typeDefIfExists;
            }
        }
    }

    private void cacheRelationshipsForInheritance(Collection<EntityDef> toCache) {
        // Populate 'relationshipsForType' map so that we don't repeat inherited attributes in subtypes
        // (this seems to only be a risk for relationship attributes)
        if (!toCache.isEmpty()) {
            List<EntityDef> leftOvers = new ArrayList<>();
            for (EntityDef entityDef : toCache) {
                String typeName = entityDef.getName();
                List<String> superTypes = entityDef.getSuperTypes();
                List<RelationshipAttributeDef> relationships = entityDef.getRelationshipAttributeDefs();
                if (superTypes == null || superTypes.isEmpty()) {
                    subTypeToSuperTypes.put(typeName, new ArrayList<>());
                } else {
                    subTypeToSuperTypes.put(typeName, superTypes);
                }
                if (superTypes == null || superTypes.isEmpty() || typeName.equals("Asset")) {
                    subTypeToSuperType.put(typeName, "");
                    uniqueRelationshipsForType.put(
                            typeName,
                            relationships.stream()
                                    .map(RelationshipAttributeDef::getName)
                                    .collect(Collectors.toSet()));
                } else {
                    // TODO: This can be replaced if we move to an interface-based, polymorphic implementation
                    String singleSuperType = config.getSingleTypeToExtend(typeName, superTypes);
                    if (uniqueRelationshipsForType.containsKey(singleSuperType)) {
                        subTypeToSuperType.put(typeName, singleSuperType);
                        Set<String> inheritedRelationships = getAllInheritedRelationships(singleSuperType);
                        Set<String> uniqueRelationships = relationships.stream()
                                .map(RelationshipAttributeDef::getName)
                                .collect(Collectors.toSet());
                        uniqueRelationships.removeAll(inheritedRelationships);
                        uniqueRelationshipsForType.put(typeName, uniqueRelationships);
                    } else {
                        leftOvers.add(entityDef);
                    }
                }
            }
            cacheRelationshipsForInheritance(leftOvers);
        }
    }

    private Set<String> getAllInheritedRelationships(String superTypeName) {
        // Retrieve all relationship attributes from the supertype (and up) for the received type
        if (superTypeName.equals("")) {
            return new HashSet<>();
        } else {
            Set<String> relations = new HashSet<>(uniqueRelationshipsForType.get(superTypeName));
            relations.addAll(getAllInheritedRelationships(subTypeToSuperType.get(superTypeName)));
            return relations;
        }
    }

    public void addSearchFieldToCache(String className, String attrName, SearchFieldGenerator.Field field) {
        String attrQName = AttributeCSVCache.getAttrQualifiedName(className, attrName);
        if (!searchCache.containsKey(attrQName)) {
            searchCache.put(attrQName, new TreeSet<>());
        }
        searchCache.get(attrQName).add(field);
    }

    public Set<SearchFieldGenerator.Field> getCachedSearchFields(String className, String attrName) {
        return searchCache.get(AttributeCSVCache.getAttrQualifiedName(className, attrName));
    }

    public LinkedHashSet<String> getAllSuperTypesForType(String typeName) {
        List<String> next = subTypeToSuperTypes.get(typeName);
        if (next.isEmpty()) {
            LinkedHashSet<String> root = new LinkedHashSet<>();
            root.add(typeName);
            return root;
        } else {
            LinkedHashSet<String> now = new LinkedHashSet<>(next);
            for (String superType : next) {
                LinkedHashSet<String> again = getAllSuperTypesForType(superType);
                now.addAll(again);
            }
            return now;
        }
    }
}
