/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.atlan.Atlan;
import com.atlan.model.enums.AtlanTypeCategory;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class MockAtlanTest {

    private static WireMockServer server = null;

    @BeforeSuite
    void startServer() {
        server = new WireMockServer(options().port(8765));
        server.start();
        setStubs();
        Atlan.setBaseUrl("http://localhost:8765");
        Atlan.setApiToken("unused");
    }

    private void setStubs() {
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type="
                        + AtlanTypeCategory.CUSTOM_METADATA.getValue().toLowerCase()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("typedefs-custom-metadata.json")
                        .withStatus(200)));
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type="
                        + AtlanTypeCategory.ATLAN_TAG.getValue().toLowerCase()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("typedefs-atlan-tags.json")
                        .withStatus(200)));
        server.stubFor(get(urlEqualTo("/api/meta/types/typedefs?type="
                        + AtlanTypeCategory.ENUM.getValue().toLowerCase()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("typedefs-enum.json")
                        .withStatus(200)));
    }

    @AfterSuite
    void stopServer() {
        server.stop();
    }
}
