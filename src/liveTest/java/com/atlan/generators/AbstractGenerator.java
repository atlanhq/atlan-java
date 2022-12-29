/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGenerator extends AtlanLiveTest {

    // We'll use our own class names for these types, as the existing type names are either overly
    // verbose, too easily conflicting with native Java classes, or have a level of inheritance that is
    // unnecessary
    protected static final Map<String, String> NAME_MAPPINGS = Map.of(
            "Referenceable", "Asset",
            "Process", "LineageProcess",
            "Collection", "AtlanCollection",
            "Query", "AtlanQuery",
            "AtlasGlossary", "Glossary",
            "AtlasGlossaryCategory", "GlossaryCategory",
            "AtlasGlossaryTerm", "GlossaryTerm",
            "MaterialisedView", "MaterializedView");

    // Map attribute types to native Java types
    protected static final Map<String, String> TYPE_MAPPINGS = Map.ofEntries(
            Map.entry("string", "String"),
            Map.entry("boolean", "Boolean"),
            Map.entry("int", "Integer"),
            Map.entry("long", "Long"),
            Map.entry("date", "Long"),
            Map.entry("float", "Double"),
            Map.entry("array<string>", "SortedSet<String>"),
            Map.entry("map<string,string>", "Map<String, String>"),
            Map.entry("map<string,long>", "Map<String, Long>"),
            Map.entry("array<map<string,string>>", "List<Map<String, String>>"),
            Map.entry("icon_type", "LinkIconType"),
            Map.entry("google_datastudio_asset_type", "GoogleDataStudioAssetType"),
            Map.entry("array<AwsTag>", "List<AWSTag>"),
            Map.entry("powerbi_endorsement", "PowerBIEndorsementType"),
            Map.entry("array<GoogleLabel>", "List<GoogleLabel>"),
            Map.entry("array<GoogleTag>", "List<GoogleTag>"),
            Map.entry("array<DbtMetricFilter>", "List<DbtMetricFilter>"),
            Map.entry("array<BadgeCondition>", "List<BadgeCondition>"));

    // Map types that use polymorphism to only a single supertype
    protected static final Map<String, String> INHERITANCE_OVERRIDES = Map.ofEntries(
            Map.entry("S3", "AWS"),
            Map.entry("DataStudioAsset", "Google"),
            Map.entry("DbtColumnProcess", "ColumnProcess"),
            Map.entry("DbtProcess", "Process"),
            Map.entry("DbtMetric", "Metric"),
            Map.entry("AWS", "Catalog"),
            Map.entry("Google", "Catalog"),
            Map.entry("GCS", "Google"));

    // Rename these attributes for consistency (handled via JsonProperty serde)
    protected static final Map<String, String> ATTRIBUTE_RENAMING = Map.ofEntries(
            Map.entry("viewsCount", "viewCount"),
            Map.entry("materialisedView", "materializedView"),
            Map.entry("materialisedViews", "materializedViews"),
            Map.entry("atlanSchema", "schema"));

    // Go ahead and create these types as non-abstract types, despite having
    // subtypes
    protected static final Map<String, String> CREATE_NON_ABSTRACT = Map.ofEntries(
            Map.entry("LineageProcess", "AbstractProcess"), Map.entry("ColumnProcess", "AbstractColumnProcess"));

    protected static final Map<String, EntityDef> typeDefCache = new ConcurrentHashMap<>();
    protected static final Map<String, Set<String>> relationshipsForType = new ConcurrentHashMap<>();
    protected static final Map<String, String> subTypeToSuperType = new ConcurrentHashMap<>();
    protected static final SortedSet<String> typesWithMaps = new TreeSet<>();

    private static void printUsage() {
        System.out.println("You must configure the credentials using Atlan.setApiToken() and Atlan.setBaseUrl().");
    }

    protected static void cacheModels() {
        if (typeDefCache.isEmpty()) {
            try {
                if (Atlan.getApiToken().equals("") || Atlan.getBaseUrl().equals("")) {
                    System.out.println("Inadequate parameters provided.");
                    printUsage();
                    System.exit(1);
                }
                TypeDefResponse response = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY);
                List<EntityDef> entityDefs = response.getEntityDefs();
                cacheTypeDefs(entityDefs);
                cacheTypesWithMaps(entityDefs);
                cacheRelationshipsForInheritance(entityDefs);
            } catch (Exception e) {
                log.error("Unexpected exception trying to retrieve typedefs.", e);
                System.exit(1);
            }
        }
    }

    private static void cacheTypeDefs(List<EntityDef> entityDefs) {
        for (EntityDef entityDef : entityDefs) {
            String name = entityDef.getName();
            typeDefCache.put(name, entityDef);
        }
    }

    private static Set<String> getAllInheritedRelationships(String superTypeName) {
        // Retrieve all relationship attributes from the supertype (and up) for the received type
        if (superTypeName.equals("")) {
            return new HashSet<>();
        } else {
            Set<String> relations = new HashSet<>(relationshipsForType.get(superTypeName));
            relations.addAll(getAllInheritedRelationships(subTypeToSuperType.get(superTypeName)));
            return relations;
        }
    }

    private static void cacheTypesWithMaps(List<EntityDef> entityDefs) {
        for (EntityDef entityDef : entityDefs) {
            for (AttributeDef attr : entityDef.getAttributeDefs()) {
                String attrType = attr.getTypeName();
                if (attrType.contains("map<")) {
                    typesWithMaps.add(entityDef.getName());
                }
            }
        }
    }

    private static void cacheRelationshipsForInheritance(List<EntityDef> entityDefs) {
        // Populate 'relationshipsForType' map so that we don't repeat inherited attributes in subtypes
        // (this seems to only be a risk for relationship attributes)
        if (!entityDefs.isEmpty()) {
            List<EntityDef> leftOvers = new ArrayList<>();
            for (EntityDef entityDef : entityDefs) {
                String typeName = entityDef.getName();
                List<String> superTypes = entityDef.getSuperTypes();
                List<RelationshipAttributeDef> relationships = entityDef.getRelationshipAttributeDefs();
                if (superTypes == null || superTypes.isEmpty()) {
                    subTypeToSuperType.put(typeName, "");
                    relationshipsForType.put(
                            typeName,
                            relationships.stream()
                                    .map(RelationshipAttributeDef::getName)
                                    .collect(Collectors.toSet()));
                } else {
                    String singleSuperType = getSingleTypeToExtend(typeName, superTypes);
                    if (relationshipsForType.containsKey(singleSuperType)) {
                        subTypeToSuperType.put(typeName, singleSuperType);
                        Set<String> inheritedRelationships = getAllInheritedRelationships(singleSuperType);
                        Set<String> uniqueRelationships = relationships.stream()
                                .map(RelationshipAttributeDef::getName)
                                .collect(Collectors.toSet());
                        uniqueRelationships.removeAll(inheritedRelationships);
                        relationshipsForType.put(typeName, uniqueRelationships);
                    } else {
                        leftOvers.add(entityDef);
                    }
                }
            }
            cacheRelationshipsForInheritance(leftOvers);
        }
    }

    protected static String getSingleTypeToExtend(String name, List<String> superTypes) {
        if (INHERITANCE_OVERRIDES.containsKey(name)) {
            return INHERITANCE_OVERRIDES.get(name);
        } else if (superTypes == null || superTypes.isEmpty()) {
            return "AtlanObject";
        } else if (superTypes.size() == 1) {
            return superTypes.get(0);
        } else {
            log.warn("Multiple superTypes detected — returning only the first: {}", superTypes);
            return superTypes.get(0);
        }
    }
}
