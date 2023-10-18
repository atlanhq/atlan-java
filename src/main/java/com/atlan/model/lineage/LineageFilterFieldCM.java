/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.fields.CustomMetadataField;
import com.atlan.util.TypeUtils;
import lombok.Getter;

/**
 * Class used to provide a proxy to building up a lineage filter with the appropriate
 * subset of conditions available, for custom metadata fields.
 */
@Getter
public class LineageFilterFieldCM extends LineageFilterField {

    private final CustomMetadataField cmField;

    public LineageFilterFieldCM(CustomMetadataField field) {
        super(field);
        this.cmField = field;
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value equals (case-sensitive)
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public LineageFilter eq(String value) {
        return build(AtlanComparisonOperator.EQ, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value equals (case-sensitive)
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public LineageFilter eq(AtlanEnum value) {
        return build(AtlanComparisonOperator.EQ, value.getValue());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not equal (case-sensitive)
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public LineageFilter neq(String value) {
        return build(AtlanComparisonOperator.NEQ, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not equal (case-sensitive)
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public LineageFilter neq(AtlanEnum value) {
        return build(AtlanComparisonOperator.NEQ, value.getValue());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that starts with
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value (prefix) to check the field's value starts with (case-sensitive)
     * @return a filter that will only match assets whose value for the field starts with the value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public LineageFilter startsWith(String value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "startsWith", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.STARTS_WITH, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that ends with
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value (suffix) to check the field's value ends with (case-sensitive)
     * @return a filter that will only match assets whose value for the field ends with the value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public LineageFilter endsWith(String value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "endsWith", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.ENDS_WITH, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that contains
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value contains (case-sensitive)
     * @return a filter that will only match assets whose value for the field contains the value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public LineageFilter contains(String value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "contains", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.CONTAINS, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that does not contain
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not contain (case-sensitive)
     * @return a filter that will only match assets whose value for the field does not contain the value provided
     * @throws InvalidRequestException if the custom metadata field is not string-comparable
     */
    public LineageFilter doesNotContain(String value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.STRING)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "contains", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.NOT_CONTAINS, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is strictly less than
     * the provided value.
     *
     * @param value the value to check the field's value is strictly less than
     * @return a filter that will only match assets whose value for the field is strictly less than the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter lt(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.LT, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is strictly greater than
     * the provided value.
     *
     * @param value the value to check the field's value is strictly greater than
     * @return a filter that will only match assets whose value for the field is strictly greater than the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter gt(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.GT, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is less than or
     * equal to the provided value.
     *
     * @param value the value to check the field's value is less than or equal to
     * @return a filter that will only match assets whose value for the field is less than or equal to the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter lte(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.LTE, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is greater than or
     * equal to the provided value.
     *
     * @param value the value to check the field's value is greater than or equal to
     * @return a filter that will only match assets whose value for the field is greater than or equal to the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter gte(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.GTE, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value.
     *
     * @param value the value to check the field's value equals
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter eq(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.EQ, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value.
     *
     * @param value the value to check the field's value does not equal
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     * @throws InvalidRequestException if the custom metadata field is not numerically-comparable
     */
    public <T extends Number> LineageFilter neq(T value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.NUMBER)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.NEQ, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value.
     *
     * @param value the value to check the field's value equals
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     * @throws InvalidRequestException if the custom metadata field is not boolean-comparable
     */
    public LineageFilter eq(Boolean value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.BOOLEAN)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.EQ, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value.
     *
     * @param value the value to check the field's value does not equal
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     * @throws InvalidRequestException if the custom metadata field is not boolean-comparable
     */
    public LineageFilter neq(Boolean value) throws InvalidRequestException {
        if (!TypeUtils.isComparable(cmField.getAttributeDef().getTypeName(), TypeUtils.ComparisonCategory.BOOLEAN)) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_QUERY, "eq", cmField.getSetName() + "." + cmField.getAttributeName());
        }
        return build(AtlanComparisonOperator.NEQ, value.toString());
    }

    // TODO: TIME_RANGE
    // TODO: NOT_EMPTY, IN, LIKE, CONTAINS_ANY, CONTAINS_ALL
}
