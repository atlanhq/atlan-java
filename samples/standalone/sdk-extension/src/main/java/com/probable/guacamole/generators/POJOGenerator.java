/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.generators;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.atlan.AtlanClient;
import com.atlan.generators.DocGenerator;
import com.atlan.generators.GeneratorConfig;
import com.atlan.generators.ModelGenerator;
import com.atlan.generators.TestGenerator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.probable.guacamole.ExtendedModelGenerator;
import com.probable.guacamole.typedefs.TypeDefCreator;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

// TODO: Won't work without multiple runs due to generated POJOs being something we introspect
//  as part of subsequent steps (classes need to re-load in-between)
@Slf4j
public class POJOGenerator extends ExtendedModelGenerator {

    POJOGenerator(AtlanClient client) {
        super(client);
    }

    public static void main(String[] args) throws Exception {
        try (AtlanClient client = new AtlanClient("http://localhost:9876", "unused")) {
            String projectName = "samples" + File.separator + "standalone" + File.separator + "sdk-extension";
            GeneratorConfig cfg = GeneratorConfig.creator(
                            POJOGenerator.class, new File("resources/templates"), PACKAGE_ROOT, projectName)
                    .serviceType(TypeDefCreator.SERVICE_TYPE)
                    .build();
            startMockTenant(); // Not needed in a real setup (this simulates a running Atlan tenant)
            new ModelGenerator(client, cfg).generate();
            new TestGenerator(client, cfg).generate();
            new DocGenerator(client, cfg).generate();
            stopMockTenant(); // Not needed in a real setup (this simulates a running Atlan tenant)
        } catch (IOException e) {
            log.error("Failed to cleanup client.", e);
        }
    }

    /**
     * Set up a mocked environment with these additional type definitions already created.
     */
    protected static WireMockServer server = null;

    static void startMockTenant() {
        server = new WireMockServer(
                options().port(9876).usingFilesUnderClasspath("samples/standalone/sdk-extension/src/main/resources"));
        server.start();
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

    static void stopMockTenant() {
        server.stop();
    }
}
