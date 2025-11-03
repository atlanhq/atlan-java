/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.model.enums.AtlanPolicyAction;
import com.atlan.serde.AtlanPolicyActionDeserializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Utilities for interacting with Jackson for serialization and deserialization.
 */
public class JacksonUtils {

    /**
     * Deserialize a primitive JSON value to an object.
     *
     * @param primitive value to deserialize
     * @param method through which the deserialized value will be set on the object
     * @return the deserialized value
     * @throws IOException if the setter method to deserialize through does not take exactly one argument, or uses an unhandled numeric type
     */
    public static Object deserializePrimitive(JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        return deserializePrimitive(primitive, method, null);
    }

    /**
     * Deserialize a primitive JSON value to an object.
     *
     * @param primitive value to deserialize
     * @param method through which the deserialized value will be set on the object
     * @param singularClass class of the first argument (parameter) to the setter method, singularized
     * @return the deserialized value
     * @throws IOException if the setter method to deserialize through does not take exactly one argument, or uses an unhandled numeric type
     */
    public static Object deserializePrimitive(JsonNode primitive, Method method, Class<?> singularClass)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (singularClass == null) {
            // If no separate singular class has been specified, fallback to retrieving it
            // from the method itself
            singularClass = ReflectionCache.getParameterOfMethod(method);
        }
        // AtlanPolicyAction is a special case, since it is an abstract enum
        // (and there's no concept of such a thing in Java)
        if (primitive.isTextual()) {
            if (singularClass == AtlanPolicyAction.class) {
                return AtlanPolicyActionDeserializer.deserialize(primitive.asText());
            } else if (singularClass.isEnum()) {
                Method fromValue = singularClass.getMethod("fromValue", String.class);
                return fromValue.invoke(null, primitive.asText());
            } else if (singularClass == Boolean.class) {
                return primitive.asBoolean();
            } else if (Number.class.isAssignableFrom(singularClass)) {
                return deserializeNumber(primitive, method);
            } else {
                return primitive.asText();
            }
        } else if (primitive.isBoolean()) {
            return primitive.asBoolean();
        } else if (primitive.isNumber()) {
            return deserializeNumber(primitive, method);
        }
        return null;
    }

    /**
     * Deserialize a number direct to an object, converting to the correct type.
     *
     * @param primitive number to deserialize
     * @param method through which to the deserialized value will be set on the object
     * @return the deserialized number
     * @throws IOException if the setter method to deserialize through does not take exactly one argument, or uses an unhandled numeric type
     */
    public static Object deserializeNumber(JsonNode primitive, Method method) throws IOException {
        Parameter[] parameters = method.getParameters();
        Class<?> parameterType;
        if (parameters.length == 1) {
            parameterType = parameters[0].getType();
        } else {
            throw new IOException(
                    "Unexpected number of parameters (" + parameters.length + ") found for method: " + method);
        }
        if (parameterType == Integer.class) {
            return primitive.asInt();
        } else if (parameterType == Long.class) {
            return primitive.asLong();
        } else if (parameterType == Double.class) {
            return primitive.asDouble();
        } else if (parameterType == Float.class) {
            return primitive.floatValue();
        } else if (parameterType == Short.class) {
            return primitive.shortValue();
        } else {
            throw new IOException("Unhandled parameter type (" + parameterType + ") found for method: " + method);
        }
    }

    /**
     * Deserialize the provided path into a string, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @return the string value, or null
     */
    public static String deserializeString(JsonNode node, String path) {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : value.asText();
    }

    /**
     * Serialize the provided field into a string, or leave it out if there is no value.
     *
     * @param gen generator through which to serialize
     * @param name of the field
     * @param value for the field
     * @throws IOException on any issues writing to the generator
     */
    public static void serializeString(JsonGenerator gen, String name, String value) throws IOException {
        if (value != null) {
            gen.writeFieldName(name);
            gen.writeString(value);
        }
    }

    /**
     * Deserialize the provided path into a long, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @return the long value, or null
     */
    public static Long deserializeLong(JsonNode node, String path) {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : value.asLong();
    }

    /**
     * Serialize the provided field into a long, or leave it out if there is no value.
     *
     * @param gen generator through which to serialize
     * @param name of the field
     * @param value for the field
     * @throws IOException on any issues writing to the generator
     */
    public static void serializeLong(JsonGenerator gen, String name, Long value) throws IOException {
        if (value != null) {
            gen.writeFieldName(name);
            gen.writeNumber(value);
        }
    }

    /**
     * Deserialize the provided path into a double, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @return the double value, or null
     */
    public static Double deserializeDouble(JsonNode node, String path) {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : value.asDouble();
    }

    /**
     * Serialize the provided field into a double, or leave it out if there is no value.
     *
     * @param gen generator through which to serialize
     * @param name of the field
     * @param value for the field
     * @throws IOException on any issues writing to the generator
     */
    public static void serializeDouble(JsonGenerator gen, String name, Double value) throws IOException {
        if (value != null) {
            gen.writeFieldName(name);
            gen.writeNumber(value);
        }
    }

    /**
     * Deserialize the provided path into a boolean, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @return the boolean value, or null
     */
    public static Boolean deserializeBoolean(JsonNode node, String path) {
        JsonNode value = node.get(path);
        return (value == null || value.isNull()) ? null : value.asBoolean();
    }

    /**
     * Serialize the provided field into a boolean, or leave it out if there is no value.
     *
     * @param gen generator through which to serialize
     * @param name of the field
     * @param value for the field
     * @throws IOException on any issues writing to the generator
     */
    public static void serializeBoolean(JsonGenerator gen, String name, Boolean value) throws IOException {
        if (value != null) {
            gen.writeFieldName(name);
            gen.writeBoolean(value);
        }
    }

    /**
     * Deserialize the provided path into a full object, or null if there is no value at the path.
     *
     * @param client connectivity to Atlan
     * @param node from which to pull the value
     * @param path at which to find the value
     * @param typeReference of the expected value
     * @return the object value, or null
     * @param <T> the type of the object's value
     * @throws JsonProcessingException on any problems parsing the expected value
     */
    public static <T> T deserializeObject(
            AtlanClient client, JsonNode node, String path, TypeReference<T> typeReference)
            throws JsonProcessingException {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : client.convertValue(value, typeReference);
    }

    /**
     * Deserialize the provided path into a full object, or null if there is no value at the path.
     *
     * @param client connectivity to Atlan
     * @param node from which to pull the value
     * @param path at which to find the value
     * @param type of the expected value
     * @return the object value, or null
     * @param <T> the type of the object's value
     * @throws JsonProcessingException on any problems parsing the expected value
     */
    public static<T> T deserializeObject(
        AtlanClient client, JsonNode node, String path, Class<T> type)
        throws JsonProcessingException {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : client.convertValue(value, type);
    }

    /**
     * Serialize the provided field into an object, or leave it out if there is no value.
     *
     * @param gen generator through which to serialize
     * @param name of the field
     * @param value for the field
     * @throws IOException on any issues writing to the generator
     */
    public static void serializeObject(JsonGenerator gen, String name, Object value) throws IOException {
        if (value != null) {
            gen.writeFieldName(name);
            gen.writeObject(value);
        }
    }
}
