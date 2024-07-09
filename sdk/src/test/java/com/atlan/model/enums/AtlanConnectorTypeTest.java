/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import com.atlan.model.assets.Connection;
import com.atlan.util.StringUtils;
import org.testng.annotations.Test;

public class AtlanConnectorTypeTest {

    final String validConnectorType = "default/s3/1234567890";
    final String customConnectorType = "default/made-up-connector/1234567890";
    final String emptyConnectorType = "";

    @Test
    void validConnectorType() {
        AtlanConnectorType type = Connection.getConnectorTypeFromQualifiedName(validConnectorType);
        assertNotNull(type);
        assertEquals(type, AtlanConnectorType.S3);
    }

    @Test
    void emptyConnectorType() {
        AtlanConnectorType type = Connection.getConnectorTypeFromQualifiedName(emptyConnectorType);
        assertNull(type);
    }

    @Test
    void customConnectorType() {
        AtlanConnectorType type = Connection.getConnectorTypeFromQualifiedName(customConnectorType);
        assertNotNull(type);
        assertEquals(type, AtlanConnectorType.UNKNOWN_CUSTOM);
    }
}
