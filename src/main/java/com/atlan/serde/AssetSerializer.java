/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.AtlanTagCache;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Serialization of all {@link Asset} objects, down through the entire inheritance hierarchy.
 * This custom serialization is necessary to create some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating custom metadata into the nested <code>businessAttributes</code> structure, including translating human-readable names into Atlan's internal hashed-string representations.</li>
 * </ul>
 */
public class AssetSerializer extends StdSerializer<Asset> {
    private static final long serialVersionUID = 2L;

    public AssetSerializer() {
        this(null);
    }

    public AssetSerializer(Class<Asset> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            Asset value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Asset asset, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        Class<?> clazz = asset.getClass();

        Set<String> nullFields = asset.getNullFields();

        Map<String, Object> attributes = new LinkedHashMap<>();
        Map<String, Map<String, Object>> businessAttributes = new LinkedHashMap<>();

        gen.writeStartObject();

        try {
            for (String fieldName : ReflectionCache.getFieldNames(clazz)) {
                if (ReflectionCache.isAttribute(clazz, fieldName)) {
                    // If the field should be attribute-nested...
                    Object attrValue;
                    if (nullFields.contains(fieldName)) {
                        // If the value should be serialized as null, then
                        // set the value to the serializable null
                        Class<?> type = ReflectionCache.getFieldType(clazz, fieldName);
                        if (type == List.class || type == Set.class || type == SortedSet.class) {
                            attrValue = Removable.EMPTY_LIST;
                        } else {
                            attrValue = Removable.NULL;
                        }
                    } else {
                        // Otherwise, pickup the value from the top-level
                        // attribute so that we can move that value across
                        attrValue = ReflectionCache.getGetter(clazz, fieldName).invoke(asset);
                    }
                    if (attrValue != null) {
                        // Ignore null values and empty collections
                        boolean skip = (attrValue instanceof Collection && ((Collection<?>) attrValue).isEmpty())
                                || (attrValue instanceof Map && ((Map<?, ?>) attrValue).isEmpty());
                        if (!skip) {
                            if (fieldName.equals("mappedAtlanTagName")) {
                                String mappedName;
                                try {
                                    mappedName = AtlanTagCache.getIdForName(attrValue.toString());
                                } catch (NotFoundException e) {
                                    mappedName = Serde.DELETED_AUDIT_OBJECT;
                                } catch (AtlanException e) {
                                    throw new IOException("Unable to serialize mappedAtlanTagName.", e);
                                }
                                attrValue = mappedName;
                            } else if (fieldName.equals("purposeAtlanTags") && attrValue instanceof Collection) {
                                List<String> mappedNames = new ArrayList<>();
                                for (Object one : (Collection<?>) attrValue) {
                                    try {
                                        mappedNames.add(AtlanTagCache.getIdForName(one.toString()));
                                    } catch (NotFoundException e) {
                                        mappedNames.add(Serde.DELETED_AUDIT_OBJECT);
                                    } catch (AtlanException e) {
                                        throw new IOException("Unable to serialize purposeAtlanTags.", e);
                                    }
                                }
                                attrValue = mappedNames;
                            }
                            // Add the value we've derived above to the attribute map for nesting
                            String serializeName = ReflectionCache.getSerializedName(clazz, fieldName);
                            attributes.put(serializeName, attrValue);
                        }
                    }
                } else if (fieldName.equals("customMetadataSets")) {
                    // Translate custom metadata to businessAttributes map
                    Map<String, CustomMetadataAttributes> cm = asset.getCustomMetadataSets();
                    if (cm != null) {
                        CustomMetadataCache.getBusinessAttributesFromCustomMetadata(cm, businessAttributes);
                    }
                } else {
                    // For any other (top-level) field, we'll just write it out as-is (skipping any null
                    // values or empty lists)
                    Object attrValue =
                            ReflectionCache.getGetter(clazz, fieldName).invoke(asset);
                    if (attrValue != null
                            && !(attrValue instanceof Collection && ((Collection<?>) attrValue).isEmpty())) {
                        String serializeName = ReflectionCache.getSerializedName(clazz, fieldName);
                        sp.defaultSerializeField(serializeName, attrValue, gen);
                    }
                }
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException("Unable to retrieve value through reflection.", e);
        } catch (AtlanException e) {
            throw new IOException("Unable to retrieve the available custom metadata in Atlan.", e);
        }

        if (!attributes.isEmpty()) {
            // Special cases to wrap-up:
            // Encode the Readme's description after serialization
            if (asset.getTypeName() != null && asset.getTypeName().equals("Readme")) {
                String unencoded = (String) attributes.get("description");
                attributes.put("description", StringUtils.encodeContent(unencoded));
            }
            sp.defaultSerializeField("attributes", attributes, gen);
        }
        if (!businessAttributes.isEmpty()) {
            sp.defaultSerializeField("businessAttributes", businessAttributes, gen);
        }
        gen.writeEndObject();
    }
}
