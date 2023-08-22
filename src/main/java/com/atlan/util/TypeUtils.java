/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

public class TypeUtils {

    public enum ComparisonCategory {
        STRING,
        NUMBER,
        BOOLEAN,
    }

    /**
     * Checks whether the provided attribute definition's type is comparable to the specified type.
     *
     * @param attrType of the attribute definition
     * @param to general class of comparisons to check are valid for the type
     * @return true only if the attribute definition could be logically compared using the general class of comparisons
     */
    public static boolean isComparable(String attrType, ComparisonCategory to) {
        String baseType = getBaseType(attrType);
        switch (baseType) {
            case "boolean":
                return to == ComparisonCategory.BOOLEAN;
            case "int":
            case "long":
            case "date":
            case "float":
                return to == ComparisonCategory.NUMBER;
            default:
                // Covers strings, maps, structs, related assets, etc
                return to == ComparisonCategory.STRING;
        }
    }

    /**
     * Determine the base type of the attribute, including when it's values are contained in an
     * array or map.
     *
     * @param attrType data type of the attribute
     * @return the most granular contained type of the attribute's values
     */
    public static String getBaseType(String attrType) {
        String baseType = attrType;
        if (attrType.contains("<")) {
            if (attrType.startsWith("array<")) {
                if (attrType.startsWith("array<map<")) {
                    baseType = getEmbeddedType(attrType.substring("array<".length(), attrType.length() - 1));
                } else {
                    baseType = getEmbeddedType(attrType);
                }
            } else if (attrType.startsWith("map<")) {
                baseType = getEmbeddedType(attrType);
            }
        }
        return baseType;
    }

    private static String getEmbeddedType(String attrType) {
        return attrType.substring(attrType.indexOf("<") + 1, attrType.indexOf(">"));
    }
}
