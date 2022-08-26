package com.atlan.util;

import java.lang.reflect.*;
import java.util.Map;

public class ReflectionUtils {

    /**
     * Build up a map of all the setter methods that exist in the class (and its superclasses),
     * from name of the field to the setter method itself (via reflection).
     * @param map of all setter methods
     * @param b starting class
     */
    public static void getSetterMethods(Map<String, Method> map, Class<?> b) {
        getMethods(map, b, "set");
    }

    /**
     * Build up a map of all the getter methods that exist in the class (and its superclasses),
     * from name of the field to the getter method itself (via reflection).
     * @param map of all getter methods
     * @param b starting class
     */
    public static void getGetterMethods(Map<String, Method> map, Class<?> b) {
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
    public static void getAllFields(Map<String, Field> map, Class<?> b) {
        Class<?> a = b.getSuperclass();
        if (a != null) {
            getAllFields(map, a);
        }
        for (Field field : b.getDeclaredFields()) {
            map.put(field.getName(), field);
        }
    }

    /**
     * Retrieve the class (type) of the first parameter of the provided method.
     * (For setter methods, this should be the only parameter.)
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
