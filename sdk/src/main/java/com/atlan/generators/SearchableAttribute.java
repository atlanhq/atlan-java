/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import com.atlan.util.StringUtils;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SearchableAttribute<T extends SearchableAttribute<?>> extends AttributeGenerator implements Comparable<T> {

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
    private static final Comparator<SearchableAttribute<?>> attributeComparator =
            Comparator.comparing(SearchableAttribute::getRenamed, stringComparator);

    private String searchType;
    private String searchTypeArgs;

    protected SearchableAttribute(AtlanClient client, GeneratorConfig cfg) {
        super(client, cfg);
    }

    public SearchableAttribute(AtlanClient client, String className, AttributeDef attributeDef, GeneratorConfig cfg) {
        super(client, className, attributeDef, cfg);
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
        Map<SearchableAttribute.IndexType, String> searchMap = getIndexesForAttribute(attributeDef);
        Set<SearchableAttribute.IndexType> indices = searchMap.keySet();
        if (!indices.isEmpty()) {
            if (indices.equals(Set.of(SearchableAttribute.IndexType.RELATION))) {
                searchType = "RelationField";
                searchTypeArgs = null;
            } else if (indices.equals(Set.of(SearchableAttribute.IndexType.S_RELATION))) {
                searchType = "SearchableRelationship";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.S_RELATION) + "\"";
            } else if (indices.equals(Set.of(SearchableAttribute.IndexType.KEYWORD))) {
                searchType = "KeywordField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.KEYWORD) + "\"";
            } else if (indices.equals(Set.of(SearchableAttribute.IndexType.TEXT))) {
                searchType = "TextField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.TEXT) + "\"";
            } else if (indices.equals(Set.of(SearchableAttribute.IndexType.NUMERIC))) {
                searchType = "NumericField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.NUMERIC) + "\"";
            } else if (indices.equals(Set.of(SearchableAttribute.IndexType.BOOLEAN))) {
                searchType = "BooleanField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.BOOLEAN) + "\"";
            } else if (indices.equals(
                    Set.of(SearchableAttribute.IndexType.NUMERIC, SearchableAttribute.IndexType.RANK_FEATURE))) {
                searchType = "NumericRankField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.NUMERIC) + "\", \""
                        + searchMap.get(SearchableAttribute.IndexType.RANK_FEATURE) + "\"";
            } else if (indices.equals(
                    Set.of(SearchableAttribute.IndexType.KEYWORD, SearchableAttribute.IndexType.TEXT))) {
                searchType = "KeywordTextField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.KEYWORD) + "\", \""
                        + searchMap.get(SearchableAttribute.IndexType.TEXT) + "\"";
            } else if (indices.equals(Set.of(
                    SearchableAttribute.IndexType.KEYWORD,
                    SearchableAttribute.IndexType.TEXT,
                    SearchableAttribute.IndexType.STEMMED))) {
                searchType = "KeywordTextStemmedField";
                searchTypeArgs = "\"" + searchMap.get(SearchableAttribute.IndexType.KEYWORD) + "\", \""
                        + searchMap.get(SearchableAttribute.IndexType.TEXT) + "\", \""
                        + searchMap.get(SearchableAttribute.IndexType.STEMMED) + "\"";
            } else {
                log.warn("Found index combination for {} that is not handled: {}", getOriginalName(), indices);
            }
        }
    }

    public boolean isDate() {
        return getType().getOriginalBase().toLowerCase(Locale.ROOT).equals("date");
    }

    private Map<SearchableAttribute.IndexType, String> getIndexesForAttribute(AttributeDef attributeDef) {

        Map<SearchableAttribute.IndexType, String> searchable = new LinkedHashMap<>();

        Map<String, String> config = attributeDef.getIndexTypeESConfig();
        String attrName = attributeDef.getName();

        if (attributeDef instanceof RelationshipAttributeDef) {
            String mappedRelationship = cfg.getSearchableRelationship(attributeDef.getName());
            String relationshipType = ((RelationshipAttributeDef) attributeDef).getRelationshipTypeName();
            if (mappedRelationship != null && mappedRelationship.equals(relationshipType)) {
                // Pull few searchable relationships from overall configuration
                searchable.put(SearchableAttribute.IndexType.S_RELATION, relationshipType);
            } else {
                // Park all other relationship attributes, as they will generally not be searchable
                searchable.put(SearchableAttribute.IndexType.RELATION, attrName);
            }
        } else {

            // Default index
            if (config != null && config.containsKey("analyzer")) {
                String analyzer = config.get("analyzer");
                if (analyzer.equals("atlan_text_analyzer")) {
                    if (attrName.endsWith(".stemmed")) {
                        searchable.put(SearchableAttribute.IndexType.STEMMED, attrName);
                    } else {
                        searchable.put(SearchableAttribute.IndexType.TEXT, attrName);
                    }
                } else {
                    log.warn("Unknown analyzer on attribute {}: {}", attrName, analyzer);
                }
            } else if (attributeDef.getIndexType() != null
                    && attributeDef.getIndexType().toLowerCase(Locale.ROOT).equals("string")) {
                searchable.put(SearchableAttribute.IndexType.KEYWORD, attrName);
            } else {
                SearchableAttribute.IndexType defIndex = getDefaultIndexForType(getType());
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
                                duplicate = searchable.put(SearchableAttribute.IndexType.KEYWORD, fieldName) != null;
                                break;
                            case "text":
                                if (fieldName.endsWith(".stemmed")) {
                                    duplicate =
                                            searchable.put(SearchableAttribute.IndexType.STEMMED, fieldName) != null;
                                } else {
                                    duplicate = searchable.put(SearchableAttribute.IndexType.TEXT, fieldName) != null;
                                }
                                break;
                            case "rank_feature":
                                duplicate =
                                        searchable.put(SearchableAttribute.IndexType.RANK_FEATURE, fieldName) != null;
                                break;
                            case "date":
                                // Ignore -- nothing special to do for this one
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
                        SearchableAttribute.IndexType defIndex = getDefaultIndexForType(getType());
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
    private static SearchableAttribute.IndexType getDefaultIndexForType(MappedType type) {
        String baseType = type.getOriginalBase();
        SearchableAttribute.IndexType toUse;
        switch (baseType) {
            case "date":
            case "float":
            case "double":
            case "int":
            case "long":
                toUse = SearchableAttribute.IndexType.NUMERIC;
                break;
            case "boolean":
                toUse = SearchableAttribute.IndexType.BOOLEAN;
                break;
            case "string":
                toUse = SearchableAttribute.IndexType.TEXT;
                break;
            default:
                toUse = SearchableAttribute.IndexType.KEYWORD;
                break;
        }
        return toUse;
    }

    public String getEnumForAttr() {
        return StringUtils.getUpperSnakeCase(getRenamed());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("rawtypes")
    public int compareTo(SearchableAttribute o) {
        return attributeComparator.compare(this, o);
    }
}
