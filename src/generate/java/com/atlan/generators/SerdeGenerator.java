/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
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

    public SerdeGenerator(Collection<AssetGenerator> assetCache) {
        assetTypes = new TreeSet<>();
        for (AssetGenerator generator : assetCache) {
            if (!generator.isAbstract() && !AssetGenerator.SKIP_GENERATING.contains(generator.getOriginalName())) {
                assetTypes.add(generator.getClassName());
            }
        }
    }
}
