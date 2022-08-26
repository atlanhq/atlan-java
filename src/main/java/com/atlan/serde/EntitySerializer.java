package com.atlan.serde;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.Attribute;
import com.atlan.model.CustomMetadataAttributes;
import com.atlan.model.core.Entity;
import com.atlan.util.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntitySerializer extends StdSerializer<Entity> {
    private static final long serialVersionUID = 2L;

    public EntitySerializer() {
        this(null);
    }

    public EntitySerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    public void serializeWithType(
            Entity value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    @Override
    public void serialize(Entity entity, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        Map<String, Method> getterMap = new HashMap<>();
        ReflectionUtils.getGetterMethods(getterMap, entity.getClass());
        Map<String, Field> fieldMap = new HashMap<>();
        ReflectionUtils.getAllFields(fieldMap, entity.getClass());

        Set<String> nullFields = entity.getNullFields();

        Map<String, Object> attributes = new LinkedHashMap<>();
        Map<String, Map<String, Object>> businessAttributes = new LinkedHashMap<>();

        gen.writeStartObject();

        try {
            for (Map.Entry<String, Method> entry : getterMap.entrySet()) {
                String fieldName = entry.getKey();
                Field field = fieldMap.get(fieldName);
                if (field != null) {
                    Method getter = entry.getValue();
                    if (field.isAnnotationPresent(Attribute.class)) {
                        Object attrValue;
                        if (nullFields.contains(fieldName)) {
                            // If the value should be serialized as null, then
                            // set the value to the serializable null
                            if (field.getType() == List.class || field.getType() == Set.class) {
                                attrValue = Removable.EMPTY_LIST;
                            } else {
                                attrValue = Removable.NULL;
                            }
                        } else {
                            // Otherwise, pickup the value from the top-level
                            // attribute so that we can move that value across
                            attrValue = getter.invoke(entity);
                        }
                        if (attrValue != null) {
                            //  3. set attributes map from the top-level attribute value
                            attributes.put(fieldName, attrValue);
                        }
                    } else if (field.getName().equals("customMetadataSets")) {
                        // 5. Translate custom metadata to businessAttributes map
                        Map<String, CustomMetadataAttributes> cm = entity.getCustomMetadataSets();
                        if (cm != null) {
                            CustomMetadataCache.getBusinessAttributesFromCustomMetadataJ(cm, businessAttributes);
                        }
                        // Then remove it, to exclude it from serialization
                        entity.setCustomMetadataSets(null);
                    } else if (!field.isAnnotationPresent(JsonIgnore.class)) {
                        Object attrValue = getter.invoke(entity);
                        if (attrValue != null
                                && !(attrValue instanceof Collection && ((Collection<?>) attrValue).isEmpty())) {
                            // Otherwise, just write out the field as-is
                            sp.defaultSerializeField(fieldName, attrValue, gen);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException("Unable to retrieve value through reflection.", e);
        } catch (AtlanException e) {
            throw new IOException("Unable to retrieve the available custom metadata in Atlan.", e);
        }

        sp.defaultSerializeField("attributes", attributes, gen);
        if (!businessAttributes.isEmpty()) {
            sp.defaultSerializeField("businessAttributes", businessAttributes, gen);
        }
        gen.writeEndObject();
    }
}
