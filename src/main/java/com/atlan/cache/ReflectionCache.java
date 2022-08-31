/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.assets.Attribute;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Lazily-loaded cache for Java reflection-based operations across the Atlan data model.
 */
@Slf4j
public class ReflectionCache {

    private static final Map<String, Map<String, Field>> fieldMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Method>> getterMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Method>> setterMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> attributesMap = new ConcurrentHashMap<>();

    private static final Map<String, Map<String, String>> fieldNameToSerialize = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> fieldNameToDeserialize = new ConcurrentHashMap<>();

    private static final Map<String, Set<String>> fieldList = new ConcurrentHashMap<>();

    /**
     * Build up a map of all fields that exist in the class (and its superclasses).
     *
     * @param map of all fields
     * @param b starting class
     * @param o original class (constant throughout recursion)
     */
    private static void getAllFields(Map<String, Field> map, Class<?> b, Class<?> o) {
        Class<?> a = b.getSuperclass();
        if (a != null) {
            getAllFields(map, a, o);
        }
        String originalClassName = o.getCanonicalName();
        for (Field field : b.getDeclaredFields()) {
            // We only need to cache fields that have something to do with serde,
            // so if Jackson is told to ignore it, so will we
            if (!field.isAnnotationPresent(JsonIgnore.class)) {
                String fieldName = field.getName();
                if (field.isAnnotationPresent(JsonProperty.class)) {
                    // If the field has a JsonProperty annotation, we need to use this name
                    // as an override for the name of the field for serde purposes
                    JsonProperty jp = field.getAnnotation(JsonProperty.class);
                    String overrideName = jp.value();
                    fieldNameToSerialize.get(originalClassName).put(fieldName, overrideName);
                    fieldNameToDeserialize.get(originalClassName).put(overrideName, fieldName);
                }
                if (field.isAnnotationPresent(Attribute.class)) {
                    // If the field has an Attribute annotation, we need to nest it into
                    // an attributes map for serialization, and un-nest that same map into flattened
                    // class members on deserialization
                    attributesMap.get(originalClassName).put(fieldName, fieldName);
                }
                map.put(fieldName, field);
            }
        }
    }

    /**
     * Build up a map of all the methods that exist in the class (and its superclasses),
     * whose name starts with a certain prefix.
     *
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
     * Build up the reflection caches for the specified class, if it is not already present.
     * (If it is already present this will do nothing.)
     *
     * @param b starting class
     */
    public static void addClass(Class<?> b) {
        String className = b.getCanonicalName();
        if (!fieldMap.containsKey(className)) {
            // Initialize all of these maps up-front, as some may not be used
            // at all by a given class, but lookups should not produce NullPointerExceptions
            // because of that
            if (!fieldNameToSerialize.containsKey(className)) {
                fieldNameToSerialize.put(className, new ConcurrentHashMap<>());
            }
            if (!fieldNameToDeserialize.containsKey(className)) {
                fieldNameToDeserialize.put(className, new ConcurrentHashMap<>());
            }
            if (!attributesMap.containsKey(className)) {
                attributesMap.put(className, new ConcurrentHashMap<>());
            }
            HashMap<String, Field> map = new HashMap<>();
            getAllFields(map, b, b);
            fieldMap.put(className, Collections.unmodifiableMap(map));
        }
        if (!getterMap.containsKey(className)) {
            HashMap<String, Method> map = new HashMap<>();
            getMethods(map, b, "get");
            getterMap.put(className, Collections.unmodifiableMap(map));
        }
        if (!setterMap.containsKey(className)) {
            HashMap<String, Method> map = new HashMap<>();
            getMethods(map, b, "set");
            setterMap.put(className, Collections.unmodifiableMap(map));
        }
    }

    /**
     * Retrieve all the field names for the provided asset type's class.
     *
     * @param b class of the asset type
     * @return collection of all field names for that asset type
     */
    public static Set<String> getFieldNames(Class<?> b) {
        addClass(b);
        if (!fieldList.containsKey(b.getCanonicalName())) {
            // Each list has a bit more than we want:
            // - fieldMap contains things like TYPE_NAME, serialVersionUID, etc
            // - getterMap contains any methods we happen to name getXYZ, whether they reference a field or not
            // - setterMap could have the same problem
            // Therefore we will set-intersect the actual field names with the getter methods
            Set<String> fields =
                    new HashSet<>(getterMap.get(b.getCanonicalName()).keySet());
            fields.retainAll(fieldMap.get(b.getCanonicalName()).keySet());
            fieldList.put(b.getCanonicalName(), Collections.unmodifiableSet(fields));
        }
        return fieldList.get(b.getCanonicalName());
    }

    /**
     * Retrieve the type of the specified field.
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return the type of that field
     */
    public static Class<?> getFieldType(Class<?> b, String fieldName) {
        addClass(b);
        return fieldMap.get(b.getCanonicalName()).get(fieldName).getType();
    }

    /**
     * Check whether the provided field name is annotated as an attribute (true) or not (false).
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return true if the field should be attributes-nested, false otherwise
     */
    public static boolean isAttribute(Class<?> b, String fieldName) {
        addClass(b);
        return attributesMap.get(b.getCanonicalName()).containsKey(fieldName);
    }

    /**
     * Retrieve the getter method for the specified field.
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return getter method that can be invoked to retrieve the value of the field
     */
    public static Method getGetter(Class<?> b, String fieldName) {
        addClass(b);
        return getterMap.get(b.getCanonicalName()).get(fieldName);
    }

    /**
     * Retrieve the setter method for the specified field.
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return setter method that can be invoked to store the value of the field
     */
    public static Method getSetter(Class<?> b, String fieldName) {
        addClass(b);
        return setterMap.get(b.getCanonicalName()).get(fieldName);
    }

    /**
     * Retrieve the name that should be used for this field for serialization purposes.
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return the name of the field to use when serializing into JSON
     */
    public static String getSerializedName(Class<?> b, String fieldName) {
        addClass(b);
        return fieldNameToSerialize.get(b.getCanonicalName()).getOrDefault(fieldName, fieldName);
    }

    /**
     * Retrieve the name that should be used for this field for deserialization purposes.
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return the name of the field to use when deserializing into an object
     */
    public static String getDeserializedName(Class<?> b, String fieldName) {
        addClass(b);
        return fieldNameToDeserialize.get(b.getCanonicalName()).getOrDefault(fieldName, fieldName);
    }

    /**
     * Retrieve the class (type) of the first parameter of the provided method.
     * (For setter methods, this should be the only parameter.)
     *
     * @param method for which to determine the first parameter's type
     * @return the class of the first parameter
     */
    public static Class<?> getParameterOfMethod(Method method) {
        Parameter[] params = method.getParameters();
        return params[0].getType();
    }

    /**
     * Retrieve the parameterized class (type) of the first parameter of the provided method.
     * (For setter methods, this should be the only parameter.)
     *
     * @param method for which to determine the first parameter's parameterized type
     * @return the parameterized type of the first parameter
     */
    public static Type getParameterizedTypeOfMethod(Method method) {
        Parameter[] params = method.getParameters();
        return params[0].getParameterizedType();
    }

    /**
     * Retrieve the class (type) within the parameterized type provided.
     * For example, if the parameterized type is {@code List<String>} this will return the String class.
     *
     * @param parameterizedType the parameterized type from which to determine the inner type
     * @return the class within the parameterized type
     */
    public static Class<?> getClassOfParameterizedType(Type parameterizedType) {
        Type parameterType = null;
        if (parameterizedType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
            parameterType = typeArguments[0];
        }
        return (Class<?>) parameterType;
    }
}
