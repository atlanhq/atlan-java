/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.Atlan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelGeneratorV2 {
    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    public static void main(String[] args) throws Exception {
        GeneratorConfig cfg =
                GeneratorConfig.getDefault(ModelGeneratorV2.class, "sdk").build();
        new ModelGenerator(cfg).generate();
        new TestGenerator(cfg).generate();
        new DocGenerator(cfg).generate();
    }
}
