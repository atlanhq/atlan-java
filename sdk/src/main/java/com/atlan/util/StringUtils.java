/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import static java.util.Objects.requireNonNull;

import com.atlan.model.assets.Connection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with strings.
 */
public final class StringUtils {
    private static final String connectionQualifiedName = "default/[a-z0-9-]+/[0-9]{10}";
    private static final String connectionQualifiedNameRelaxed = "default/[a-z0-9-]+/[a-zA-Z0-9-._:|]+";
    private static final Pattern whitespacePattern = Pattern.compile("\\s");
    private static final Pattern connectionQN = Pattern.compile(connectionQualifiedName);
    private static final Pattern connectionQNRelaxed = Pattern.compile(connectionQualifiedNameRelaxed);
    private static final Pattern connectionQNPrefix = Pattern.compile("(" + connectionQualifiedName + ")/.*");
    private static final Pattern connectionQNPrefixRelaxed =
            Pattern.compile("(" + connectionQualifiedNameRelaxed + ")/.*");
    private static final Pattern domainQNPrefix = Pattern.compile("(default/domain/[a-zA-Z0-9-]+/super)/.*");
    private static final Pattern uuidPattern =
            Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

    /**
     * Checks whether a string contains any whitespace characters or not.
     *
     * @param str the string to check.
     * @return {@code true} if the string contains any whitespace characters; otherwise, {@code
     *     false}.
     */
    public static boolean containsWhitespace(String str) {
        requireNonNull(str);
        return whitespacePattern.matcher(str).find();
    }

    /**
     * Compares two strings for equality. The time taken is independent of the number of characters
     * that match.
     *
     * @param a one of the strings to compare.
     * @param b the other string to compare.
     * @return true if the strings are equal, false otherwise.
     */
    public static boolean secureCompare(String a, String b) {
        byte[] digesta = a.getBytes(StandardCharsets.UTF_8);
        byte[] digestb = b.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(digesta, digestb);
    }

    /**
     * Determine the name of a field that's read or written by a getter/setter method,
     * from the name of the method.
     *
     * @param methodName from which to reverse-engineer the field name
     * @return the field name
     */
    public static String getFieldNameFromMethodName(String methodName) {
        if (methodName.length() > 3 && (methodName.startsWith("set") || methodName.startsWith("get"))) {
            StringBuilder sb = new StringBuilder(methodName);
            sb.delete(0, 3);
            sb.replace(0, 1, sb.substring(0, 1).toLowerCase(Locale.ROOT));
            return sb.toString();
        }
        return null;
    }

    /**
     * Encode the provided content for storage as a README's content.
     *
     * @param decoded to be encoded
     * @return encoded README content
     */
    public static String encodeContent(String decoded) {
        return decoded == null
                ? null
                : URLEncoder.encode(decoded, StandardCharsets.UTF_8).replace("+", "%20");
    }

    /**
     * Decode the provided content from the README-encoded form to plain HTML.
     *
     * @param encoded to be decoded
     * @return decoded README content (HTML)
     */
    public static String decodeContent(String encoded) {
        return encoded == null ? null : URLDecoder.decode(encoded.replace("%20", "+"), StandardCharsets.UTF_8);
    }

    /**
     * Retrieve the connection's qualifiedName from the provided asset qualifiedName.
     * Note that this will also return null if the qualifiedName provided is for a connection (only) already!
     *
     * @param qualifiedName of the asset, from which to retrieve the connection's qualifiedName
     * @return the qualifiedName of the connection, or null if none can be determined
     */
    public static String getConnectionQualifiedName(String qualifiedName) {
        return getConnectionQualifiedName(qualifiedName, false);
    }

