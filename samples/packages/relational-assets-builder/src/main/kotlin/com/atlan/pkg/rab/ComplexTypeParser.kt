/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

/**
 * Utility for parsing complex SQL type definitions (STRUCT, ARRAY, MAP) into nested field information.
 * Supports Hive-style colon-separated fields ("name:TYPE") and space-separated fields ("name TYPE").
 */
object ComplexTypeParser {
    data class FieldDefinition(
        val name: String,
        val rawType: String,
    )

    /**
     * Result of parsing a complex type definition.
     * @param fields list of named fields extracted from the type
     * @param syntheticNode path segment(s) to insert between a parent column QN and child field names
     *   in the qualified name. Null for STRUCT (no synthetic node), "items" for ARRAY,
     *   "values" for MAP. Can be combined, e.g. "items/items" for ARRAY<ARRAY<STRUCT<...>>>.
     */
    data class ParseResult(
        val fields: List<FieldDefinition>,
        val syntheticNode: String?,
    )

    /**
     * Attempt to parse a raw type string into structured nested field information.
     * Handles STRUCT<...>, ARRAY<STRUCT<...>>, and MAP<K, STRUCT<...>> recursively.
     * Returns null if the type is not a recognized complex type with parseable fields.
     */
    fun extractStructFields(rawType: String): ParseResult? {
        val trimmed = rawType.trim()
        val upper = trimmed.uppercase()
        return when {
            upper.startsWith("STRUCT<") && trimmed.endsWith(">") -> {
                val content = trimmed.substring("STRUCT<".length, trimmed.length - 1)
                val fields = parseStructContent(content)
                fields.takeIf { it.isNotEmpty() }?.let { ParseResult(it, null) }
            }

            upper.startsWith("ARRAY<") && trimmed.endsWith(">") -> {
                val inner = trimmed.substring("ARRAY<".length, trimmed.length - 1).trim()
                val innerResult = extractStructFields(inner) ?: return null
                ParseResult(innerResult.fields, combineSyntheticNodes("items", innerResult.syntheticNode))
            }

            upper.startsWith("MAP<") && trimmed.endsWith(">") -> {
                val inner = trimmed.substring("MAP<".length, trimmed.length - 1).trim()
                val valueType = extractMapValueType(inner) ?: return null
                val innerResult = extractStructFields(valueType.trim()) ?: return null
                ParseResult(innerResult.fields, combineSyntheticNodes("values", innerResult.syntheticNode))
            }

            else -> {
                null
            }
        }
    }

    /**
     * Parse the content between the outermost STRUCT<...> brackets into field definitions.
     * Splits on commas at depth 0 (not inside nested angle brackets or parentheses).
     */
    private fun parseStructContent(content: String): List<FieldDefinition> {
        val fields = mutableListOf<FieldDefinition>()
        var depth = 0
        var start = 0
        for (i in content.indices) {
            when (content[i]) {
                '<', '(' -> {
                    depth++
                }

                '>', ')' -> {
                    depth--
                }

                ',' -> {
                    if (depth == 0) {
                        parseField(content.substring(start, i).trim())?.let { fields.add(it) }
                        start = i + 1
                    }
                }
            }
        }
        parseField(content.substring(start).trim())?.let { fields.add(it) }
        return fields
    }

    /**
     * Parse a single "name:TYPE" or "name TYPE" field definition string.
     * Prefers colon separator (Hive/BigQuery style), falls back to first whitespace.
     */
    private fun parseField(fieldStr: String): FieldDefinition? {
        if (fieldStr.isBlank()) return null
        val colonIdx = firstColonAtDepthZero(fieldStr)
        if (colonIdx > 0) {
            val name = fieldStr.substring(0, colonIdx).trim()
            val type = fieldStr.substring(colonIdx + 1).trim()
            if (name.isNotBlank() && type.isNotBlank()) return FieldDefinition(name, type)
        }
        // Fallback: "fieldName TYPE" (space-separated)
        val spaceIdx = fieldStr.indexOfFirst { it.isWhitespace() }
        if (spaceIdx > 0) {
            val name = fieldStr.substring(0, spaceIdx).trim()
            val type = fieldStr.substring(spaceIdx + 1).trim()
            if (name.isNotBlank() && type.isNotBlank()) return FieldDefinition(name, type)
        }
        return null
    }

    /**
     * Find the index of the first colon that is not inside angle brackets or parentheses.
     */
    private fun firstColonAtDepthZero(s: String): Int {
        var depth = 0
        for (i in s.indices) {
            when (s[i]) {
                '<', '(' -> depth++
                '>', ')' -> depth--
                ':' -> if (depth == 0) return i
            }
        }
        return -1
    }

    /**
     * Extract the value type from MAP<keyType, valueType> content,
     * splitting on the first comma at depth 0.
     */
    private fun extractMapValueType(mapContent: String): String? {
        var depth = 0
        for (i in mapContent.indices) {
            when (mapContent[i]) {
                '<', '(' -> depth++
                '>', ')' -> depth--
                ',' -> if (depth == 0) return mapContent.substring(i + 1).trim()
            }
        }
        return null
    }

    private fun combineSyntheticNodes(
        outer: String,
        inner: String?,
    ): String = if (inner != null) "$outer/$inner" else outer
}
