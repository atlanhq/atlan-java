/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.atlan.AtlanClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockAtlanTenant {

    public static final AtlanClient client = new AtlanClient("http://localhost:8765", "unused");

    public void startServer() {
        // Nothing to do, server is now run via Gradle
    }

    public void stopServer() {
        try {
            client.close();
        } catch (Exception e) {
            log.error("Unable to close client used for unit tests.", e);
        }
    }
}
