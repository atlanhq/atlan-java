/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Date;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.serde.Removable;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lazily-loaded cache for Java reflection-based operations across the Atlan data model.
 */
public class ReflectionCache {

    private static final Map<String, Map<String, Field>> fieldMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Method>> getterMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Method>> setterMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> attributesMap = new ConcurrentHashMap<>();

    private static final Map<String, Map<String, String>> fieldNameToSerialize = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> fieldNameToDeserialize = new ConcurrentHashMap<>();

    private static final Map<String, Set<String>> fieldList = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> dateFields = new ConcurrentHashMap<>();

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
                map.put(fieldName, field);
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
                if (field.isAnnotationPresent(Date.class)) {
                    // If the field has a Date annotation, track it as a date field
                    dateFields.get(originalClassName).add(fieldName);
                }
            }
        }
    }

    /**
     * Build up a map of all the methods that exist in the class (and its superclasses),
     * whose name starts with a certain prefix.
     *
     * @param map of all getter/setter methods
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
            if (prefix == null) {
                map.put(name, method);
            } else if (name.startsWith(prefix)) {
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
    private static void addClass(Class<?> b) {
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
            if (!dateFields.containsKey(className)) {
                dateFields.put(className, ConcurrentHashMap.newKeySet());
            }
            HashMap<String, Field> map = new HashMap<>();
            getAllFields(map, b, b);
            fieldMap.put(className, Collections.unmodifiableMap(map));
        }
        if (className.endsWith("BuilderImpl")) {
            // Need to put builder methods into the setter map, not the root class
            // itself (as the root class itself will be immutable)
            if (!setterMap.containsKey(className)) {
                HashMap<String, Method> map = new HashMap<>();
                getMethods(map, b, null);
                setterMap.put(className, Collections.unmodifiableMap(map));
            }
        } else {
            if (!getterMap.containsKey(className)) {
                HashMap<String, Method> map = new HashMap<>();
                getMethods(map, b, "get");
                getterMap.put(className, Collections.unmodifiableMap(map));
            }
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
     * Check whether the provided field name is annotated as a date (true) or not (false).
     *
     * @param b class of the asset type
     * @param fieldName name of the field
     * @return true if the field should be treated as a date, false otherwise
     */
    public static boolean isDate(Class<?> b, String fieldName) {
        addClass(b);
        return dateFields.get(b.getCanonicalName()).contains(fieldName);
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
     * Retrieve the value for a specified field from the provided asset.
     *
     * @param a asset from which to retrieve the value
     * @param fieldName field on that asset from which to retrieve the value
     * @return value of the field on that asset
     * @throws IOException if there is any error retrieving the value dynamically
     */
    public static Object getValue(Asset a, String fieldName) throws IOException {
        Method getter = getGetter(a.getClass(), fieldName);
        if (getter == null) {
            return null;
        }
        try {
            return getter.invoke(a);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException(
                    "Failed to retrieve value for " + a.getClass().getName() + "." + fieldName + " through reflection.",
                    e);
        }
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
     * Set the value of a field on a specific asset (via its mutable builder).
     *
     * @param builder for the asset through which to set the property
     * @param fieldName name of the property to set
     * @param value value to set on the property
     * @return true if the property was set, otherwise false (for example if no such property appears to exist)
     * @throws NoSuchMethodException if there is no setter on the builder to set this field
     * @throws IllegalAccessException if the setter cannot be accessed to set this field
     * @throws InvocationTargetException if the provided builder cannot be used
     */
    public static boolean setValue(Asset.AssetBuilder<?, ?> builder, String fieldName, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setter = getSetter(builder.getClass(), fieldName);
        if (setter != null) {
            if (value instanceof Removable) {
                builder.nullField(fieldName);
            } else if (value instanceof String && ((String) value).isEmpty()) {
                builder.nullField(fieldName);
            } else if (value == null) {
                builder.nullField(fieldName);
            } else {
                setter.invoke(builder, value);
            }
            return true;
        }
        return false;
    }

    /**
     * Set the value of a field on a specific asset (via its mutable builder).
     *
     * @param builder for the asset through which to set the property
     * @param fieldName name of the property to set
     * @param value value to set on the property
     * @throws NoSuchMethodException if there is no setter on the builder to set this field
     * @throws IllegalAccessException if the setter cannot be accessed to set this field
     * @throws InvocationTargetException if the provided builder cannot be used
     */
    public static void setValue(
            RelationshipAttributes.RelationshipAttributesBuilder<?, ?> builder, String fieldName, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setter = getSetter(builder.getClass(), fieldName);
        if (setter != null) {
            if (value instanceof Removable) {
                builder.nullField(fieldName);
            } else if (value instanceof String && ((String) value).isEmpty()) {
                builder.nullField(fieldName);
            } else if (value == null) {
                builder.nullField(fieldName);
            } else {
                setter.invoke(builder, value);
            }
        }
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
        if (parameterType instanceof ParameterizedType) {
            // We need to see if the embedded type is another wrapper, as there are
            // cases where we have List<Map<String, String>>
            parameterType = ((ParameterizedType) parameterType).getRawType();
        } else if (parameterType instanceof WildcardType) {
            // We also need to see if the embedded type is a bounded generic, such
            // as <? extends AbstractProcess>, and if so to retrieve the upper bound
            WildcardType genericType = (WildcardType) parameterType;
            parameterType = genericType.getUpperBounds()[0];
            if (parameterType instanceof ParameterizedType) {
                // And the upper bound could actually itself be a wrapper, so check if
                // we need to unpack that
                parameterType = ((ParameterizedType) parameterType).getRawType();
            }
        }
        return (Class<?>) parameterType;
    }
}
