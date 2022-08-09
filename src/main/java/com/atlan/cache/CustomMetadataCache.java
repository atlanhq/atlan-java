package com.atlan.cache;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.LogicException;
import com.atlan.model.CustomMetadata;
import com.atlan.model.responses.TypeDefResponse;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.BusinessMetadataDef;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for translating between Atlan-internal ID strings and human-readable names for
 * custom metadata (including attributes).
 */
@Slf4j
public class CustomMetadataCache {

    private static Map<String, BusinessMetadataDef> cacheById = new ConcurrentHashMap<>();
    private static Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private static Map<String, String> mapNameToId = new ConcurrentHashMap<>();

    private static Map<String, Map<String, String>> mapAttrIdToName = new ConcurrentHashMap<>();
    private static Map<String, Map<String, String>> mapAttrNameToId = new ConcurrentHashMap<>();

    private static synchronized void refreshCache() throws AtlanException {
        log.debug("Refreshing cache of custom metadata...");
        TypeDefResponse response = TypeDefsEndpoint.getTypeDefs("business_metadata");
        List<BusinessMetadataDef> customMetadata;
        if (response != null) {
            customMetadata = response.getBusinessMetadataDefs();
        } else {
            customMetadata = Collections.emptyList();
        }
        cacheById = new ConcurrentHashMap<>();
        mapIdToName = new ConcurrentHashMap<>();
        mapNameToId = new ConcurrentHashMap<>();
        mapAttrIdToName = new ConcurrentHashMap<>();
        mapAttrNameToId = new ConcurrentHashMap<>();
        for (BusinessMetadataDef bmDef : customMetadata) {
            String typeId = bmDef.getName();
            cacheById.put(typeId, bmDef);
            mapIdToName.put(typeId, bmDef.getDisplayName());
            mapNameToId.put(bmDef.getDisplayName(), typeId);
            mapAttrIdToName.put(typeId, new ConcurrentHashMap<>());
            mapAttrNameToId.put(typeId, new ConcurrentHashMap<>());
            for (AttributeDef attributeDef : bmDef.getAttributeDefs()) {
                mapAttrIdToName.get(typeId).put(attributeDef.getName(), attributeDef.getDisplayName());
                mapAttrNameToId.get(typeId).put(attributeDef.getDisplayName(), attributeDef.getName());
            }
        }
    }

    /**
     * Translate the provided human-readable custom metadata set name to the Atlan-internal ID string.
     * @param name human-readable name of the custom metadata set
     * @return Atlan-internal ID string of the custom metadata set
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    private static String getIdForName(String name) throws AtlanException {
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
     * @param id Atlan-internal ID string of the custom metadata set
     * @return human-readable name of the custom metadata set
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    private static String getNameForId(String id) throws AtlanException {
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
     * Translate the provided human-readable custom metadata attribute name to the Atlan-internal ID string.
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
     * Translate the provided custom metadata object into a business attributes object.
     * We receive the businessAttributes object (rather than creating a new one) to initialize it with
     * any existing business attributes.
     * @param customMetadata custom metadata object
     * @param businessAttributes business attributes object, which will be changed
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static void getBusinessAttributesFromCustomMetadata(
            CustomMetadata customMetadata, Map<String, Map<String, Object>> businessAttributes) throws AtlanException {
        if (customMetadata != null) {
            for (String cmName : customMetadata.listSets()) {
                String cmId = getIdForName(cmName);
                Map<String, Object> bmAttrs = businessAttributes.getOrDefault(cmId, new LinkedHashMap<>());
                for (String attrName : customMetadata.listAttributesForSet(cmName)) {
                    String cmAttrId = getAttrIdForName(cmId, attrName);
                    bmAttrs.put(cmAttrId, customMetadata.getValueForAttribute(cmName, attrName));
                }
                businessAttributes.put(cmId, bmAttrs);
            }
        }
    }

    /**
     * Translate the provided business attributes object into a custom metadata object.
     * @param businessAttributes business attributes object
     * @return custom metadata object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     */
    public static CustomMetadata getCustomMetadataFromBusinessAttributes(JsonObject businessAttributes)
            throws AtlanException {
        CustomMetadata.CustomMetadataBuilder builder = CustomMetadata.builder();
        for (String cmId : businessAttributes.keySet()) {
            String cmName = getNameForId(cmId);
            JsonObject bmAttrs = businessAttributes.get(cmId).getAsJsonObject();
            for (String attrId : bmAttrs.keySet()) {
                String cmAttrName = getAttrNameForId(cmId, attrId);
                JsonElement jsonValue = bmAttrs.get(attrId);
                if (jsonValue.isJsonArray()) {
                    Set<Object> values = new HashSet<>();
                    JsonArray array = jsonValue.getAsJsonArray();
                    for (JsonElement element : array) {
                        Object primitive = deserializePrimitive(element);
                        values.add(primitive);
                    }
                    if (!values.isEmpty()) {
                        // It seems assets that previously had multivalued custom metadata that was later
                        // removed retain an empty set for that attribute, but this is equivalent to the
                        // custom metadata not existing from a UI and delete-ability perspective (so we will
                        // treat as non-existent in the deserialization as well)
                        builder.withAttribute(cmName, cmAttrName, values);
                    }
                } else if (jsonValue.isJsonPrimitive()) {
                    Object primitive = deserializePrimitive(jsonValue);
                    builder.withAttribute(cmName, cmAttrName, primitive);
                } else {
                    throw new LogicException(
                            "Unable to deserialize non-primitive custom metadata value.",
                            jsonValue.getAsString(),
                            "ATLAN-CLIENT-CM-500-002",
                            500);
                }
            }
        }
        return builder.build();
    }

    private static Object deserializePrimitive(JsonElement jsonValue) throws LogicException {
        if (jsonValue.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonValue.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            } else if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            } else if (primitive.isNumber()) {
                return primitive.getAsNumber();
            } else {
                throw new LogicException(
                        "Unable to deserialize unrecognized primitive custom metadata value.",
                        primitive.getAsString(),
                        "ATLAN-CLIENT-CM-500-001",
                        500);
            }
        } else {
            throw new LogicException(
                    "Unable to deserialize non-primitive custom metadata value.",
                    jsonValue.getAsString(),
                    "ATLAN-CLIENT-CM-500-002",
                    500);
        }
    }
}
