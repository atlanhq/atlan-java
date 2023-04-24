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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator {

    public static final String DIRECTORY = ""
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "assets";

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
    private final String parentClassName;
    private List<Attribute> attributes;

    public AssetGenerator(EntityDef entityDef) {
        super(entityDef);
        this.entityDef = entityDef;
        this.parentClassName = getSingleTypeToExtend(originalName, entityDef.getSuperTypes());
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
        resolveAttributes();
        resolveRelationships();
    }

    public String getTemplateFile() {
        return ModelGeneratorV2.getTemplateFile(className);
    }

    private void resolveAttributes() {
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : entityDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(className, attributeDef);
            attributes.add(attribute);
        }
    }

    private void resolveRelationships() {
        for (RelationshipAttributeDef relationshipAttributeDef : entityDef.getRelationshipAttributeDefs()) {
            Attribute attribute = new Attribute(className, relationshipAttributeDef);
            attributes.add(attribute);
        }
    }

    /**
     * Retrieve the name of the singular type that we should extend for inheritance.
     *
     * @param name of the type we need to determine inheritance for
     * @param superTypes list of super types that are defined for that type
     * @return the name of a single type to use for inheritance
     */
    private static String getSingleTypeToExtend(String name, List<String> superTypes) {
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
    public static final class Attribute {

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
                Map.entry("adlsObjectMetadata", "putAdlsObjectMetadata"));

        @Setter(AccessLevel.PRIVATE)
        private MappedType type;

        private final String originalName;
        private final String renamed;
        private final String description;

        public Attribute(String className, AttributeDef attributeDef) {
            this.type = getMappedType(attributeDef.getTypeName());
            this.originalName =
                    attributeDef.getDisplayName() == null ? attributeDef.getName() : attributeDef.getDisplayName();
            this.renamed = getLowerCamelCase(originalName);
            this.description = AttributeCSVCache.getAttributeDescription(className, originalName);
        }

        public String getSingular() {
            if (type.getName().contains("<")) {
                return SINGULAR_MAPPINGS.getOrDefault(originalName, "");
            }
            return null;
        }
    }
}
