/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.atlan.mock.MockAtlanTenant;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class MockTenant extends MockAtlanTenant {
    @BeforeSuite
    void start() {
        startServer();
        addStubs();
    }

    void addStubs() {
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type=enum"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("probable-guacamole-enums.json")
                        .withStatus(200)));
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type=struct"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("probable-guacamole-structs.json")
                        .withStatus(200)));
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type=entity"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("probable-guacamole-entities.json")
                        .withStatus(200)));
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type=relationship"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("probable-guacamole-relationships.json")
                        .withStatus(200)));
    }

    @AfterSuite
    void stop() {
        stopServer();
    }
}
