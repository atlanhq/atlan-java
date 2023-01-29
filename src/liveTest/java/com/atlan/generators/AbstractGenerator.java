/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.*;
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

    enum IndexType {
        KEYWORD,
        TEXT,
        RANK_FEATURE,
        DATE,
        BOOLEAN,
        FLOAT,
    }

    protected static final String CSV_TYPE_NAME = "Type Name";
    protected static final String CSV_TYPE_DESC = "Type Description";
    protected static final String CSV_ATTR_NAME = "Attribute Name";
    protected static final String CSV_ATTR_DESC = "Attribute Description";

    protected static final String[] CSV_HEADER = {CSV_TYPE_NAME, CSV_TYPE_DESC, CSV_ATTR_NAME, CSV_ATTR_DESC};

    private static final String DESCRIPTIONS_FILE =
            "" + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "attributes.csv";

    // We'll use our own class names for these types, as the existing type names are either overly
    // verbose, too easily conflicting with native Java classes, or have a level of inheritance that is
    // unnecessary
    protected static final Map<String, String> NAME_MAPPINGS = Map.ofEntries(
            Map.entry("Referenceable", "Asset"),
            Map.entry("Process", "LineageProcess"),
            Map.entry("Collection", "AtlanCollection"),
            Map.entry("Query", "AtlanQuery"),
            Map.entry("AtlasGlossary", "Glossary"),
            Map.entry("AtlasGlossaryCategory", "GlossaryCategory"),
            Map.entry("AtlasGlossaryTerm", "GlossaryTerm"),
            Map.entry("MaterialisedView", "MaterializedView"),
            Map.entry("certificate_status", "AtlanCertificateStatus"),
            Map.entry("AwsTag", "AWSTag"),
            Map.entry("AwsCloudWatchMetric", "AWSCloudWatchMetric"),
            Map.entry("google_datastudio_asset_type", "GoogleDataStudioAssetType"),
            Map.entry("icon_type", "LinkIconType"),
            Map.entry("powerbi_endorsement", "PowerBIEndorsementType"));

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
            Map.entry("array<AwsTag>", "List<AWSTag>"),
            Map.entry("array<AzureTag>", "List<AzureTag>"),
            Map.entry("array<GoogleLabel>", "List<GoogleLabel>"),
            Map.entry("array<GoogleTag>", "List<GoogleTag>"),
            Map.entry("array<DbtMetricFilter>", "List<DbtMetricFilter>"),
            Map.entry("array<BadgeCondition>", "List<BadgeCondition>"),
            Map.entry("array<PopularityInsights>", "SortedSet<PopularityInsights>"),
            Map.entry("array<ColumnValueFrequencyMap>", "List<ColumnValueFrequencyMap>"));

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
            Map.entry("Azure", "Catalog"),
            Map.entry("GCS", "Google"),
            Map.entry("ADLS", "Azure"));

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

    protected static final Map<String, EntityDef> entityDefCache = new ConcurrentHashMap<>();
    protected static final Map<String, RelationshipDef> relationshipDefCache = new ConcurrentHashMap<>();
    protected static final Map<String, TypeDef> typeDefCache = new ConcurrentHashMap<>();
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
        if (entityDefCache.isEmpty()) {
            try {
                if (Atlan.getApiToken().equals("") || Atlan.getBaseUrl().equals("")) {
                    System.out.println("Inadequate parameters provided.");
                    printUsage();
                    System.exit(1);
                }
                TypeDefResponse entities = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY);
                TypeDefResponse relationships = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.RELATIONSHIP);
                TypeDefResponse enums = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENUM);
                TypeDefResponse structs = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.STRUCT);
                List<EntityDef> entityDefs = entities.getEntityDefs();
                cacheEntityDefs(entityDefs);
                cacheRelationshipDefs(relationships.getRelationshipDefs());
                cacheOtherTypeDefs(enums.getEnumDefs());
                cacheOtherTypeDefs(structs.getStructDefs());
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

    /**
     * Retrieve a list of all attribute definitions that are inherited by this type from all
     * of its supertypes (and their supertypes).
     *
     * @param typeDef type definition from which to retrieve the inherited attributes
     * @return a map of the list of inherited attributes, keyed by the name of the type that owns each set of attributes
     */
    protected static Map<String, List<AttributeDef>> getAllInheritedAttributes(TypeDef typeDef) {
        if (typeDef instanceof EntityDef) {
            EntityDef entityDef = (EntityDef) typeDef;
            List<String> superTypes = entityDef.getSuperTypes();
            if (superTypes == null || superTypes.isEmpty()) {
                return new LinkedHashMap<>();
            } else {
                Map<String, List<AttributeDef>> allInherited = new LinkedHashMap<>();
                for (String superTypeName : superTypes) {
                    EntityDef superTypeDef = entityDefCache.get(superTypeName);
                    allInherited.putAll(getAllInheritedAttributes(superTypeDef));
                    allInherited.put(superTypeName, superTypeDef.getAttributeDefs());
                }
                return allInherited;
            }
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * Retrieve a list of all relationship attribute definitions that are inherited by this type from all
     * of its supertypes (and their supertypes).
     *
     * @param entityDef entity type definition from which to retrieve the inherited relationship attributes
     * @return a map of the list of inherited relationship attributes, keyed by the name of the type that owns each set of relationship attributes
     */
    protected static Map<String, List<RelationshipAttributeDef>> getAllInheritedRelationshipAttributes(
            EntityDef entityDef) {
        List<String> superTypes = entityDef.getSuperTypes();
        if (superTypes == null || superTypes.isEmpty()) {
            return new LinkedHashMap<>();
        } else {
            Map<String, List<RelationshipAttributeDef>> allInherited = new LinkedHashMap<>();
            for (String superTypeName : superTypes) {
                EntityDef superTypeDef = entityDefCache.get(superTypeName);
                allInherited.putAll(getAllInheritedRelationshipAttributes(superTypeDef));
                allInherited.put(superTypeName, superTypeDef.getRelationshipAttributeDefs());
            }
            return allInherited;
        }
    }

    /**
     * Retrieve a mapping from field name to type of index for the provided attribute's searchable fields.
     *
     * @param attributeDef attribute definition from which to retrieve the searchable fields
     * @return a map of the searchable field names to their index type for the attribute
     */
    protected static Map<String, IndexType> getSearchFieldsForAttribute(AttributeDef attributeDef) {
        String attrName = attributeDef.getName();
        Map<String, IndexType> map = new LinkedHashMap<>();
        // Default index
        Map<String, String> config = attributeDef.getIndexTypeESConfig();
        if (config != null && config.containsKey("analyzer")) {
            String analyzer = config.get("analyzer");
            if (analyzer.equals("atlan_text_analyzer")) {
                map.put(attrName, IndexType.TEXT);
            } else {
                log.warn("Unknown analyzer on attribute {}: {}", attributeDef.getName(), analyzer);
            }
        } else {
            map.put(attrName, getDefaultIndexForType(attributeDef.getTypeName()));
        }
        // Additional indexes
        Map<String, Map<String, String>> fields = attributeDef.getIndexTypeESFields();
        if (fields != null) {
            for (Map.Entry<String, Map<String, String>> entry : fields.entrySet()) {
                String fieldName = attrName + "." + entry.getKey();
                Map<String, String> indexDetails = entry.getValue();
                if (indexDetails != null && indexDetails.containsKey("type")) {
                    String indexType = indexDetails.get("type");
                    switch (indexType) {
                        case "keyword":
                            map.put(fieldName, IndexType.KEYWORD);
                            break;
                        case "text":
                            map.put(fieldName, IndexType.TEXT);
                            break;
                        case "rank_feature":
                            map.put(fieldName, IndexType.RANK_FEATURE);
                            break;
                        default:
                            log.warn(
                                    "Unknown index type on attribute {}, field {}: {}",
                                    attributeDef.getName(),
                                    fieldName,
                                    indexType);
                            break;
                    }
                } else {
                    map.put(fieldName, getDefaultIndexForType(attributeDef.getTypeName()));
                }
            }
        }
        return map;
    }

    /**
     * Lookup the default index for the provided attribute data type.
     *
     * @param typeName data type of the attribute
     * @return the default index for that data type
     */
    protected static IndexType getDefaultIndexForType(String typeName) {
        String baseType = typeName;
        if (typeName.startsWith("array<")) {
            if (typeName.startsWith("array<map<")) {
                baseType = getEmbeddedType(typeName.substring("array<".length(), typeName.length() - 1));
            } else {
                baseType = getEmbeddedType(typeName);
            }
        }
        IndexType toUse;
        switch (baseType) {
            case "date":
                toUse = IndexType.DATE;
                break;
            case "float":
            case "double":
            case "int":
            case "long":
                toUse = IndexType.FLOAT;
                break;
            case "boolean":
                toUse = IndexType.BOOLEAN;
                break;
            case "string":
            default:
                toUse = IndexType.KEYWORD;
                break;
        }
        return toUse;
    }

    /**
     * Determine the primitive type of the attribute when it's values are contained in an
     * array or map.
     *
     * @param attrType data type of the attribute
     * @return the primitive contained type of the attribute's values
     */
    protected static String getEmbeddedType(String attrType) {
        return attrType.substring(attrType.indexOf("<") + 1, attrType.indexOf(">"));
    }

    private static String getAttrQualifiedName(String typeName, String attrName) {
        return typeName + "|" + attrName;
    }

    private static void cacheEntityDefs(List<EntityDef> entityDefs) {
        for (EntityDef entityDef : entityDefs) {
            String name = entityDef.getName();
            entityDefCache.put(name, entityDef);
        }
    }

    private static void cacheRelationshipDefs(List<RelationshipDef> relationshipDefs) {
        for (RelationshipDef relationshipDef : relationshipDefs) {
            String name = relationshipDef.getName();
            relationshipDefCache.put(name, relationshipDef);
        }
    }

    private static <T extends TypeDef> void cacheOtherTypeDefs(List<T> typeDefs) {
        for (TypeDef typeDef : typeDefs) {
            String name = typeDef.getName();
            typeDefCache.put(name, typeDef);
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