    /**
     * Retrieve the connection's qualifiedName from the provided asset qualifiedName.
     * Note that this will also return null if the qualifiedName provided is for a connection (only) already!
     *
     * @param qualifiedName of the asset, from which to retrieve the connection's qualifiedName
     * @param relaxed whether to allow non-standard qualifiedNames (those not using epochs)
     * @return the qualifiedName of the connection, or null if none can be determined
     */
    public static String getConnectionQualifiedName(String qualifiedName, boolean relaxed) {
        Pattern toUse;
        if (relaxed) {
            toUse = connectionQNPrefixRelaxed;
        } else {
            toUse = connectionQNPrefix;
        }
        if (qualifiedName != null) {
            Matcher m = toUse.matcher(qualifiedName);
            if (m.find() && m.groupCount() > 0) {
                return m.group(1);
            }
        }
        return null;
    }

    /**
     * Retrieve the domain's top-most ancestral domain qualifiedName.
     *
     * @param domainQualifiedName of the domain, from which to retrieve the top-most ancestral domain qualifiedName
     * @return the qualifiedName of the top-most ancestral domain, or null if none can be determined
     */
    public static String getSuperDomainQualifiedName(String domainQualifiedName) {
        if (domainQualifiedName != null) {
            Matcher m = domainQNPrefix.matcher(domainQualifiedName);
            if (m.find() && m.groupCount() > 0) {
                return m.group(1);
            } else if (domainQualifiedName.startsWith("default/domain/")) {
                return domainQualifiedName;
            }
        }
        return null;
    }

    /**
     * Retrieve the name of a data asset from the provided qualifiedName.
     *
     * @param qualifiedName from which to retrieve the name component
     * @return the name of the data asset
     */
    public static String getNameFromQualifiedName(String qualifiedName) {
        return getNameFromQualifiedName(qualifiedName, "/");
    }

    /**
     * Retrieve the name of a data asset from the provided qualifiedName.
     *
     * @param qualifiedName from which to retrieve the name component
     * @param delimiter by which the (non-connection) components of the qualifiedName are separated
     * @return the name of the data asset
     */
    public static String getNameFromQualifiedName(String qualifiedName, String delimiter) {
        if (qualifiedName == null) {
            return null;
        } else if (qualifiedName.indexOf(delimiter) > 0) {
            return qualifiedName.substring(qualifiedName.lastIndexOf(delimiter) + 1);
        } else {
            String connectionQN = getConnectionQualifiedName(qualifiedName);
            return qualifiedName.substring(connectionQN.length() + 1);
        }
    }

    /**
     * Retrieve the qualifiedName of the parent data asset of the provided asset's qualifiedName.
     *
     * @param qualifiedName from which to retrieve the parent asset's qualifiedName
     * @return the qualifiedName of the parent data asset
     */
    public static String getParentQualifiedNameFromQualifiedName(String qualifiedName) {
        return getParentQualifiedNameFromQualifiedName(qualifiedName, "/");
    }

    /**
     * Retrieve the qualifiedName of the parent data asset of the provided asset's qualifiedName.
     *
     * @param qualifiedName from which to retrieve the parent asset's qualifiedName
     * @param delimiter by which the (non-connection) components of the qualifiedName are separated
     * @return the qualifiedName of the parent data asset
     */
    public static String getParentQualifiedNameFromQualifiedName(String qualifiedName, String delimiter) {
        if (qualifiedName != null && qualifiedName.indexOf(delimiter) > 0) {
            return qualifiedName.substring(0, qualifiedName.lastIndexOf(delimiter));
        }
        return null;
    }

    /**
     * Remove any leading and trailing /-slashes from the provided string.
     *
     * @param toTrim the string to trim
     * @return the string, without any leading or trailing / (if any)
     */
    public static String trimPathDelimiters(String toTrim) {
        if (toTrim == null) {
            return "";
        } else {
            if (toTrim.startsWith("/")) {
                toTrim = toTrim.substring(1);
            }
            if (toTrim.endsWith("/")) {
                toTrim = toTrim.substring(0, toTrim.length() - 1);
            }
            return toTrim;
        }
    }

