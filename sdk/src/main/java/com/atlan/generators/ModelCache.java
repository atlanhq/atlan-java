/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
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
    private final Map<String, RelationshipGenerator> relationshipCache = new HashMap<>();

    private final Map<String, List<String>> subTypeToSuperTypes = new ConcurrentHashMap<>();

    private static ModelCache INSTANCE = null;

    private ModelCache(AtlanClient client) throws AtlanException {
        enumDefCache = new ConcurrentHashMap<>();
        for (EnumDef enumDef : client.typeDefs.list(AtlanTypeCategory.ENUM).getEnumDefs()) {
            enumDefCache.put(enumDef.getName(), enumDef);
        }
        structDefCache = new ConcurrentHashMap<>();
        for (StructDef structDef :
                client.typeDefs.list(AtlanTypeCategory.STRUCT).getStructDefs()) {
            structDefCache.put(structDef.getName(), structDef);
        }
        entityDefCache = new ConcurrentHashMap<>();
        for (EntityDef entityDef :
                client.typeDefs.list(AtlanTypeCategory.ENTITY).getEntityDefs()) {
            entityDefCache.put(entityDef.getName(), entityDef);
        }
        relationshipDefCache = new ConcurrentHashMap<>();
        for (RelationshipDef relationshipDef :
                client.typeDefs.list(AtlanTypeCategory.RELATIONSHIP).getRelationshipDefs()) {
            relationshipDefCache.put(relationshipDef.getName(), relationshipDef);
        }
    }

    private static ModelCache createInstance(AtlanClient client) {
        try {
            ModelCache cache = new ModelCache(client);
            cache = fixMissingQNDescription(cache);
            cache.cacheInheritance(cache.getEntityDefCache().values());
            return cache;
        } catch (AtlanException | InterruptedException e) {
            log.error("Unable to refresh typedef caches.", e);
            return null;
        }
    }

    private static ModelCache fixMissingQNDescription(ModelCache cache) throws AtlanException, InterruptedException {
        EntityDef ref = cache.getEntityDefCache().get("Referenceable");
        if (ref != null) {
            Optional<AttributeDef> qn = ref.getAttributeDefs().stream()
                    .filter(attr -> attr.getName().equals("qualifiedName"))
                    .findFirst();
            if (qn.isEmpty()
                    || qn.get().getDescription() == null
                    || qn.get().getDescription().isEmpty()) {
                log.info("Referenceable had empty qualifiedName, overwriting it...");
                String desc =
                        "Unique name for this asset. This is typically a concatenation of the asset's name onto its parent's qualifiedName. This must be unique across all assets of the same type.";
                List<AttributeDef> fixed = new ArrayList<>();
                ref.getAttributeDefs().forEach(attr -> {
                    if (attr.getName().equals("qualifiedName")) {
                        AttributeDef attrFixed =
                                attr.toBuilder().description(desc).build();
                        fixed.add(attrFixed);
                    } else {
                        fixed.add(attr);
                    }
                });
                EntityDef refFixed = ref.toBuilder().attributeDefs(fixed).build();
                cache.getEntityDefCache().put("Referenceable", refFixed);
            }
        }
        return cache;
        // if (retry) {
        //     log.info("Referenceable had empty qualifiedName, retrying (attempt #{})...", retryAttempt);
        //     Thread.sleep(HttpClient.waitTime(retryAttempt).toMillis());
        //     return retryIfNeeded(client, new ModelCache(client), retryAttempt + 1);
        // } else {
        //     return cache;
        // }
    }

    public static ModelCache getInstance(AtlanClient client) {
        if (INSTANCE == null) {
            INSTANCE = createInstance(client);
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

    public RelationshipGenerator addRelationshipGenerator(String name, RelationshipGenerator generator) {
        return relationshipCache.put(name, generator);
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

    public Set<String> getUniqueAttributesForType(String originalName) {
        EntityDef entityDef = entityDefCache.get(originalName);
        return new TreeSet<>(
                entityDef.getAttributeDefs().stream().map(AttributeDef::getName).collect(Collectors.toSet()));
    }

    public Set<String> getUniqueRelationshipsForType(String originalName) {
        EntityDef entityDef = entityDefCache.get(originalName);
        Set<String> startingPoint = new TreeSet<>(entityDef.getRelationshipAttributeDefs().stream()
                .map(AttributeDef::getName)
                .collect(Collectors.toSet()));
        Set<String> superTypes = getAllSuperTypesForType(originalName);
        for (String superType : superTypes) {
            // Skip Referenceable to avoid losing the overloaded 'meanings' relationship
            if (superType != null && !superType.equals("Referenceable")) {
                EntityDef superDef = entityDefCache.get(superType);
                Set<String> toRemove = superDef.getRelationshipAttributeDefs().stream()
                        .map(AttributeDef::getName)
                        .collect(Collectors.toSet());
                startingPoint.removeAll(toRemove);
            }
        }
        return startingPoint;
    }

    public String getTypeDescription(String originalName) {
        String fromTypeDef = null;
        TypeDef def = enumDefCache.getOrDefault(originalName, null);
        if (def == null) {
            def = structDefCache.getOrDefault(originalName, null);
        }
        if (def == null) {
            def = entityDefCache.getOrDefault(originalName, null);
        }
        if (def == null) {
            def = relationshipDefCache.getOrDefault(originalName, null);
        }
        if (def != null) {
            fromTypeDef = def.getDescription();
        }
        return (fromTypeDef != null && !fromTypeDef.isEmpty()) ? fromTypeDef : "TBC";
    }

    public String getAttributeDescription(String objectName, String attrName) {
        String fromTypeDef = null;
        TypeDef def = enumDefCache.getOrDefault(objectName, null);
        if (def == null) {
            def = structDefCache.getOrDefault(objectName, null);
        }
        if (def == null) {
            def = relationshipDefCache.getOrDefault(objectName, null);
        }
        SortedSet<AttributeDef> allAttrs = new TreeSet<>();
        if (def == null && entityDefCache.containsKey(objectName)) {
            def = entityDefCache.getOrDefault(objectName, null);
            allAttrs = getAllAttributesForType(objectName);
        } else if (def != null) {
            allAttrs = new TreeSet<>(def.getAttributeDefs());
        }
        if (def != null) {
            for (AttributeDef attr : allAttrs) {
                if (attrName.equals(attr.getName())) {
                    fromTypeDef = attr.getDescription();
                    break;
                }
            }
            if (fromTypeDef == null && def instanceof EntityDef) {
                for (RelationshipAttributeDef attr : getAllRelationshipsForType(def.getName())) {
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
        return (fromTypeDef != null && !fromTypeDef.isEmpty()) ? fromTypeDef : "TBC";
    }

    private void cacheInheritance(Collection<EntityDef> toCache) {
        // Populate inheritance maps
        if (!toCache.isEmpty()) {
            List<EntityDef> leftOvers = new ArrayList<>();
            for (EntityDef entityDef : toCache) {
                String typeName = entityDef.getName();
                List<String> superTypes = entityDef.getSuperTypes();
                if (superTypes == null || superTypes.isEmpty()) {
                    subTypeToSuperTypes.put(typeName, new ArrayList<>());
                } else {
                    subTypeToSuperTypes.put(typeName, superTypes);
                }
                if (superTypes != null && !superTypes.isEmpty() && !typeName.equals("Asset")) {
                    for (String superType : superTypes) {
                        if (!subTypeToSuperTypes.containsKey(superType)) {
                            leftOvers.add(entityDefCache.get(superType));
                        }
                    }
                }
            }
            cacheInheritance(leftOvers);
        }
    }

    public Set<String> getAllSuperTypesForType(String typeName) {
        List<String> next = subTypeToSuperTypes.get(typeName);
        if (next.isEmpty()) {
            LinkedHashSet<String> root = new LinkedHashSet<>();
            root.add(typeName);
            return root;
        } else {
            LinkedHashSet<String> now = new LinkedHashSet<>(next);
            for (String superType : next) {
                Set<String> again = getAllSuperTypesForType(superType);
                now.addAll(again);
            }
            return now;
        }
    }

    public SortedSet<AttributeDef> getAllNonAssetAttributesForType(String originalName) {
        SortedSet<AttributeDef> all = getAllAttributesForType(originalName);
        all.removeAll(getAllAttributesForType("Asset"));
        return all;
    }

    public SortedSet<RelationshipAttributeDef> getAllNonAssetRelationshipsForType(String originalName) {
        SortedSet<RelationshipAttributeDef> all = getAllRelationshipsForType(originalName);
        all.removeAll(getAllRelationshipsForType("Asset"));
        return all;
    }

    SortedSet<AttributeDef> getAllAttributesForType(String originalName) {
        SortedSet<AttributeDef> full = new TreeSet<>();
        getAttributesForType(originalName, full, new HashSet<>());
        return full;
    }

    private void getAttributesForType(
            String originalName, SortedSet<AttributeDef> aggregated, Set<String> processedTypes) {
        if (!processedTypes.contains(originalName)) {
            EntityDef entityDef = entityDefCache.get(originalName);
            if (originalName.equals("Referenceable")) {
                // Retain only the 'qualifiedName' attribute from Referenceable
                List<AttributeDef> attrs = entityDefCache.get(originalName).getAttributeDefs();
                for (AttributeDef attributeDef : attrs) {
                    if (attributeDef.getName().equals("qualifiedName")) {
                        aggregated.add(attributeDef);
                    }
                }
            } else {
                addAndLogAttributeConflicts(originalName, aggregated, entityDef.getAttributeDefs(), originalName);
                processedTypes.add(originalName);
                List<String> superTypes = entityDef.getSuperTypes();
                if (superTypes != null && !superTypes.isEmpty()) {
                    for (String superType : superTypes) {
                        getAttributesForType(superType, aggregated, processedTypes);
                    }
                }
            }
        }
    }

    SortedSet<RelationshipAttributeDef> getAllRelationshipsForType(String originalName) {
        SortedSet<RelationshipAttributeDef> full = new TreeSet<>();
        getRelationshipsForType(originalName, full, new HashSet<>());
        return full;
    }

    private void getRelationshipsForType(
            String originalName, SortedSet<RelationshipAttributeDef> aggregated, Set<String> processedTypes) {
        if (!processedTypes.contains(originalName)) {
            EntityDef entityDef = entityDefCache.get(originalName);
            if (originalName.equals("Referenceable")) {
                // Retain only the 'meanings' relationship from Referenceable
                List<RelationshipAttributeDef> attrs = entityDef.getRelationshipAttributeDefs();
                for (RelationshipAttributeDef attributeDef : attrs) {
                    if (attributeDef.getName().equals("meanings")) {
                        aggregated.add(attributeDef);
                    }
                }
            } else {
                Set<String> uniqueRelationships = getUniqueRelationshipsForType(originalName);
                Set<RelationshipAttributeDef> retained = new TreeSet<>();
                for (RelationshipAttributeDef attributeDef : entityDef.getRelationshipAttributeDefs()) {
                    if (uniqueRelationships.contains(attributeDef.getName())) {
                        boolean added = retained.add(attributeDef);
                        if (!added) {
                            log.warn(
                                    "Conflicting relationship found for {}, within own typedef: {}",
                                    originalName,
                                    attributeDef.getName());
                        }
                    }
                }
                if (!retained.isEmpty()) {
                    addAndLogRelationshipConflicts(originalName, aggregated, retained, originalName);
                }
                processedTypes.add(originalName);
                List<String> superTypes = entityDef.getSuperTypes();
                if (superTypes != null && !superTypes.isEmpty()) {
                    for (String superType : superTypes) {
                        getRelationshipsForType(superType, aggregated, processedTypes);
                    }
                }
            }
        }
    }

    // Set of attributes that are known to conflict with relationship attributes of the same name
    private static final Set<String> attributesToIgnore = Set.of("inputs", "outputs");

    static String getAttrQualifiedName(String typeName, String attrName) {
        return typeName + "|" + attrName;
    }

    private void addAndLogAttributeConflicts(
            String typeName, SortedSet<AttributeDef> toAddTo, Collection<AttributeDef> toAdd, String fromSuperType) {
        for (AttributeDef one : toAdd) {
            if (!attributesToIgnore.contains(one.getName())) {
                boolean added = toAddTo.add(one);
                if (!added) {
                    log.warn("Conflicting attribute found for {}, from {}: {}", typeName, fromSuperType, one.getName());
                }
            }
        }
    }

    private void addAndLogRelationshipConflicts(
            String typeName,
            SortedSet<RelationshipAttributeDef> toAddTo,
            Collection<RelationshipAttributeDef> toAdd,
            String fromSuperType) {
        for (RelationshipAttributeDef one : toAdd) {
            boolean added = toAddTo.add(one);
            if (!added) {
                log.warn("Conflicting relationship found for {}, from {}: {}", typeName, fromSuperType, one.getName());
            }
        }
    }
}
