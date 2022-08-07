/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.serde;

import com.atlan.model.*;
import com.atlan.model.core.Entity;
import com.atlan.model.relations.Reference;
import com.atlan.util.StringUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
public class EntityTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Entity.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final String discriminator = "typeName";

        final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
        final TypeAdapter<Reference> referenceAdapter = gson.getDelegateAdapter(this, TypeToken.get(Reference.class));

        final TypeAdapter<IndistinctAsset> assetAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(IndistinctAsset.class));

        final TypeAdapter<Glossary> glossaryAdapter = gson.getDelegateAdapter(this, TypeToken.get(Glossary.class));
        final TypeAdapter<GlossaryCategory> glossaryCategoryAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(GlossaryCategory.class));
        final TypeAdapter<GlossaryTerm> glossaryTermAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(GlossaryTerm.class));

        final TypeAdapter<LineageProcess> processAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(LineageProcess.class));
        final TypeAdapter<ColumnProcess> columnProcessAdapter =
                gson.getDelegateAdapter(this, TypeToken.get(ColumnProcess.class));

        final TypeAdapter<Table> tableAdapter = gson.getDelegateAdapter(this, TypeToken.get(Table.class));

        TypeAdapter<Entity> resultCustomTypeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, Entity value) throws IOException {

                // Approach: change the object itself, then serialize it at the end
                String typeName = value.getTypeName();
                Class<?> c;
                Asset toModify;
                if (typeName == null) {
                    c = IndistinctAsset.class;
                    toModify = ((IndistinctAsset) value).toBuilder().build();
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            c = Glossary.class;
                            toModify = ((Glossary) value).toBuilder().build();
                            break;
                        case "AtlasGlossaryCategory":
                            c = GlossaryCategory.class;
                            toModify = ((GlossaryCategory) value).toBuilder().build();
                            break;
                        case "AtlasGlossaryTerm":
                            c = GlossaryTerm.class;
                            toModify = ((GlossaryTerm) value).toBuilder().build();
                            break;
                        case "Process":
                            c = LineageProcess.class;
                            toModify = ((LineageProcess) value).toBuilder().build();
                            break;
                        case "ColumnProcess":
                            c = ColumnProcess.class;
                            toModify = ((ColumnProcess) value).toBuilder().build();
                            break;
                        case "Table":
                            c = Table.class;
                            toModify = ((Table) value).toBuilder().build();
                            break;
                        default:
                            c = IndistinctAsset.class;
                            toModify = ((IndistinctAsset) value).toBuilder().build();
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

                //  2. iterate through getter methods to retrieve attribute values
                try {
                    for (Map.Entry<String, Method> entry : getterMap.entrySet()) {
                        String fieldName = entry.getKey();
                        Field field = fieldMap.get(fieldName);
                        if (field != null && field.isAnnotationPresent(Attribute.class)) {
                            Object attrValue;
                            if (nullFields.contains(fieldName)) {
                                // If the value should be serialized as null, then
                                // set the value to the serializable null
                                attrValue = Removable.NULL;
                            } else {
                                // Otherwise, pickup the value from the top-level
                                // attribute so that we can move that value across
                                Method get = entry.getValue();
                                attrValue = get.invoke(toModify);
                            }
                            if (attrValue != null) {
                                //  3. set attributes map from the top-level attribute value
                                attributes.put(fieldName, attrValue);
                                Method set = setterMap.get(fieldName);
                                if (set != null) {
                                    //  4. null the individual top-level attribute
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
                    assetAdapter.write(out, (IndistinctAsset) toModify);
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            glossaryAdapter.write(out, (Glossary) toModify);
                            break;
                        case "AtlasGlossaryCategory":
                            glossaryCategoryAdapter.write(out, (GlossaryCategory) toModify);
                            break;
                        case "AtlasGlossaryTerm":
                            glossaryTermAdapter.write(out, (GlossaryTerm) toModify);
                            break;
                        case "Process":
                            processAdapter.write(out, (LineageProcess) toModify);
                            break;
                        case "ColumnProcess":
                            columnProcessAdapter.write(out, (ColumnProcess) toModify);
                            break;
                        case "Table":
                            tableAdapter.write(out, (Table) toModify);
                            break;
                        default:
                            assetAdapter.write(out, (IndistinctAsset) toModify);
                            break;
                    }
                }
            }

            @Override
            public Entity read(JsonReader in) throws IOException {
                JsonObject object = jsonElementAdapter.read(in).getAsJsonObject();
                JsonObject attributes = object.getAsJsonObject("attributes");
                JsonObject relationshipAttributes = object.getAsJsonObject("relationshipAttributes");

                Entity value;
                Class<?> c;

                String typeName = object.getAsJsonPrimitive(discriminator).getAsString();
                if (typeName == null) {
                    value = assetAdapter.fromJsonTree(object);
                    c = Asset.class;
                } else {
                    switch (typeName) {
                        case "AtlasGlossary":
                            value = glossaryAdapter.fromJsonTree(object);
                            c = Glossary.class;
                            break;
                        case "AtlasGlossaryCategory":
                            value = glossaryCategoryAdapter.fromJsonTree(object);
                            c = GlossaryCategory.class;
                            break;
                        case "AtlasGlossaryTerm":
                            value = glossaryTermAdapter.fromJsonTree(object);
                            c = GlossaryTerm.class;
                            break;
                        case "Process":
                            value = processAdapter.fromJsonTree(object);
                            c = LineageProcess.class;
                            break;
                        case "ColumnProcess":
                            value = columnProcessAdapter.fromJsonTree(object);
                            c = ColumnProcess.class;
                            break;
                        case "Table":
                            value = tableAdapter.fromJsonTree(object);
                            c = Table.class;
                            break;
                        default:
                            value = assetAdapter.fromJsonTree(object);
                            c = Asset.class;
                            break;
                    }
                }

                Map<String, Method> setterMap = new LinkedHashMap<>();
                getSetterMethods(setterMap, c);

                if (attributes != null) {
                    for (String attrKey : attributes.keySet()) {
                        Method method = setterMap.get(attrKey);
                        if (method != null) {
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
                        Method method = setterMap.get(relnKey);
                        if (method != null) {
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

    private static void deserialize(
            Entity value, JsonElement jsonElement, Method method, TypeAdapter<Reference> referenceAdapter)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonElement.isJsonPrimitive()) {
            deserializePrimitive(value, jsonElement.getAsJsonPrimitive(), method);
        } else if (jsonElement.isJsonArray()) {
            deserializeList(value, jsonElement.getAsJsonArray(), method, referenceAdapter);
        } else if (jsonElement.isJsonObject()) {
            // TODO: Could be a map or a reference...
            deserializeObject(value, jsonElement.getAsJsonObject(), method, referenceAdapter);
        }
    }

    private static void deserializePrimitive(Entity value, JsonPrimitive primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
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

    private static void deserializeList(
            Entity value, JsonArray array, Method method, TypeAdapter<Reference> referenceAdapter)
            throws IllegalAccessException, InvocationTargetException, IOException {
        List<Object> list = new ArrayList<>();
        for (JsonElement element : array) {
            Object deserialized = deserializeElement(element, method, referenceAdapter);
            list.add(deserialized);
        }
        Parameter[] params = method.getParameters();
        Class<?> paramClass = params[0].getType();
        if (paramClass == Set.class) {
            method.invoke(value, new LinkedHashSet<>(list));
        } else if (paramClass == List.class) {
            method.invoke(value, list);
        } else {
            throw new IOException("Unable to deserialize JSON list to Java class: " + paramClass.getCanonicalName());
        }
    }

    private static void deserializeObject(
            Entity value, JsonObject jsonObject, Method method, TypeAdapter<Reference> referenceAdapter)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Parameter[] params = method.getParameters();
        Class<?> paramClass = params[0].getType();
        if (paramClass == Reference.class) {
            method.invoke(value, referenceAdapter.fromJsonTree(jsonObject));
        } else if (paramClass == Map.class) {
            // TODO: need to translate Java object to a Map
            method.invoke(value, jsonObject);
        } else {
            throw new IOException("Unable to deserialize JSON object to Java class: " + paramClass.getCanonicalName());
        }
    }

    private static void deserializeNumber(Entity value, JsonPrimitive primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Object number = deserializeNumber(primitive.getAsNumber(), method);
        method.invoke(value, number);
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @param referenceAdapter for translating relationship objects
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    private static Object deserializeElement(
            JsonElement element, Method method, TypeAdapter<Reference> referenceAdapter) throws IOException {
        if (element.isJsonPrimitive()) {
            return deserializePrimitive(element.getAsJsonPrimitive(), method);
        } else if (element.isJsonArray()) {
            throw new IOException("Directly-nested arrays are not supported.");
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
    private static Object deserializePrimitive(JsonPrimitive primitive, Method method) throws IOException {
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
     * @throws IOException if the setter method to deserialize through does not take exactly one argument
     */
    private static Object deserializeNumber(Number primitive, Method method) throws IOException {
        Parameter[] parameters = method.getParameters();
        Class<?> parameterType = Boolean.class;
        if (parameters.length == 1) {
            parameterType = parameters[0].getType();
        } else {
            throw new IOException(
                    "Unexpected number of parameters (" + parameters.length + ") found for method: " + method);
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
