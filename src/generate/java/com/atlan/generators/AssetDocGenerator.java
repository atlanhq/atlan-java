/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.io.File;
import java.util.LinkedHashSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetDocGenerator extends AssetTestGenerator {

    public static final String DIRECTORY =
            "src" + File.separator + "generate" + File.separator + "resources" + File.separator + "markdown";

    private static final String ASSET_GUID = "705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23";
    private static final String ASSET_QN = "default/snowflake/1234567890/test/qualifiedName";

    private final LinkedHashSet<String> superTypes;

    public AssetDocGenerator(AssetGenerator asset) {
        super(asset);
        this.superTypes = ModelGeneratorV2.getAllSuperTypesForType(getOriginalName());
    }
}
