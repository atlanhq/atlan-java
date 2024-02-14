/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.typedefs.*;
import com.atlan.util.StringUtils;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator implements Comparable<AssetGenerator> {

    public static final String DIRECTORY = "assets";

    // Sort attribute definitions in a set based purely on their name (two attributes
    // in the same set with the same name should be a conflict / duplicate)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<AssetGenerator> assetComparator =
            Comparator.comparing(AssetGenerator::getOriginalName, stringComparator);

    private final EntityDef entityDef;
    private String parentClassName;
    private SortedSet<Attribute<?>> interfaceAttributes;
    private SortedSet<Attribute<?>> classAttributes;
    private SortedSet<Attribute<?>> nonInheritedAttributes;
    private List<AssetGenerator> originalSuperTypes = null;
    private List<AssetGenerator> fullSubTypes = null;
    private List<String> originalSubTypes = null;
    private List<String> subTypes = null;
    private List<String> mapContainers = null;
    private final Set<String> superTypes;

    public AssetGenerator(EntityDef entityDef, GeneratorConfig cfg) {
        super(entityDef, cfg);
        this.entityDef = entityDef;
        resolveClassName();
        super.description = cache.getTypeDescription(originalName);
        this.superTypes = cache.getAllSuperTypesForType(getOriginalName());
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(originalName);
    }

    public String resolveSuperTypeName(String name) {
        if (name.equals("Referenceable")) {
            // Don't rename Referenceable, since we have an actual interface defined for it
            return name;
        }
        return cfg.resolveClassName(name);
    }

    public void resolveDetails() {
        resolveParentClassName();
        resolveSubTypes();
        resolveAttributes();
        resolveRelationships();
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

    public String getInterfaceTemplateFile() {
        try {
            return cfg.getFreemarkerConfig()
                    .getTemplate("I" + className + ".ftl")
                    .getSourceName();
        } catch (TemplateNotFoundException e) {
            // Do nothing - no template to load or otherwise handle
        } catch (IOException e) {
            log.error("Error reading template: I{}.ftl", className, e);
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

    public boolean isAbstract() {
        return (originalSubTypes != null && !originalSubTypes.isEmpty()) && !cfg.forceNonAbstract(getOriginalName());
    }

    public void resolveParentClassName() {
        if (getOriginalName().equals("Asset")) {
            this.parentClassName = "Reference";
        } else {
            this.parentClassName = "Asset";
        }
    }

    public SortedSet<String> getAllSubTypes(String originalTypeName) {
        SortedSet<String> localSubTypes = new TreeSet<>();
        AssetGenerator assetGen = cache.getCachedAssetType(originalTypeName);
        if (assetGen != null) {
            assetGen.resolveSubTypes();
            if (!assetGen.isAbstract()) {
                log.info("Adding concrete subtype {} to: {}", assetGen.getClassName(), originalTypeName);
                localSubTypes.add(assetGen.getClassName());
            }
            List<String> subTypes = assetGen.getOriginalSubTypes();
            if (subTypes != null && !subTypes.isEmpty()) {
                for (String subType : subTypes) {
                    SortedSet<String> further = getAllSubTypes(subType);
                    localSubTypes.addAll(further);
                }
            }
        }
        return localSubTypes;
    }

    private void resolveSubTypes() {
        originalSubTypes = entityDef.getSubTypes();
        if (originalSubTypes != null && !originalSubTypes.isEmpty()) {
            subTypes = new ArrayList<>();
            fullSubTypes = new ArrayList<>();
            for (String originalSubType : originalSubTypes) {
                AssetGenerator sub = cache.getCachedAssetType(originalSubType);
                if (sub != null) {
                    fullSubTypes.add(sub);
                }
                if (cfg.includeTypedef(sub.getEntityDef())) {
                    MappedType subType = cache.getCachedType(originalSubType);
                    if (subType != null) {
                        subTypes.add(subType.getName());
                    } else {
                        log.warn("Mapped subType was not found: {}", originalSubType);
                    }
                }
            }
        }
        List<String> superTypes = entityDef.getSuperTypes();
        if (superTypes != null && !superTypes.isEmpty()) {
            originalSuperTypes = new ArrayList<>();
            for (String superType : superTypes) {
                AssetGenerator parent = cache.getCachedAssetType(superType);
                if (parent != null) {
                    originalSuperTypes.add(parent);
                }
            }
        }
    }

    private void resolveAttributes() {
        Set<AttributeDef> allAttributes;
        if (getOriginalName().equals("Asset")) {
            allAttributes = cache.getAllAttributesForType(getOriginalName());
        } else {
            allAttributes = cache.getAllNonAssetAttributesForType(getOriginalName());
        }
        nonInheritedAttributes = new TreeSet<>();
        for (AttributeDef attributeDef :
                cache.getEntityDefCache().get(getOriginalName()).getAttributeDefs()) {
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                nonInheritedAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        classAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : allAttributes) {
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        interfaceAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : cache.getAllAttributesForType(getOriginalName())) {
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                interfaceAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void resolveRelationships() {
        Set<RelationshipAttributeDef> allRelationships;
        if (getOriginalName().equals("Asset")) {
            allRelationships = cache.getAllRelationshipsForType(getOriginalName());
        } else {
            allRelationships = cache.getAllNonAssetRelationshipsForType(getOriginalName());
        }
        Set<String> uniqueRelationships = cache.getUniqueRelationshipsForType(getOriginalName());
        for (RelationshipAttributeDef relationshipAttributeDef :
                cache.getEntityDefCache().get(getOriginalName()).getRelationshipAttributeDefs()) {
            if (uniqueRelationships.contains(relationshipAttributeDef.getName())) {
                Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
                if (!attribute.getType().getName().equals("Internal")) {
                    nonInheritedAttributes.add(attribute);
                    checkAndAddMapContainer(attribute);
                }
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : allRelationships) {
            Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : cache.getAllRelationshipsForType(getOriginalName())) {
            Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                interfaceAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void checkAndAddMapContainer(Attribute<?> attribute) {
        if (attribute.getType().getContainer() != null
                && attribute.getType().getContainer().contains("Map")) {
            if (mapContainers == null) {
                mapContainers = new ArrayList<>();
            }
            mapContainers.add(attribute.getRenamed());
        }
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(AssetGenerator o) {
        return assetComparator.compare(this, o);
    }

    @Getter
    public static class Attribute<T extends Attribute<?>> extends AttributeGenerator implements Comparable<T> {

        enum IndexType {
            KEYWORD,
            TEXT,
            RANK_FEATURE,
            BOOLEAN,
            NUMERIC,
            STEMMED,
            RELATION,
            S_RELATION,
            ;
        }

        // Sort attribute definitions in a set based purely on their name (two attributes
        // in the same set with the same name should be a conflict / duplicate)
        private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
        private static final Comparator<Attribute<?>> attributeComparator =
                Comparator.comparing(Attribute::getRenamed, stringComparator);

        private String searchType;
        private String searchTypeArgs;

        protected Attribute(GeneratorConfig cfg) {
            super(cfg);
        }

        public Attribute(String className, AttributeDef attributeDef, GeneratorConfig cfg) {
            super(className, attributeDef, cfg);
        }

        @Override
        protected void resolveName() {
            super.resolveName();
            setRenamed(cfg.resolveAttributeName(getOriginalName()));
            setSnakeCaseRenamed(StringUtils.getLowerSnakeCase(getRenamed()));
        }

        @Override
        protected void resolveType(AttributeDef attributeDef) {
            super.resolveType(attributeDef);
            String enumName = cfg.resolveAttributeToEnumeration(getOriginalName());
            if (enumName != null) {
                setType(getType().toBuilder()
                        .name(enumName)
                        .type(MappedType.Type.ENUM)
                        .build());
            }
            String assetTypeOverride = cfg.resolveAttributeToTypeOverride(className, getOriginalName());
            if (assetTypeOverride != null) {
                setType(getType().toBuilder()
                        .name(assetTypeOverride)
                        .type(MappedType.Type.ASSET)
                        .build());
                setRetyped(true);
            }
            Map<IndexType, String> searchMap = getIndexesForAttribute(attributeDef);
            Set<IndexType> indices = searchMap.keySet();
            if (!indices.isEmpty()) {
                if (indices.equals(Set.of(IndexType.RELATION))) {
                    searchType = "RelationField";
                    searchTypeArgs = null;
                } else if (indices.equals(Set.of(IndexType.S_RELATION))) {
                    searchType = "SearchableRelationship";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.S_RELATION) + "\"";
                } else if (indices.equals(Set.of(IndexType.KEYWORD))) {
                    searchType = "KeywordField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.KEYWORD) + "\"";
                } else if (indices.equals(Set.of(IndexType.TEXT))) {
                    searchType = "TextField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.TEXT) + "\"";
                } else if (indices.equals(Set.of(IndexType.NUMERIC))) {
                    searchType = "NumericField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.NUMERIC) + "\"";
                } else if (indices.equals(Set.of(IndexType.BOOLEAN))) {
                    searchType = "BooleanField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.BOOLEAN) + "\"";
                } else if (indices.equals(Set.of(IndexType.NUMERIC, IndexType.RANK_FEATURE))) {
                    searchType = "NumericRankField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.NUMERIC) + "\", \""
                            + searchMap.get(IndexType.RANK_FEATURE) + "\"";
                } else if (indices.equals(Set.of(IndexType.KEYWORD, IndexType.TEXT))) {
                    searchType = "KeywordTextField";
                    searchTypeArgs =
                            "\"" + searchMap.get(IndexType.KEYWORD) + "\", \"" + searchMap.get(IndexType.TEXT) + "\"";
                } else if (indices.equals(Set.of(IndexType.KEYWORD, IndexType.TEXT, IndexType.STEMMED))) {
                    searchType = "KeywordTextStemmedField";
                    searchTypeArgs = "\"" + searchMap.get(IndexType.KEYWORD) + "\", \"" + searchMap.get(IndexType.TEXT)
                            + "\", \"" + searchMap.get(IndexType.STEMMED) + "\"";
                } else {
                    log.warn("Found index combination for {} that is not handled: {}", getOriginalName(), indices);
                }
            }
        }

        public String getSingular() {
            if (getType().getContainer() != null) {
                return cfg.resolveSingular(getOriginalName());
            }
            return null;
        }

        public boolean isDate() {
            return getType().getOriginalBase().toLowerCase(Locale.ROOT).equals("date");
        }

        private Map<IndexType, String> getIndexesForAttribute(AttributeDef attributeDef) {

            Map<IndexType, String> searchable = new LinkedHashMap<>();

            Map<String, String> config = attributeDef.getIndexTypeESConfig();
            String attrName = attributeDef.getName();

            if (attributeDef instanceof RelationshipAttributeDef) {
                String mappedRelationship = cfg.getSearchableRelationship(attributeDef.getName());
                String relationshipType = ((RelationshipAttributeDef) attributeDef).getRelationshipTypeName();
                if (mappedRelationship != null && mappedRelationship.equals(relationshipType)) {
                    // Pull few searchable relationships from overall configuration
                    searchable.put(IndexType.S_RELATION, relationshipType);
                } else {
                    // Park all other relationship attributes, as they will generally not be searchable
                    searchable.put(IndexType.RELATION, attrName);
                }
            } else {

                // Default index
                if (config != null && config.containsKey("analyzer")) {
                    String analyzer = config.get("analyzer");
                    if (analyzer.equals("atlan_text_analyzer")) {
                        if (attrName.endsWith(".stemmed")) {
                            searchable.put(IndexType.STEMMED, attrName);
                        } else {
                            searchable.put(IndexType.TEXT, attrName);
                        }
                    } else {
                        log.warn("Unknown analyzer on attribute {}: {}", attrName, analyzer);
                    }
                } else {
                    IndexType defIndex = getDefaultIndexForType(getType());
                    searchable.put(defIndex, attrName);
                }

                boolean duplicate = false;
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
                                    duplicate = searchable.put(IndexType.KEYWORD, fieldName) != null;
                                    break;
                                case "text":
                                    if (fieldName.endsWith(".stemmed")) {
                                        duplicate = searchable.put(IndexType.STEMMED, fieldName) != null;
                                    } else {
                                        duplicate = searchable.put(IndexType.TEXT, fieldName) != null;
                                    }
                                    break;
                                case "rank_feature":
                                    duplicate = searchable.put(IndexType.RANK_FEATURE, fieldName) != null;
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
                            IndexType defIndex = getDefaultIndexForType(getType());
                            duplicate = searchable.put(defIndex, fieldName) != null;
                        }
                    }
                }

                if (duplicate) {
                    log.info("Same attribute had multiple (identical) index references: {}", attrName);
                }
            }

            return searchable;
        }

        /**
         * Lookup the default index for the provided attribute data type.
         *
         * @param type mapped type of the attribute
         * @return the default index for that data type
         */
        private static IndexType getDefaultIndexForType(MappedType type) {
            String baseType = type.getOriginalBase();
            IndexType toUse;
            switch (baseType) {
                case "date":
                case "float":
                case "double":
                case "int":
                case "long":
                    toUse = IndexType.NUMERIC;
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

        public String getEnumForAttr() {
            return getRenamed()
                    .replaceAll("_", "")
                    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                    .replaceAll("([a-z])([A-Z])", "$1_$2")
                    .toUpperCase(Locale.ROOT);
        }

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("rawtypes")
        public int compareTo(Attribute o) {
            return attributeComparator.compare(this, o);
        }
    }
}
