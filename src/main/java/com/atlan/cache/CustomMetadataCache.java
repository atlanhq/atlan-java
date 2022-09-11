/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.LogicException;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.CustomMetadataDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.serde.Removable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * custom metadata (including attributes).
 */
@Slf4j
public class CustomMetadataCache {

    private static Map<String, CustomMetadataDef> cacheById = new ConcurrentHashMap<>();
    private static Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private static Map<String, String> mapNameToId = new ConcurrentHashMap<>();

    private static Map<String, Map<String, String>> mapAttrIdToName = new ConcurrentHashMap<>();
    private static Map<String, Map<String, String>> mapAttrNameToId = new ConcurrentHashMap<>();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of custom metadata...");
        TypeDefResponse response = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.CUSTOM_METADATA);
        List<CustomMetadataDef> customMetadata;
        if (response != null) {
            customMetadata = response.getCustomMetadataDefs();
        } else {
            customMetadata = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        mapAttrIdToName = new ConcurrentHashMap<>();
        mapAttrNameToId = new ConcurrentHashMap<>();
        for (CustomMetadataDef bmDef : customMetadata) {
            String typeId = bmDef.getName();
            cacheById.put(typeId, bmDef);
            mapIdToName.put(typeId, bmDef.getDisplayName());
            mapNameToId.put(bmDef.getDisplayName(), typeId);
            mapAttrIdToName.put(typeId, new ConcurrentHashMap<>());
            mapAttrNameToId.put(typeId, new ConcurrentHashMap<>());
            for (AttributeDef attributeDef : bmDef.getAttributeDefs()) {
                String attrId = attributeDef.getName();
                mapAttrIdToName.get(typeId).put(attrId, attributeDef.getDisplayName());
                mapAttrNameToId.get(typeId).put(attributeDef.getDisplayName(), attributeDef.getName());
            }
        }
    }

    /**
     * Translate the provided human-readable custom metadata set name to the Atlan-internal ID string.
     *
     * @param name human-readable name of the custom metadata set
     * @return Atlan-internal ID string of the custom metadata set
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getIdForName(String name) throws AtlanException {
        String cmId = mapNameToId.get(name);
        if (cmId != null) {
            // If found, return straight away
            return cmId;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapNameToId.get(name);
        }
    }

    /**
     * Translate the provided Atlan-internal custom metadata ID string to the human-readable custom metadata set name.
     *
     * @param id Atlan-internal ID string of the custom metadata set
     * @return human-readable name of the custom metadata set
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static String getNameForId(String id) throws AtlanException {
        String cmName = mapIdToName.get(id);
        if (cmName != null) {
            // If found, return straight away
            return cmName;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            return mapIdToName.get(id);
        }
    }

    /**
     * Retrieve all the custom metadata attributes. The map will be keyed by custom metadata set
     * name, and the value will be a listing of all the attributes within that set (with all the details
     * of each of those attributes).
     *
     * @return a map from custom metadata set name to all details about all its attributes
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static Map<String, List<AttributeDef>> getAllCustomAttributes() throws AtlanException {
        return getAllCustomAttributes(false);
    }

    /**
     * Retrieve all the custom metadata attributes. The map will be keyed by custom metadata set
     * name, and the value will be a listing of all the attributes within that set (with all the details
     * of each of those attributes).
     *
     * @param forceRefresh if true, will refresh the custom metadata cache; if false, will only refresh
     *                     the cache if it is empty
     * @return a map from custom metadata set name to all details about all its attributes
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static Map<String, List<AttributeDef>> getAllCustomAttributes(boolean forceRefresh) throws AtlanException {
        if (cacheById.isEmpty() || forceRefresh) {
            refreshCache();
        }
        Map<String, List<AttributeDef>> map = new HashMap<>();
        for (Map.Entry<String, CustomMetadataDef> entry : cacheById.entrySet()) {
            String typeId = entry.getKey();
            String typeName = getNameForId(typeId);
            CustomMetadataDef typeDef = entry.getValue();
            map.put(typeName, typeDef.getAttributeDefs());
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Translate the provided human-readable custom metadata attribute name to the Atlan-internal ID string.
     *
     * @param setId Atlan-internal ID string for the custom metadata set
     * @param attributeName human-readable name of the attribute
     * @return Atlan-internal ID string for the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    private static String getAttrIdForName(String setId, String attributeName) throws AtlanException {
        String attrId = null;
        Map<String, String> subMap = mapAttrNameToId.get(setId);
        if (subMap != null) {
            attrId = subMap.get(attributeName);
        }
        if (attrId != null) {
            // If found, return straight away
            return attrId;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            subMap = mapAttrNameToId.get(setId);
            if (subMap != null) {
                return subMap.get(attributeName);
            } else {
                return null;
            }
        }
    }

    /**
     * Translate the provided Atlan-internal ID for a custom metadata attribute to the human-readable attribute name.
     *
     * @param setId Atlan-internal ID string for the custom metadata set
     * @param attributeId Atlan-internal ID string for the attribute
     * @return human-readable name of the attribute
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    private static String getAttrNameForId(String setId, String attributeId) throws AtlanException {
        String attrName = null;
        Map<String, String> subMap = mapAttrIdToName.get(setId);
        if (subMap != null) {
            attrName = subMap.get(attributeId);
        }
        if (attrName != null) {
            // If found, return straight away
            return attrName;
        } else {
            // Otherwise, refresh the cache and look again (could be stale)
            refreshCache();
            subMap = mapAttrIdToName.get(setId);
            if (subMap != null) {
                return subMap.get(attributeId);
            } else {
                return null;
            }
        }
    }

    /**
     * Construct a full set of attributes for the given custom metadata, but where all the values are null.
     *
     * @param customMetadataName for which to construct the empty map
     * @return a map from custom metadata attribute name (human-readable) to null values
     * @throws AtlanException on any API issues
     */
    public static Map<String, Object> getEmptyAttributes(String customMetadataName) throws AtlanException {
        String cmId = getIdForName(customMetadataName);
        Map<String, String> attributes = mapAttrNameToId.get(cmId);
        Map<String, Object> empty = new LinkedHashMap<>();
        for (String attrName : attributes.keySet()) {
            empty.put(attrName, Removable.NULL);
        }
        return empty;
    }

    /**
     * Translate the provided custom metadata object into a business attributes object.
     * We receive the businessAttributes object (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadata custom metadata object
     * @param businessAttributes business attributes object, which will be changed
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static void getBusinessAttributesFromCustomMetadata(
            Map<String, CustomMetadataAttributes> customMetadata, Map<String, Map<String, Object>> businessAttributes)
            throws AtlanException {
        if (customMetadata != null) {
            for (Map.Entry<String, CustomMetadataAttributes> entry : customMetadata.entrySet()) {
                String cmName = entry.getKey();
                String cmId = getIdForName(cmName);
                CustomMetadataAttributes attrs = entry.getValue();
                Map<String, Object> bmAttrs = businessAttributes.getOrDefault(cmId, new LinkedHashMap<>());
                getIdMapFromNameMap(cmName, attrs.getAttributes(), bmAttrs);
                businessAttributes.put(cmId, bmAttrs);
            }
        }
    }

    /**
     * Translate the provided map keyed by human-readable attribute name into a map of keyed by attribute id.
     * We receive the idToValue map (rather than creating a new one) to initialize it with
     * any existing business attributes.
     *
     * @param customMetadataName human-readable name of the custom metadata
     * @param nameToValue the attributes and their values for the custom metadata
     * @param idToValue the business metadata to map into
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static void getIdMapFromNameMap(
            String customMetadataName, Map<String, Object> nameToValue, Map<String, Object> idToValue)
            throws AtlanException {
        String cmId = getIdForName(customMetadataName);
        for (Map.Entry<String, Object> entry : nameToValue.entrySet()) {
            String attrName = entry.getKey();
            String cmAttrId = getAttrIdForName(cmId, attrName);
            idToValue.put(cmAttrId, entry.getValue());
        }
    }

    /**
     * Translate the provided business attributes object into a custom metadata object.
     *
     * @param businessAttributes business attributes object
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static Map<String, CustomMetadataAttributes> getCustomMetadataFromBusinessAttributes(
            JsonNode businessAttributes) throws AtlanException {
        Map<String, CustomMetadataAttributes> map = new LinkedHashMap<>();
        Iterator<String> itrCM = businessAttributes.fieldNames();
        while (itrCM.hasNext()) {
            String cmId = itrCM.next();
            String cmName = getNameForId(cmId);
            JsonNode bmAttrs = businessAttributes.get(cmId);
            CustomMetadataAttributes.CustomMetadataAttributesBuilder<?, ?> builder = CustomMetadataAttributes.builder();
            Iterator<String> itrCMA = bmAttrs.fieldNames();
            while (itrCMA.hasNext()) {
                String attrId = itrCMA.next();
                String cmAttrName = getAttrNameForId(cmId, attrId);
                JsonNode jsonValue = bmAttrs.get(attrId);
                if (jsonValue.isArray()) {
                    Set<Object> values = new HashSet<>();
                    ArrayNode array = (ArrayNode) jsonValue;
                    for (JsonNode element : array) {
                        Object primitive = deserializePrimitive(element);
                        values.add(primitive);
                    }
                    if (!values.isEmpty()) {
                        // It seems assets that previously had multivalued custom metadata that was later
                        // removed retain an empty set for that attribute, but this is equivalent to the
                        // custom metadata not existing from a UI and delete-ability perspective (so we will
                        // treat as non-existent in the deserialization as well)
                        builder.attribute(cmAttrName, values);
                    }
                } else if (jsonValue.isValueNode()) {
                    Object primitive = deserializePrimitive(jsonValue);
                    builder.attribute(cmAttrName, primitive);
                } else {
                    throw new LogicException(
                            "Unable to deserialize non-primitive custom metadata value: " + jsonValue,
                            "ATLAN-CLIENT-CM-500-002",
                            500);
                }
            }
            CustomMetadataAttributes cma = builder.build();
            if (!cma.isEmpty()) {
                map.put(cmName, builder.build());
            }
        }
        return map;
    }

    private static Object deserializePrimitive(JsonNode jsonValue) throws LogicException {
        if (jsonValue.isValueNode()) {
            if (jsonValue.isTextual()) {
                return jsonValue.asText();
            } else if (jsonValue.isBoolean()) {
                return jsonValue.asBoolean();
            } else if (jsonValue.isIntegralNumber()) {
                return jsonValue.asLong();
            } else if (jsonValue.isFloatingPointNumber()) {
                return jsonValue.asDouble();
            } else {
                throw new LogicException(
                        "Unable to deserialize unrecognized primitive custom metadata value: " + jsonValue,
                        "ATLAN-CLIENT-CM-500-001",
                        500);
            }
        } else {
            throw new LogicException(
                    "Unable to deserialize non-primitive custom metadata value:" + jsonValue,
                    "ATLAN-CLIENT-CM-500-002",
                    500);
        }
    }
}
