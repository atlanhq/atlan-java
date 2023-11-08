/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.io.File;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SerdeGenerator {

    public static final String DIRECTORY = "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "serde";

    private final SortedSet<String> assetTypes;
    private final String generatorName;

    public SerdeGenerator(Collection<AssetGenerator> assetCache, GeneratorConfig cfg) {
        this.generatorName = cfg.getGeneratorName();
        assetTypes = new TreeSet<>();
        for (AssetGenerator generator : assetCache) {
            if (!generator.isAbstract() && cfg.includeTypedef(generator.getEntityDef())) {
                assetTypes.add(generator.getClassName());
            }
        }
    }
}
