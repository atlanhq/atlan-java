package com.atlan.util;

import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class GsonUtils {

    /**
     * Deserialize a primitive JSON value to an object.
     * @param primitive value to deserialize
     * @param method through which the deserialized value will be set on the object
     * @return the deserialized value
     * @throws IOException if the setter method to deserialize through does not take exactly one argument
     */
    public static Object deserializePrimitive(JsonPrimitive primitive, Method method) throws IOException {
        if (primitive.isString()) {
            return primitive.getAsString();
        } else if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        } else if (primitive.isNumber()) {
            return GsonUtils.deserializeNumber(primitive.getAsNumber(), method);
        }
        return null;
    }

    /**
     * Deserialize a number direct to an object, converting to the correct type
     * across Integer, Long, Double, Float, Short, Byte.
     * @param primitive number to deserialize
     * @param method through which the deserialized value will be set on the object
     * @return the deserialized number
     * @throws IOException if the setter method to deserialize through does not take exactly one argument
     */
    public static Object deserializeNumber(Number primitive, Method method) throws IOException {
        Parameter[] parameters = method.getParameters();
        Class<?> parameterType;
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
}
