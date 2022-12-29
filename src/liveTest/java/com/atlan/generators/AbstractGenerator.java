/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public abstract class AbstractGenerator extends AtlanLiveTest {

    protected static final String CSV_TYPE_NAME = "Type Name";
    protected static final String CSV_TYPE_DESC = "Type Description";
    protected static final String CSV_ATTR_NAME = "Attribute Name";
    protected static final String CSV_ATTR_DESC = "Attribute Description";

    protected static final String[] CSV_HEADER = {CSV_TYPE_NAME, CSV_TYPE_DESC, CSV_ATTR_NAME, CSV_ATTR_DESC};

    private static final String DESCRIPTIONS_FILE =
            "" + "src" + File.separator + "liveTest" + File.separator + "resources" + File.separator + "attributes.csv";

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
            Map.entry("array<BadgeCondition>", "List<BadgeCondition>"),
            Map.entry("array<PopularityInsights>", "SortedSet<PopularityInsights>"));

    // Map types that use polymorphism to only a single supertype
    protected static final Map<String, String> INHERITANCE_OVERRIDES = Map.ofEntries(
            //            Map.entry("Asset", "Reference"),
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
            Map.entry("connectorName", "connectorType"),
            Map.entry("__hasLineage", "hasLineage"),
            Map.entry("viewsCount", "viewCount"),
            Map.entry("materialisedView", "materializedView"),
            Map.entry("materialisedViews", "materializedViews"),
            Map.entry("atlanSchema", "schema"),
            Map.entry("sourceQueryComputeCostList", "sourceQueryComputeCosts"),
            Map.entry("sourceReadTopUserList", "sourceReadTopUsers"),
            Map.entry("sourceReadRecentUserList", "sourceReadRecentUsers"),
            Map.entry("sourceReadRecentUserRecordList", "sourceReadRecentUserRecords"),
            Map.entry("sourceReadTopUserRecordList", "sourceReadTopUserRecords"),
            Map.entry("sourceReadPopularQueryRecordList", "sourceReadPopularQueryRecords"),
            Map.entry("sourceReadExpensiveQueryRecordList", "sourceReadExpensiveQueryRecords"),
            Map.entry("sourceReadSlowQueryRecordList", "sourceReadSlowQueryRecords"),
            Map.entry("sourceQueryComputeCostRecordList", "sourceQueryComputeCostRecords"));

    // Go ahead and create these types as non-abstract types, despite having
    // subtypes
    protected static final Map<String, String> CREATE_NON_ABSTRACT = Map.ofEntries(
            Map.entry("LineageProcess", "AbstractProcess"), Map.entry("ColumnProcess", "AbstractColumnProcess"));

    protected static final Map<String, EntityDef> typeDefCache = new ConcurrentHashMap<>();
    protected static final Map<String, Set<String>> relationshipsForType = new ConcurrentHashMap<>();
    protected static final SortedSet<String> typesWithMaps = new TreeSet<>();
    private static final Map<String, String> subTypeToSuperType = new ConcurrentHashMap<>();
    private static final Map<String, String> qualifiedAttrToDescription = new ConcurrentHashMap<>();
    private static final Map<String, String> typeNameToDescription = new ConcurrentHashMap<>();

    private static void printUsage() {
        System.out.println("You must configure the credentials using Atlan.setApiToken() and Atlan.setBaseUrl().");
    }

    /** Cache all type definition information we can find from Atlan itself. */
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

    /** Cache all attribute descriptions that are defined in an external CSV file. */
    protected static void cacheDescriptions() {
        try (BufferedReader in = Files.newBufferedReader(Paths.get(DESCRIPTIONS_FILE), UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader(CSV_HEADER)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(in);
            for (CSVRecord record : records) {
                String typeName = record.get(CSV_TYPE_NAME);
                String typeDesc = record.get(CSV_TYPE_DESC);
                typeNameToDescription.put(typeName, typeDesc == null || typeDesc.length() == 0 ? "" : typeDesc);
                String attrName = record.get(CSV_ATTR_NAME);
                String attrDesc = record.get(CSV_ATTR_DESC);
                if (attrDesc != null && attrDesc.length() > 0) {
                    // Don't even bother using up memory if there is no description...
                    String attrQN = getAttrQualifiedName(typeName, attrName);
                    qualifiedAttrToDescription.put(attrQN, attrDesc);
                }
            }
        } catch (IOException e) {
            log.error("Unable to access or read descriptions CSV file.", e);
            System.exit(1);
        }
    }

    /**
     * Retrieve the description of the attribute as defined in the external CSV file.
     *
     * @param typeName name of the type in which the attribute exists
     * @param attrName name of the attribute within that type
     * @return the description for that attribute, if any, or 'TBC' if none could be found
     */
    protected static String getAttributeDescription(String typeName, String attrName) {
        String attrQN = getAttrQualifiedName(typeName, attrName);
        return qualifiedAttrToDescription.getOrDefault(attrQN, "TBC");
    }

    /**
     * Retrieve the description of the type as defined in the external CSV file.
     *
     * @param typeName name of the type for which to obtain a description
     * @return the description for that type
     */
    protected static String getTypeDescription(String typeName) {
        return typeNameToDescription.getOrDefault(typeName, "TBC");
    }

    private static String getAttrQualifiedName(String typeName, String attrName) {
        return typeName + "|" + attrName;
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

    /**
     * Retrieve the name of the singular type that we should extend for inheritance.
     *
     * @param name of the type we need to determine inheritance for
     * @param superTypes list of super types that are defined for that type
     * @return the name of a single type to use for inheritance
     */
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
