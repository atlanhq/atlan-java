/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.api.QueryParserEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.ParsedQuery;
import com.atlan.model.admin.QueryParserRequest;
import com.atlan.model.enums.QueryParserSourceType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

public class QueryParserTest extends AtlanLiveTest {

    @Test(groups = {"query.parse.valid"})
    void parseValidQuery() throws AtlanException {
        QueryParserRequest request = QueryParserRequest.creator(
                        "INSERT INTO orders (order_name, customer_id, product_id) VALUES(SELECT 'test_order', id, 21 FROM customers)",
                        QueryParserSourceType.SNOWFLAKE)
                .defaultDatabase("ORDERS")
                .defaultSchema("PRODUCTION")
                .build();
        ParsedQuery response = QueryParserEndpoint.parseQuery(request);
        assertNotNull(response);
        assertFalse(response.getObjects().isEmpty());
        assertFalse(response.getRelationships().isEmpty());
        assertTrue(response.getErrors() == null || response.getErrors().isEmpty());
    }

    @Test(groups = {"query.parse.invalid"})
    void parseInvalidQuery() throws AtlanException {
        QueryParserRequest request = QueryParserRequest.creator(
                        "INSERT INTO orders (order_name, customer_id, product_id) VALUES(SELECT 'test_order', id, 21 FROM customers) with some extra",
                        QueryParserSourceType.SNOWFLAKE)
                .defaultDatabase("ORDERS")
                .defaultSchema("PRODUCTION")
                .build();
        ParsedQuery response = QueryParserEndpoint.parseQuery(request);
        assertNotNull(response);
        assertTrue(response.getObjects().isEmpty());
        assertTrue(response.getRelationships().isEmpty());
        List<ParsedQuery.ParseError> errors = response.getErrors();
        assertFalse(errors.isEmpty());
        assertEquals(errors.size(), 2);
        Set<String> errorTypes =
                errors.stream().map(ParsedQuery.ParseError::getErrorType).collect(Collectors.toSet());
        assertEquals(errorTypes.size(), 1);
        assertTrue(errorTypes.contains("SyntaxError"));
    }
}
