/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.util.StringUtils;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SearchFieldGenerator extends TypeGenerator {

    public static final String DIRECTORY = "enums";

    enum IndexType {
        KEYWORD("KeywordFields"),
        TEXT("TextFields"),
        RANK_FEATURE("RankFields"),
        BOOLEAN("BooleanFields"),
        NUMERIC("NumericFields"),
        DATE("NumericFields"),
        FLOAT("NumericFields"),
        STEMMED("StemmedFields");

        @Getter
        private final String className;

        IndexType(String className) {
            this.className = className;
        }
    }

    private final IndexType toGenerate;
    private SortedSet<Field> fields;

    private static final List<FieldDetails> COMMON_KEYWORDS = List.of(
            new FieldDetails(
                    "GUID",
                    "__guid",
                    "Globally unique identifier (GUID) of any object in Atlan.",
                    SearchFieldGenerator.IndexType.KEYWORD),
            new FieldDetails(
                    "CREATED_BY",
                    "__createdBy",
                    "Atlan user who created this asset.",
                    SearchFieldGenerator.IndexType.KEYWORD),
            new FieldDetails(
                    "MODIFIED_BY",
                    "__modifiedBy",
                    "Atlan user who last updated the asset.",
                    SearchFieldGenerator.IndexType.KEYWORD),
            new FieldDetails(
                    "STATE",
                    "__state",
                    "Asset status in Atlan (active vs deleted).",
                    SearchFieldGenerator.IndexType.KEYWORD),
            new FieldDetails(
                    "TRAIT_NAMES",
                    "__traitNames",
                    "All directly-assigned Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "PROPAGATED_TRAIT_NAMES",
                    "__propagatedTraitNames",
                    "All propagated Atlan tags that exist on an asset, searchable by the internal hashed-string ID of the Atlan tag.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "ASSIGNED_TERMS",
                    "__meanings",
                    "All terms attached to an asset, searchable by the term's qualifiedName.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "TYPE_NAME",
                    "__typeName.keyword",
                    "Type of the asset. For example Table, Column, and so on.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "SUPER_TYPE_NAMES",
                    "__superTypeNames.keyword",
                    "All super types of an asset.",
                    SearchFieldGenerator.IndexType.KEYWORD),
            new FieldDetails(
                    "QUALIFIED_NAME",
                    "qualifiedName",
                    "Unique fully-qualified name of the asset in Atlan.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "GLOSSARY",
                    "__glossary",
                    "Glossary in which the asset is contained, searchable by the qualifiedName of the glossary.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "CATEGORIES",
                    "__categories",
                    "Categories in which the term is organized, searchable by the qualifiedName of the category.",
                    IndexType.KEYWORD),
            new FieldDetails(
                    "PARENT_CATEGORY",
                    "__parentCategory",
                    "Parent category in which a subcategory is contained, searchable by the qualifiedName of the category.",
                    IndexType.KEYWORD));

    private static final List<FieldDetails> COMMON_TEXT = List.of(
            new FieldDetails(
                    "ATLAN_TAGS_TEXT",
                    "__classificationsText",
                    "All Atlan tags that exist on an asset, whether directly assigned or propagated, searchable by the internal hashed-string ID of the Atlan tag.",
                    IndexType.TEXT),
            new FieldDetails(
                    "MEANINGS_TEXT",
                    "__meaningsText",
                    "All terms attached to an asset, as a single comma-separated string.",
                    IndexType.TEXT),
            new FieldDetails(
                    "TYPE_NAME",
                    "__typeName",
                    "Type of the asset. For example Table, Column, and so on.",
                    IndexType.TEXT),
            new FieldDetails(
                    "SUPER_TYPE_NAMES",
                    "__superTypeNames",
                    "All super types of an asset.",
                    SearchFieldGenerator.IndexType.TEXT),
            new FieldDetails(
                    "QUALIFIED_NAME",
                    "qualifiedName.text",
                    "Unique fully-qualified name of the asset in Atlan.",
                    IndexType.TEXT));

    private static final List<FieldDetails> COMMON_NUMERICS = List.of(
            new FieldDetails(
                    "TIMESTAMP",
                    "__timestamp",
                    "Time (in milliseconds) when the asset was created.",
                    IndexType.NUMERIC),
            new FieldDetails(
                    "MODIFICATION_TIMESTAMP",
                    "__modificationTimestamp",
                    "Time (in milliseconds) when the asset was last updated.",
                    IndexType.NUMERIC));

    public SearchFieldGenerator(Collection<EntityDef> entityDefs, IndexType toGenerate, GeneratorConfig cfg) {
        super(cfg);
        this.toGenerate = toGenerate;
        resolveClassName();
        resolveFields(entityDefs);
    }

    @Override
    protected void resolveClassName() {
        super.className = toGenerate.getClassName();
    }

    private void resolveFields(Collection<EntityDef> entityDefs) {
        fields = new TreeSet<>();
        switch (toGenerate) {
            case KEYWORD:
                for (FieldDetails details : COMMON_KEYWORDS) {
                    fields.add(new Field(details, cfg));
                }
                break;
            case TEXT:
                for (FieldDetails details : COMMON_TEXT) {
                    fields.add(new Field(details, cfg));
                }
                break;
            case NUMERIC:
                for (FieldDetails details : COMMON_NUMERICS) {
                    fields.add(new Field(details, cfg));
                }
                break;
            default:
                // Do nothing, no defaults to set up...
                break;
        }
        for (EntityDef entityDef : entityDefs) {
            for (AttributeDef attributeDef : entityDef.getAttributeDefs()) {
                Field field = new Field(getClassName(), entityDef.getName(), attributeDef, toGenerate, cfg);
                if (!field.getType().getName().equals("Internal") && field.getSearchFieldName() != null) {
                    if (!fields.add(field)) {
                        log.info(
                                "Skipping duplicate field {}, from asset {}", field.getEnumName(), entityDef.getName());
                    }
                    cache.addSearchFieldToCache(entityDef.getName(), attributeDef.getName(), field);
                }
            }
        }
    }

    @Getter
    private static class FieldDetails {
        private final String enumName;
        private final String searchFieldName;
        private final String description;
        private final IndexType toFilter;

        private FieldDetails(String enumName, String searchFieldName, String description, IndexType toFilter) {
            this.enumName = enumName;
            this.searchFieldName = searchFieldName;
            this.description = description;
            this.toFilter = toFilter;
        }
    }

    @Getter
    public static class Field extends AssetGenerator.Attribute<Field> implements Comparable<Field> {

        private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
        private static final Comparator<Field> comparator =
                Comparator.comparing(Field::getEnumName, stringComparator).thenComparing(Field::getToFilter);

        private String searchFieldName;
        private String enumName;
        private final SearchFieldGenerator.IndexType toFilter;

        private Field(FieldDetails details, GeneratorConfig cfg) {
            super(cfg);
            this.enumName = details.getEnumName();
            this.searchFieldName = details.getSearchFieldName();
            super.description = details.getDescription();
            this.toFilter = details.getToFilter();
        }

        public Field(
                String className,
                String entityName,
                AttributeDef attributeDef,
                SearchFieldGenerator.IndexType toFilter,
                GeneratorConfig cfg) {
            super(className, attributeDef, cfg);
            this.toFilter = toFilter;
            resolveSearchDetails(attributeDef);
            this.description = cache.getAttributeDescription(entityName, getOriginalName());
        }

        private void resolveSearchDetails(AttributeDef attributeDef) {
            searchFieldName = getSearchFieldForAttribute(attributeDef);
            if (searchFieldName != null) {
                enumName = StringUtils.getUpperSnakeCase(getRenamed());
            }
        }

        private String getSearchFieldForAttribute(AttributeDef attributeDef) {

            Set<String> searchable = new LinkedHashSet<>();

            Map<String, String> config = attributeDef.getIndexTypeESConfig();
            String attrName = attributeDef.getName();

            // Default index
            if (config != null && config.containsKey("analyzer")) {
                String analyzer = config.get("analyzer");
                if (analyzer.equals("atlan_text_analyzer")) {
                    if (toFilter == SearchFieldGenerator.IndexType.STEMMED && attrName.endsWith(".stemmed")) {
                        searchable.add(attrName);
                    } else if (toFilter == SearchFieldGenerator.IndexType.TEXT && !attrName.endsWith(".stemmed")) {
                        searchable.add(attrName);
                    }
                } else {
                    log.warn("Unknown analyzer on attribute {}: {}", attrName, analyzer);
                }
            } else {
                SearchFieldGenerator.IndexType defIndex = getDefaultIndexForType(getType());
                if (defIndex == toFilter
                        || (toFilter == SearchFieldGenerator.IndexType.NUMERIC
                                && (defIndex == SearchFieldGenerator.IndexType.DATE
                                        || defIndex == SearchFieldGenerator.IndexType.FLOAT))) {
                    searchable.add(attrName);
                }
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
                                if (toFilter == SearchFieldGenerator.IndexType.KEYWORD) {
                                    duplicate = searchable.add(fieldName);
                                }
                                break;
                            case "text":
                                if (toFilter == SearchFieldGenerator.IndexType.STEMMED
                                        && fieldName.endsWith(".stemmed")) {
                                    duplicate = searchable.add(fieldName);
                                } else if (toFilter == SearchFieldGenerator.IndexType.TEXT
                                        && !fieldName.endsWith(".stemmed")) {
                                    duplicate = searchable.add(fieldName);
                                }
                                break;
                            case "rank_feature":
                                if (toFilter == SearchFieldGenerator.IndexType.RANK_FEATURE) {
                                    duplicate = searchable.add(fieldName);
                                }
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
                        SearchFieldGenerator.IndexType defIndex = getDefaultIndexForType(getType());
                        if (defIndex == toFilter
                                || (toFilter == SearchFieldGenerator.IndexType.NUMERIC
                                        && (defIndex == SearchFieldGenerator.IndexType.DATE
                                                || defIndex == SearchFieldGenerator.IndexType.FLOAT))) {
                            duplicate = searchable.add(fieldName);
                        }
                    }
                }
            }

            if (duplicate) {
                log.info("Same attribute had multiple (identical) index references: {}", attrName);
            }
            if (searchable.size() > 1) {
                log.warn(
                        "Multiple searchable fields of the same index type for attribute {}: {}", attrName, searchable);
            }

            // Return only the first element of the set, if any
            if (!searchable.isEmpty()) {
                return searchable.iterator().next();
            }
            return null;
        }

        public String getEnumClassName() {
            return toFilter.getClassName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Field o) {
            return comparator.compare(this, o);
        }

        /**
         * Lookup the default index for the provided attribute data type.
         *
         * @param type mapped type of the attribute
         * @return the default index for that data type
         */
        private static SearchFieldGenerator.IndexType getDefaultIndexForType(MappedType type) {
            String baseType = type.getOriginalBase();
            SearchFieldGenerator.IndexType toUse;
            switch (baseType) {
                case "date":
                    toUse = SearchFieldGenerator.IndexType.DATE;
                    break;
                case "float":
                case "double":
                case "int":
                case "long":
                    toUse = SearchFieldGenerator.IndexType.FLOAT;
                    break;
                case "boolean":
                    toUse = SearchFieldGenerator.IndexType.BOOLEAN;
                    break;
                case "string":
                default:
                    toUse = SearchFieldGenerator.IndexType.KEYWORD;
                    break;
            }
            return toUse;
        }
    }
}
