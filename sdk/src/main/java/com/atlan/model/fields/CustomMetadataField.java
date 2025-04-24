/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.util.TypeUtils;
import java.util.Collection;
import lombok.Getter;

/**
 * Utility class to simplify searching for values on custom metadata attributes.
 */
@Getter
public class CustomMetadataField extends SearchableField {

    private final String setName;
    private final String attributeName;
    private final AttributeDef attributeDef;

    // TODO public final LineageFilterFieldCM inLineage = new LineageFilterFieldCM(this);

    /**
     * Default constructor.
     *
     * @param client connectivity to Atlan tenant with the custom metadata
     * @param setName human-readable name of the custom metadata set
     * @param attributeName human-readable name of the custom metadata attribute
     * @throws AtlanException on any issues communicating with the API, or if the custom metadata (attribute) does not exist
     */
    @SuppressWarnings("this-escape")
    public CustomMetadataField(AtlanClient client, String setName, String attributeName) throws AtlanException {
        super(
                client.getCustomMetadataCache().getAttributeForSearchResults(setName, attributeName),
                client.getCustomMetadataCache().getAttrIdForName(setName, attributeName));
        this.setName = setName;
        this.attributeName = attributeName;
        this.attributeDef = client.getCustomMetadataCache().getAttributeDef(getElasticFieldName());
    }

    /**
     * Factory method shortcut to the default constructor.
     *
     * @param client connectivity to Atlan tenant with the custom metadata
     * @param set human-readable name of the custom metadata set
     * @param attr human-readable name of the custom metadata attribute
     * @throws AtlanException on any issues communicating with the API, or if the custom metadata (attribute) does not exist
     */
    public static CustomMetadataField of(AtlanClient client, String set, String attr) throws AtlanException {
        return new CustomMetadataField(client, set, attr);
    }

    /** Retrieve the name of the field as it can be searched. */
    public String getSearchableFieldName() {
        return super.getElasticFieldName();
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that starts with
     * the provided value.
     *
     * @param value the value (prefix) to check the field's value starts with
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field starts with the value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public Query startsWith(String value, boolean caseInsensitive) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "startsWith", setName + "." + attributeName);
        }
        return IKeywordSearchable.startsWith(getElasticFieldName(), value, caseInsensitive);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that exactly equals
     * the provided enumerated value.
     *
     * @param value the value (enumerated) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the enumerated value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public Query eq(AtlanEnum value) throws InvalidRequestException {
        if (attributeDef.getEnumValues() != null
                && !attributeDef.getEnumValues().isEmpty()) {
            return eq(value.getValue(), false);
        }
        return IKeywordSearchable.eq(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that exactly equals
     * the provided boolean value.
     *
     * @param value the value (boolean) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the boolean value provided
     * @throws InvalidRequestException if the custom metadata field is not boolean-comparable
     */
    public Query eq(boolean value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.BOOLEAN)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "eq", setName + "." + attributeName);
        }
        return IBooleanSearchable.eq(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that exactly equals
     * the provided string value. Note that this can also be modified to ignore case when doing the exact match.
     *
     * @param value the value (string) to check the field's value is exactly equal to
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field is exactly equal to the string value provided (optionally case-insensitive)
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public Query eq(String value, boolean caseInsensitive) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "eq", setName + "." + attributeName);
        }
        return IKeywordSearchable.eq(getElasticFieldName(), value, caseInsensitive);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that exactly equals
     * at least one of the provided string values.
     *
     * @param values the values (strings) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to at least one of the string values provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public Query in(Collection<String> values) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "in", setName + "." + attributeName);
        }
        return IKeywordSearchable.in(getElasticFieldName(), values);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that exactly
     * matches the provided numeric value.
     *
     * @param value the numeric value to exactly match
     * @return a query that will only match assets whose value for the field is exactly the numeric value provided
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query eq(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "eq", setName + "." + attributeName);
        }
        return INumericallySearchable.eq(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that is strictly greater
     * than the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly greater than the numeric value provided
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query gt(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "gt", setName + "." + attributeName);
        }
        return INumericallySearchable.gt(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is greater
     * than or equal to the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is greater than or equal to the numeric value provided
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query gte(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "gte", setName + "." + attributeName);
        }
        return INumericallySearchable.gte(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that is strictly less
     * than the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly less than the numeric value provided
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query lt(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "lt", setName + "." + attributeName);
        }
        return INumericallySearchable.lt(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value that is less
     * than or equal to the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is less than or equal to the numeric value provided
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query lte(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "lte", setName + "." + attributeName);
        }
        return INumericallySearchable.lte(getElasticFieldName(), value);
    }

    /**
     * Returns a query that will match all assets whose custom metadata attribute has a value between the
     * minimum and maximum specified values, inclusive.
     *
     * @param min minimum value of the field that will match (inclusive)
     * @param max maximum value of the field that will match (inclusive)
     * @return a query that will only match assets whose value for the field is between the min and max (both inclusive)
     * @param <T> numeric values
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> Query between(T min, T max) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "between", setName + "." + attributeName);
        }
        return INumericallySearchable.between(getElasticFieldName(), min, max);
    }

    /**
     * Returns a query that will textually match the provided value against the field. This
     * analyzes the provided value according to the same analysis carried out on the field
     * (for example, tokenization, stemming, and so on).
     *
     * @param value the string value to match against
     * @return a query that will only match assets whose analyzed value for the field matches the value provided (which will also be analyzed)
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public Query match(String value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(attributeDef.getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "match", setName + "." + attributeName);
        }
        return ITextSearchable.match(getElasticFieldName(), value);
    }
}
