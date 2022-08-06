/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import com.atlan.model.*;
import com.atlan.model.core.EntityX;
import com.atlan.model.relations.Reference;
import com.atlan.util.StringUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Creates type adapter for interface {@code EntityX} able to deserialize raw JSON to subtype
 * implementation based on discriminator field {@code typeName}.
 */
@Slf4j
public class EntityXAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!EntityX.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final String discriminator = "typeName";

        final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
        final TypeAdapter<Reference> referenceAdapter =
            gson.getDelegateAdapter(this, TypeToken.get(Reference.class));

        final TypeAdapter<AssetX> assetAdapter = gson.getDelegateAdapter(this, TypeToken.get(AssetX.class));
        final TypeAdapter<GlossaryX> glossaryAdapter = gson.getDelegateAdapter(this, TypeToken.get(GlossaryX.class));
        final TypeAdapter<GlossaryCategoryX> glossaryCategoryAdapter =
            gson.getDelegateAdapter(this, TypeToken.get(GlossaryCategoryX.class));
        final TypeAdapter<GlossaryTermX> glossaryTermAdapter =
            gson.getDelegateAdapter(this, TypeToken.get(GlossaryTermX.class));

        TypeAdapter<EntityX> resultCustomTypeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, EntityX value) throws IOException {

                // Approach: change the object itself, then serialize it at the end
                String typeName = value.getTypeName();
                Class<?> c;
                AssetX toModify;
                if (typeName == null) {
                    c = AssetX.class;
                    toModify = ((AssetX) value).toBuilder().build();
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            c = GlossaryX.class;
                            toModify = ((GlossaryX) value).toBuilder().build();
                            break;
                        case "AtlasGlossaryCategory":
                            c = GlossaryCategoryX.class;
                            toModify = ((GlossaryCategoryX) value).toBuilder().build();
                            break;
                        case "AtlasGlossaryTerm":
                            c = GlossaryTermX.class;
                            toModify = ((GlossaryTermX) value).toBuilder().build();
                            break;
                        default:
                            c = AssetX.class;
                            toModify = ((AssetX) value).toBuilder().build();
                            break;
                    }
                }

                //  1. get getter methods for attributes
                Map<String, Method> getterMap = new LinkedHashMap<>();
                getGetterMethods(getterMap, c);
                Map<String, Method> setterMap = new LinkedHashMap<>();
                getSetterMethods(setterMap, c);
                Map<String, Field> fieldMap = new LinkedHashMap<>();
                getAllFields(fieldMap, c);

                Map<String, Object> attributes = toModify.getAttributes();
                if (attributes == null) {
                    attributes = new LinkedHashMap<>();
                }

                Set<String> nullFields = toModify.getNullFields();
                if (nullFields == null) {
                    // TODO: embed this init somewhere in the model class itself
                    nullFields = Collections.EMPTY_SET;
                }

                //  2. iterate through getter methods to retrieve attribute values
                try {
                    for (Map.Entry<String, Method> entry : getterMap.entrySet()) {
                        String fieldName = entry.getKey();
                        Field field = fieldMap.get(fieldName);
                        if (field != null && field.isAnnotationPresent(Attribute.class)) {
                            log.debug("Moving attribute: {}", fieldName);
                            Method get = entry.getValue();
                            Object attrValue = get.invoke(toModify);
                            if (attrValue != null) {
                                //  3. set attributes map from the attribute values
                                if (nullFields.contains(fieldName)) {
                                    // If the value should be serialized as null, then
                                    // set the value to the serializable null
                                    attrValue = Removable.NULL;
                                }
                                attributes.put(fieldName, attrValue);
                                Method set = setterMap.get(fieldName);
                                if (set != null) {
                                    log.debug(" ... nulling the top-level field");
                                    //  4. null the individual top-level attributes
                                    set.invoke(toModify, (Object) null);
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IOException("Unable to retrieve value through reflection.", e);
                }

                toModify.setAttributes(attributes);

                //  5. serialize the new object
                if (typeName == null) {
                    assetAdapter.write(out, toModify);
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            glossaryAdapter.write(out, (GlossaryX) toModify);
                            break;
                        case "AtlasGlossaryCategory":
                            glossaryCategoryAdapter.write(out, (GlossaryCategoryX) toModify);
                            break;
                        case "AtlasGlossaryTerm":
                            glossaryTermAdapter.write(out, (GlossaryTermX) toModify);
                            break;
                        default:
                            assetAdapter.write(out, toModify);
                            break;
                    }
                }

            }

            @Override
            public EntityX read(JsonReader in) throws IOException {
                JsonObject object = jsonElementAdapter.read(in).getAsJsonObject();
                JsonObject attributes = object.getAsJsonObject("attributes");
                JsonObject relationshipAttributes = object.getAsJsonObject("relationshipAttributes");

                EntityX value;
                Class<?> c;

                String typeName = object.getAsJsonPrimitive(discriminator).getAsString();
                if (typeName == null) {
                    value = assetAdapter.fromJsonTree(object);
                    c = AssetX.class;
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            value = glossaryAdapter.fromJsonTree(object);
                            c = GlossaryX.class;
                            break;
                        case "AtlasGlossaryCategory":
                            value = glossaryCategoryAdapter.fromJsonTree(object);
                            c = GlossaryCategoryX.class;
                            break;
                        case "AtlasGlossaryTerm":
                            value = glossaryTermAdapter.fromJsonTree(object);
                            c = GlossaryTermX.class;
                            break;
                        default:
                            value = assetAdapter.fromJsonTree(object);
                            c = AssetX.class;
                            break;
                    }
                }

                Map<String, Method> setterMap = new LinkedHashMap<>();
                getSetterMethods(setterMap, c);

                if (attributes != null) {
                    for (String attrKey : attributes.keySet()) {
                        log.debug("Looking up method for: " + attrKey);
                        Method method = setterMap.get(attrKey);
                        if (method != null) {
                            log.debug(" ... deserializing {} using: {}", attrKey, method);
                            try {
                                deserialize(value, attributes.get(attrKey), method, referenceAdapter);
                            } catch (NoSuchMethodException e) {
                                throw new IOException("Missing fromValue method for enum.", e);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new IOException("Failed to deserialize through reflection.", e);
                            }
                        }
                    }
                }

                if (relationshipAttributes != null) {
                    for (String relnKey : relationshipAttributes.keySet()) {
                        log.debug("Looking up method for: " + relnKey);
                        Method method = setterMap.get(relnKey);
                        if (method != null) {
                            log.debug(" ... deserializing {} using: {}", relnKey, method);
                            try {
                                deserialize(value, relationshipAttributes.get(relnKey), method, referenceAdapter);
                            } catch (NoSuchMethodException e) {
                                throw new IOException("Missing fromValue method for enum.", e);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new IOException("Failed to deserialize through reflection.", e);
                            }
                        }
                    }
                }

                return value;

            }
        };
        return (TypeAdapter<T>) resultCustomTypeAdapter.nullSafe();
    }

    private static void deserialize(EntityX value, JsonElement jsonElement, Method method, TypeAdapter<Reference> referenceAdapter) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (jsonElement.isJsonPrimitive()) {
            deserializePrimitive(value, jsonElement.getAsJsonPrimitive(), method);
        } else if (jsonElement.isJsonArray()) {
            deserializeList(value, jsonElement.getAsJsonArray(), method, referenceAdapter);
        } else if (jsonElement.isJsonObject()) {
            method.invoke(value, referenceAdapter.fromJsonTree(jsonElement));
        }
    }

    private static void deserializePrimitive(EntityX value, JsonPrimitive primitive, Method method) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (primitive.isString()) {
            Parameter[] params = method.getParameters();
            Class<?> paramClass = params[0].getType();
            if (paramClass.isEnum()) {
                Method fromValue = paramClass.getMethod("fromValue", String.class);
                method.invoke(value, fromValue.invoke(null, primitive.getAsString()));
            } else {
                method.invoke(value, primitive.getAsString());
            }
        } else if (primitive.isBoolean()) {
            method.invoke(value, primitive.getAsBoolean());
        } else if (primitive.isNumber()) {
            deserializeNumber(value, primitive, method);
        }
    }

    private static void deserializeList(EntityX value, JsonArray array, Method method, TypeAdapter<Reference> referenceAdapter) throws IllegalAccessException, InvocationTargetException {
        List<Object> list = new ArrayList<>();
        for (JsonElement element : array) {
            Object deserialized = deserializeElement(element, method, referenceAdapter);
            list.add(deserialized);
        }
        method.invoke(value, list);
    }

    private static void deserializeNumber(EntityX value, JsonPrimitive primitive, Method method) throws IllegalAccessException, InvocationTargetException {
        Object number = deserializeNumber(primitive.getAsNumber(), method);
        method.invoke(value, number);
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @param referenceAdapter for translating relationship objects
     * @return the deserialized object
     */
    private static Object deserializeElement(JsonElement element, Method method, TypeAdapter<Reference> referenceAdapter) {
        if (element.isJsonPrimitive()) {
            return deserializePrimitive(element.getAsJsonPrimitive(), method);
        } else if (element.isJsonArray()) {
            log.error("Directly-nested arrays are not supported.");
        } else if (element.isJsonObject()) {
            return referenceAdapter.fromJsonTree(element);
        }
        return null;
    }

    /**
     * Deserialize a primitive JSON value to an object.
     * @param primitive value to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @return the deserialized value
     */
    private static Object deserializePrimitive(JsonPrimitive primitive, Method method) {
        if (primitive.isString()) {
            return primitive.getAsString();
        } else if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        } else if (primitive.isNumber()) {
            return deserializeNumber(primitive.getAsNumber(), method);
        }
        return null;
    }

    /**
     * Deserialize a number direct to an object, converting to the correct type
     * across Integer, Long, Double, Float, Short, Byte.
     * @param primitive number to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @return the deserialized number
     */
    private static Object deserializeNumber(Number primitive, Method method) {
        Parameter[] parameters = method.getParameters();
        Class<?> parameterType = Boolean.class;
        if (parameters.length == 1) {
            parameterType = parameters[0].getType();
        } else {
            log.error("Unexpected number of parameters ({}) found for method: {}", parameters.length, method);
        }
        if (parameterType == Integer.class) {
            return primitive.intValue();
        } else if (parameterType == Long.class) {
            return primitive.longValue();
        } else if (parameterType == Double.class) {
            return primitive.doubleValue();
        } else if (parameterType == Float.class) {
            return primitive.floatValue();
        } else if (parameterType == Short.class) {
            return primitive.shortValue();
        } else if (parameterType == Byte.class) {
            return primitive.byteValue();
        }
        return null;
    }

    /**
     * Build up a map of all the setter methods that exist in the class (and its superclasses),
     * from name of the field to the setter method itself (via reflection).
     * @param map of all setter methods
     * @param b starting class
     */
    private static void getSetterMethods(Map<String, Method> map, Class<?> b) {
        getMethods(map, b, "set");
    }

    /**
     * Build up a map of all the getter methods that exist in the class (and its superclasses),
     * from name of the field to the getter method itself (via reflection).
     * @param map of all getter methods
     * @param b starting class
     */
    private static void getGetterMethods(Map<String, Method> map, Class<?> b) {
        getMethods(map, b, "get");
    }

    /**
     * Build up a map of all the methods that exist in the class (and its superclasses),
     * whose name starts with a certain prefix.
     * @param map of all setter methods
     * @param b starting class
     * @param prefix each method must start with
     */
    private static void getMethods(Map<String, Method> map, Class<?> b, String prefix) {
        Class<?> a = b.getSuperclass();
        if (a != null) {
            getMethods(map, a, prefix);
        }
        for (Method method : b.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith(prefix)) {
                map.put(StringUtils.getFieldNameFromMethodName(name), method);
            }
        }
    }

    /**
     * Build up a map of all methods that exist in the class (and its superclasses),
     * from name of the method to the method itself (reflection).
     * @param map of all methods
     * @param b starting class
     */
    private static void getAllFields(Map<String, Field> map, Class<?> b) {
        Class<?> a = b.getSuperclass();
        if (a != null) {
            getAllFields(map, a);
        }
        for (Field field : b.getDeclaredFields()) {
            map.put(field.getName(), field);
        }
    }
}
