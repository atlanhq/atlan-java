package com.atlan.util;

import com.atlan.net.ApiResourceJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
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
    public static Object deserializePrimitive(JsonNode primitive, Method method) throws IOException {
        if (primitive.isTextual()) {
            return primitive.asText();
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
     * Deserialize the provided path into a boolean, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @return the boolean value, or null
     */
    public static Boolean deserializeBoolean(JsonNode node, String path) {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : value.asBoolean();
    }

    /**
     * Deserialize the provided path into a full object, or null if there is no value at the path.
     *
     * @param node from which to pull the value
     * @param path at which to find the value
     * @param typeReference of the expected value
     * @return the object value, or null
     * @param <T> the type of the object's value
     * @throws JsonProcessingException on any problems parsing the expected value
     */
    public static <T> T deserializeObject(JsonNode node, String path, TypeReference<T> typeReference)
            throws JsonProcessingException {
        JsonNode value = node.get(path);
        return value == null || value.isNull() ? null : ApiResourceJ.mapper.readValue(value.toString(), typeReference);
    }
}
