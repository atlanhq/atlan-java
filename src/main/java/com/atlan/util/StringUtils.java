/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.util;

import static java.util.Objects.requireNonNull;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * Utilities for working with strings.
 */
public final class StringUtils {
    private static Pattern whitespacePattern = Pattern.compile("\\s");

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
        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            StringBuilder sb = new StringBuilder(methodName);
            sb.delete(0, 3);
            sb.replace(0, 1, sb.substring(0, 1).toLowerCase());
            return sb.toString();
        }
        return null;
    }

    /**
     * Encode the provided content for storage as a README's content.
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
     * @param encoded to be decoded
     * @return decoded README content (HTML)
     */
    public static String decodeContent(String encoded) {
        return encoded == null ? null : URLDecoder.decode(encoded.replace("%20", "+"), StandardCharsets.UTF_8);
    }
}
