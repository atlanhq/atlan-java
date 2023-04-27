/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator {

    public static final String DIRECTORY = "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "assets";

    public static final Set<String> SKIP_GENERATING = Set.of(
            "Referenceable",
            "DataStudio",
            "AtlasServer",
            "DataSet",
            "Infrastructure",
            "ProcessExecution",
            "__AtlasAuditEntry",
            "__AtlasUserProfile",
            "__AtlasUserSavedSearch",
            "__ExportImportAuditEntry",
            "__internal");

    public static final Set<String> NON_ABSTRACT = Set.of("Process", "ColumnProcess", "QlikSpace");

    private static final Map<String, String> CLASS_RENAMING = Map.ofEntries(
            Map.entry("Referenceable", "Asset"),
            Map.entry("Process", "LineageProcess"),
            Map.entry("Collection", "AtlanCollection"),
            Map.entry("Query", "AtlanQuery"),
            Map.entry("AtlasGlossary", "Glossary"),
            Map.entry("AtlasGlossaryCategory", "GlossaryCategory"),
            Map.entry("AtlasGlossaryTerm", "GlossaryTerm"),
            Map.entry("MaterialisedView", "MaterializedView"));

    private static final Map<String, String> INHERITANCE_OVERRIDES = Map.ofEntries(
            Map.entry("Asset", "Reference"),
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

    private final EntityDef entityDef;
    private String parentClassName;
    private List<Attribute> attributes;
    private List<String> originalSubTypes = null;
    private List<String> subTypes = null;
    private List<String> mapContainers = null;

    public AssetGenerator(EntityDef entityDef) {
        super(entityDef);
        this.entityDef = entityDef;
        resolveClassName();
        super.description = AttributeCSVCache.getTypeDescription(originalName);
    }

    @Override
    protected void resolveClassName() {
        super.className = CLASS_RENAMING.containsKey(originalName)
                ? CLASS_RENAMING.get(originalName)
                : getUpperCamelCase(originalName);
    }

    public void resolveDetails() {
        resolveParentClassName();
        resolveSubTypes();
        resolveAttributes();
        resolveRelationships();
    }

    public String getTemplateFile() {
        return ModelGeneratorV2.getTemplateFile(className);
    }

    public boolean isAbstract() {
        return (originalSubTypes != null && !originalSubTypes.isEmpty()) && !NON_ABSTRACT.contains(getOriginalName());
    }

    public void resolveParentClassName() {
        String parentOriginalName = getSingleTypeToExtend(originalName, entityDef.getSuperTypes());
        this.parentClassName = getClassToExtend(parentOriginalName);
    }

    private String getClassToExtend(String originalSuperTypeName) {
        // Default to the mapped name
        return CLASS_RENAMING.getOrDefault(originalSuperTypeName, getUpperCamelCase(originalSuperTypeName));
    }

    private void resolveSubTypes() {
        originalSubTypes = entityDef.getSubTypes();
        if (originalSubTypes != null && !originalSubTypes.isEmpty()) {
            subTypes = new ArrayList<>();
            for (String originalSubType : originalSubTypes) {
                if (!SKIP_GENERATING.contains(originalSubType)) {
                    MappedType subType = ModelGeneratorV2.getCachedType(originalSubType);
                    if (subType != null) {
                        String flattened = INHERITANCE_OVERRIDES.getOrDefault(originalSubType, null);
                        // Only output the subtype if that subtype still considers this type its parent,
                        // after polymorphic flattening...
                        if (flattened == null || flattened.equals(getOriginalName())) {
                            subTypes.add(subType.getName());
                        }
                    } else {
                        log.warn("Mapped subType was not found: {}", originalSubType);
                    }
                }
            }
        }
    }

    private void resolveAttributes() {
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : entityDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(className, attributeDef);
            if (!attribute.getType().getName().equals("Internal")) {
                attributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void resolveRelationships() {
        Set<String> uniqueRelationships = ModelGeneratorV2.getUniqueRelationshipsForType(getOriginalName());
        for (RelationshipAttributeDef relationshipAttributeDef : entityDef.getRelationshipAttributeDefs()) {
            Attribute attribute = new Attribute(className, relationshipAttributeDef);
            if (uniqueRelationships.contains(attribute.getOriginalName())
                    && !attribute.getType().getName().equals("Internal")) {
                boolean duplicate = false;
                for (Attribute existing : attributes) {
                    if (existing.getRenamed().equals(attribute.getRenamed())) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    log.warn(
                            "Found duplicate relationship defined in {} for {} ({}) - skipping.",
                            className,
                            attribute.getRenamed(),
                            attribute.getFullType());
                } else {
                    attributes.add(attribute);
                    checkAndAddMapContainer(attribute);
                }
            }
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

    /**
     * Retrieve the name of the singular type that we should extend for inheritance.
     *
     * @param name of the type we need to determine inheritance for
     * @param superTypes list of super types that are defined for that type
     * @return the name of a single type to use for inheritance
     */
    static String getSingleTypeToExtend(String name, List<String> superTypes) {
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

    @Getter
    public static class Attribute extends AttributeGenerator {

        // Provide a name that Lombok can use for the singularization of these multivalued attributes
        private static final Map<String, String> SINGULAR_MAPPINGS = Map.ofEntries(
                Map.entry("seeAlso", "seeAlsoOne"),
                Map.entry("replacedByTerm", "replacedByTerm"),
                Map.entry("validValuesFor", "validValueFor"),
                Map.entry("isA", "isATerm"),
                Map.entry("replacedBy", "replacedByTerm"),
                Map.entry("childrenCategories", "childCategory"),
                Map.entry("queryUserMap", "putQueryUserMap"),
                Map.entry("queryPreviewConfig", "putQueryPreviewConfig"),
                Map.entry("reportType", "putReportType"),
                Map.entry("projectHierarchy", "addProjectHierarchy"),
                Map.entry("certifier", "putCertifier"),
                Map.entry("presetChartFormData", "putPresetChartFormData"),
                Map.entry("resourceMetadata", "putResourceMetadata"),
                Map.entry("adlsObjectMetadata", "putAdlsObjectMetadata"),
                Map.entry("columnHistogram", "addColumnHistogram"),
                Map.entry("foreignKeyTo", "addForeignKeyTo"),
                Map.entry("quickSightFolderHierarchy", "addQuickSightFolderHierarchy"),
                Map.entry("columnMaxs", "addColumnMax"),
                Map.entry("columnMins", "addColumnMin"));

        private static final Map<String, String> ATTRIBUTE_RENAMING = Map.ofEntries(
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
                Map.entry("sourceQueryComputeCostRecordList", "sourceQueryComputeCostRecords"),
                Map.entry("meanings", "assignedTerms"),
                Map.entry("sqlAsset", "primarySqlAsset"));

        private static final Map<String, String> TYPE_OVERRIDES = Map.ofEntries(
                Map.entry("announcementType", "AtlanAnnouncementType"),
                Map.entry("connectorName", "AtlanConnectorType"),
                Map.entry("category", "AtlanConnectionCategory"));

        protected Attribute() {}

        public Attribute(String className, AttributeDef attributeDef) {
            super(className, attributeDef);
        }

        @Override
        protected void resolveName() {
            super.resolveName();
            setRenamed(
                    ATTRIBUTE_RENAMING.containsKey(originalName)
                            ? ATTRIBUTE_RENAMING.get(originalName)
                            : getLowerCamelCase(originalName));
        }

        @Override
        protected void resolveType(AttributeDef attributeDef) {
            super.resolveType(attributeDef);
            if (TYPE_OVERRIDES.containsKey(originalName)) {
                setType(getType().toBuilder()
                        .name(TYPE_OVERRIDES.get(originalName))
                        .build());
            }
        }

        public String getSingular() {
            if (getType().getContainer() != null) {
                return SINGULAR_MAPPINGS.getOrDefault(originalName, "");
            }
            return null;
        }
    }
}
