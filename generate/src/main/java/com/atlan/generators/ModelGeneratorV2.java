/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ModelGeneratorV2 {
    public static void main(String[] args) throws Exception {
        try (AtlanClient client = new AtlanClient()) {
            GeneratorConfig cfg =
                GeneratorConfig.getDefault(ModelGeneratorV2.class, "sdk").build();
            new ModelGenerator(client, cfg).generate();
            new TestGenerator(client, cfg).generate();
            new DocGenerator(client, cfg).generate();
        } catch (IOException e) {
            log.error("Unable to teardown client used for model generation.", e);
        }
    }
}
