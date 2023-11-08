/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AtlanComparisonOperator implements AtlanEnum {
    LT(new String[] {"<", "lt"}),
    GT(new String[] {">", "gt"}),
    LTE(new String[] {"<=", "lte"}),
    GTE(new String[] {">=", "gte"}),
    EQ(new String[] {"=", "eq"}),
    NEQ(new String[] {"!=", "neq"}),
    IN(new String[] {"in", "IN"}),
    LIKE(new String[] {"like", "LIKE"}),
    STARTS_WITH(new String[] {"startsWith", "STARTSWITH", "begins_with", "BEGINS_WITH"}),
    ENDS_WITH(new String[] {"endsWith", "ENDSWITH", "ends_with", "ENDS_WITH"}),
    CONTAINS(new String[] {"contains", "CONTAINS"}),
    NOT_CONTAINS(new String[] {"not_contains", "NOT_CONTAINS"}),
    CONTAINS_ANY(new String[] {"containsAny", "CONTAINSANY", "contains_any", "CONTAINS_ANY"}),
    CONTAINS_ALL(new String[] {"containsAll", "CONTAINSALL", "contains_all", "CONTAINS_ALL"}),
    IS_NULL(new String[] {"isNull", "ISNULL", "is_null", "IS_NULL"}),
    NOT_NULL(new String[] {"notNull", "NOTNULL", "not_null", "NOT_NULL"}),
    TIME_RANGE(new String[] {"timerange", "TIMERANGE", "time_range", "TIME_RANGE"}),
    NOT_EMPTY(new String[] {"notEmpty", "NOTEMPTY", "not_empty", "NOT_EMPTY"});

    private final String[] symbols;

    private static final Map<String, AtlanComparisonOperator> operatorsMap = new ConcurrentHashMap<>();

    static {
        for (AtlanComparisonOperator operator : AtlanComparisonOperator.values()) {
            for (String s : operator.symbols) {
                operatorsMap.put(s, operator);
            }
        }
    }

    AtlanComparisonOperator(String[] symbols) {
        this.symbols = symbols;
    }

    /** {@inheritDoc} */
    @JsonValue
    @Override
    public String getValue() {
        return symbols[0];
    }

    public static AtlanComparisonOperator fromValue(String value) {
        return operatorsMap.getOrDefault(value, null);
    }
}
