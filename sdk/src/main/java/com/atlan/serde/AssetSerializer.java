/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.CustomEntity;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.relations.Reference;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
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
    private final AtlanClient client;

    public AssetSerializer(AtlanClient client) {
        this(Asset.class, client);
    }

    public AssetSerializer(Class<Asset> t, AtlanClient client) {
        super(t);
        this.client = client;
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
        Map<String, Object> appendRelationships = new LinkedHashMap<>();
        Map<String, Object> removeRelationships = new LinkedHashMap<>();
        Set<AtlanTag> appendAtlanTags = new LinkedHashSet<>();
        Set<AtlanTag> removeAtlanTags = new LinkedHashSet<>();
        Set<AtlanTag> replaceAtlanTags = new LinkedHashSet<>();
        Map<String, Map<String, Object>> businessAttributes = new LinkedHashMap<>();

        gen.writeStartObject();

        try {
            for (String fieldName : ReflectionCache.getFieldNames(clazz)) {
                if (ReflectionCache.isAttribute(clazz, fieldName)) {
                    // If the field should be attribute-nested...
                    Object attrValue;
                    Class<?> type = ReflectionCache.getFieldType(clazz, fieldName);
                    if (nullFields.contains(fieldName)) {
                        // If the value should be serialized as null, then
                        // set the value to the serializable null
                        if (type == List.class || type == Set.class || type == SortedSet.class) {
                            attrValue = Removable.EMPTY_LIST;
                        } else {
                            attrValue = Removable.NULL;
                        }
                    } else {
                        // Otherwise, pickup the value from the top-level
                        // attribute so that we can move that value across
                        attrValue = ReflectionCache.getValue(asset, fieldName);
                    }
                    if (attrValue != null) {
                        // Ignore null values and empty collections
                        boolean skip = (attrValue instanceof Collection && ((Collection<?>) attrValue).isEmpty())
                                || (attrValue instanceof Map && ((Map<?, ?>) attrValue).isEmpty());
                        if (!skip) {
                            if (fieldName.equals("mappedAtlanTagName")) {
                                String mappedName;
                                try {
                                    mappedName = client.getAtlanTagCache().getSidForName(attrValue.toString());
                                } catch (NotFoundException e) {
                                    mappedName = Serde.DELETED_AUDIT_OBJECT;
                                } catch (AtlanException e) {
                                    throw new IOException("Unable to serialize mappedAtlanTagName.", e);
                                }
                                if (mappedName == null) {
                                    mappedName = Serde.DELETED_AUDIT_OBJECT;
                                }
                                attrValue = mappedName;
                            } else if (fieldName.equals("purposeAtlanTags") && attrValue instanceof Collection) {
                                List<String> mappedNames = new ArrayList<>();
                                for (Object one : (Collection<?>) attrValue) {
                                    try {
                                        String name = client.getAtlanTagCache().getSidForName(one.toString());
                                        if (name == null) {
                                            name = Serde.DELETED_AUDIT_OBJECT;
                                        }
                                        mappedNames.add(name);
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
                            // Note: the value could be a singular reference, or a collection of references...
                            if (attrValue instanceof Collection) {
                                // If it is in a collection, check whether the first value is a reference
                                // (there should always be one, as earlier condition would exclude empty
                                // collections)
                                Collection<?> values = (Collection<?>) attrValue;
                                Optional<?> first = values.stream().findFirst();
                                if (first.isPresent() && first.get() instanceof Reference) {
                                    List<Object> appends = new ArrayList<>();
                                    List<Object> removes = new ArrayList<>();
                                    List<Object> replace = new ArrayList<>();
                                    for (Object value : values) {
                                        Reference relationship = (Reference) value;
                                        switch (relationship.getSemantic()) {
                                            case APPEND:
                                                appends.add(relationship);
                                                break;
                                            case REMOVE:
                                                removes.add(relationship);
                                                break;
                                            default:
                                                replace.add(relationship);
                                                break;
                                        }
                                    }
                                    if (!appends.isEmpty()) {
                                        appendRelationships.put(serializeName, appends);
                                    }
                                    if (!removes.isEmpty()) {
                                        removeRelationships.put(serializeName, removes);
                                    }
                                    if (!replace.isEmpty()) {
                                        attributes.put(serializeName, replace);
                                    }
                                } else {
                                    attributes.put(serializeName, attrValue);
                                }
                            } else if (attrValue instanceof Reference) {
                                // If the value is a relationship, put it into the appropriate portion of
                                // the request based on its semantic
                                Reference relationship = (Reference) attrValue;
                                switch (relationship.getSemantic()) {
                                    case APPEND:
                                        appendRelationships.put(serializeName, attrValue);
                                        break;
                                    case REMOVE:
                                        removeRelationships.put(serializeName, attrValue);
                                        break;
                                    default:
                                        attributes.put(serializeName, attrValue);
                                        break;
                                }
                            } else {
                                attributes.put(serializeName, attrValue);
                            }
                        }
                    }
                } else if (fieldName.equals("customMetadataSets")) {
                    // Translate custom metadata to businessAttributes map
                    Map<String, CustomMetadataAttributes> cm = asset.getCustomMetadataSets();
                    if (cm != null && !cm.isEmpty()) {
                        client.getCustomMetadataCache().getBusinessAttributesFromCustomMetadata(cm, businessAttributes);
                    }
                } else if (fieldName.equals("atlanTags")) {
                    Set<AtlanTag> tags = asset.getAtlanTags();
                    for (AtlanTag tag : tags) {
                        switch (tag.getSemantic()) {
                            case APPEND:
                                appendAtlanTags.add(tag);
                                break;
                            case REMOVE:
                                removeAtlanTags.add(tag);
                                break;
                            default:
                                replaceAtlanTags.add(tag);
                                break;
                        }
                    }
                } else {
                    // For any other (top-level) field, we'll just write it out as-is (skipping any null
                    // values or empty lists)
                    Object attrValue = ReflectionCache.getValue(asset, fieldName);
                    if (attrValue != null
                            && !(attrValue instanceof Collection && ((Collection<?>) attrValue).isEmpty())
                            && !attrValue.equals(Collections.EMPTY_MAP)
                            && !attrValue.equals(Collections.EMPTY_SET)
                            && !attrValue.equals(Collections.EMPTY_LIST)) {
                        String serializeName = ReflectionCache.getSerializedName(clazz, fieldName);
                        sp.defaultSerializeField(serializeName, attrValue, gen);
                    }
                }
            }

            // If a URL-based icon has been provided, allow that to be used as the assetIcon.
            String iconUrl = asset.getIconUrl();
            if (iconUrl != null
                    && !iconUrl.isEmpty()
                    && (asset.getTypeName().equals("Catalog") || asset instanceof CustomEntity)) {
                attributes.put("assetIcon", iconUrl);
            }

        } catch (AtlanException e) {
            throw new IOException(e);
        }

        if (!attributes.isEmpty()) {
            // Special cases to wrap-up:
            // Encode the Readme's description after serialization
            if (asset.getTypeName() != null && asset.getTypeName().equals("Readme")) {
                Object desc = attributes.get("description");
                if (desc instanceof Removable) {
                    attributes.put("description", desc);
                } else {
                    String unencoded = (String) desc;
                    attributes.put("description", StringUtils.encodeContent(unencoded));
                }
            }
            sp.defaultSerializeField("attributes", attributes, gen);
        }
        if (!appendRelationships.isEmpty()) {
            sp.defaultSerializeField("appendRelationshipAttributes", appendRelationships, gen);
        }
        if (!removeRelationships.isEmpty()) {
            sp.defaultSerializeField("removeRelationshipAttributes", removeRelationships, gen);
        }
        if (!appendAtlanTags.isEmpty()) {
            sp.defaultSerializeField("addOrUpdateClassifications", appendAtlanTags, gen);
        }
        if (!removeAtlanTags.isEmpty()) {
            sp.defaultSerializeField("removeClassifications", removeAtlanTags, gen);
        }
        if (!replaceAtlanTags.isEmpty()) {
            sp.defaultSerializeField("classifications", replaceAtlanTags, gen);
        }
        if (!businessAttributes.isEmpty()) {
            sp.defaultSerializeField("businessAttributes", businessAttributes, gen);
        }
        gen.writeEndObject();
    }
}