    /**
     * Checks whether a string is a valid UUID(v4) or not.
     *
     * @param str the string to check.
     * @return {@code true} if the string is a valid UUID(v4); otherwise, {@code
     *     false}.
     */
    public static boolean isUUID(String str) {
        return str != null && uuidPattern.matcher(str).find();
    }

    /**
     * Checks whether a string is a valid connection qualifiedName or not.
     *
     * @param qn the string to check.
     * @return {@code true} if the string is a valid connection qualifiedName; otherwise, {@code false}.
     */
    public static boolean isValidConnectionQN(String qn) {
        return isValidConnectionQN(qn, false);
    }

    /**
     * Checks whether a string is a valid connection qualifiedName or not.
     *
     * @param qn the string to check.
     * @param relaxed whether to allow non-standard qualifiedNames (those not using epochs).
     * @return {@code true} if the string is a valid connection qualifiedName; otherwise, {@code false}.
     */
    public static boolean isValidConnectionQN(String qn, boolean relaxed) {
        if (qn == null || qn.isEmpty()) return false;
        Pattern toUse;
        if (relaxed) {
            toUse = connectionQNRelaxed;
        } else {
            toUse = connectionQN;
        }
        if (toUse.matcher(qn).matches()) {
            String type = Connection.getConnectorFromQualifiedName(qn);
            return (type != null && !type.isEmpty());
        }
        return false;
    }

    /**
     * Convert the provided string into UpperCamelCase.
     *
     * @param text to convert
     * @return the original text, in UpperCamelCase
     */
    public static String getUpperCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (String s : words) {
            String word = s;
            word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            builder.append(word);
        }
        return builder.toString();
    }

    /**
     * Convert the provided string to lowerCamelCase, leaving alone consecutive capital letters.
     * For example: "MySQLDatabase" turns into "mySQLDatabase"
     *
     * @param text to convert
     * @return the original text, in lowerCamelCase
     */
    public static String getLowerCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : Character.toLowerCase(word.charAt(0)) + word.substring(1);
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }
            builder.append(word);
        }
        return builder.toString();
    }

    /**
     * Convert the provided string to lowerCamelCase, aggressively lowercasing consecutive capital letters.
     * For example: "MySQLDatabase" turns into "mysqldatabase"
     *
     * @param text to convert
     * @return the original text, in lowerCamelCase
     */
    public static String getLowerCamelCaseAggressive(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty()
                        ? word
                        : Character.toLowerCase(word.charAt(0))
                                + word.substring(1).toLowerCase(Locale.ROOT);
            } else {
                word = word.isEmpty()
                        ? word
                        : Character.toUpperCase(word.charAt(0))
                                + word.substring(1).toLowerCase(Locale.ROOT);
            }
            builder.append(word);
        }
        return builder.toString();
    }

    /**
     * Convert the provided string to UPPER_SNAKE_CASE.
     *
     * @param text to convert
     * @return the original text, in UPPER_SNAKE_CASE
     */
    public static String getUpperSnakeCase(String text) {
        return getSnakeCase(text).toUpperCase(Locale.ROOT);
    }

    /**
     * Convert the provided string to lower_snake_case.
     *
     * @param text to convert
     * @return the original text, in lower_snake_case
     */
    public static String getLowerSnakeCase(String text) {
        return getSnakeCase(text).toLowerCase(Locale.ROOT);
    }

    private static String getSnakeCase(String text) {
        return text.replaceAll("_", "")
                .replaceAll("GUIDs", "Guids")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2");
    }

    /**
     * Convert the provided string to Title Case.
     * @param text to convert
     * @return the original text, in Title Case
     */
    public static String getTitleCase(String text) {
        StringBuilder titleCase = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            // Insert a space before uppercase letters except for the first character
            if (i > 0 && Character.isUpperCase(currentChar)) {
                titleCase.append(" ");
            }
            titleCase.append(i == 0 ? Character.toUpperCase(currentChar) : currentChar);
        }
        return titleCase.toString();
    }
}
