/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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

    public List<TestAttribute> getTypeSpecificProperties() {
        List<TestAttribute> remaining = new ArrayList<>();
        for (TestAttribute testAttribute : getTestAttributes()) {
            if (!testAttribute.getRelationship() && !testAttribute.getInherited()) {
                remaining.add(testAttribute);
            }
        }
        return remaining;
    }

    public List<TestAttribute> getInheritedProperties() {
        List<TestAttribute> remaining = new ArrayList<>();
        for (TestAttribute testAttribute : getTestAttributes()) {
            if (!testAttribute.getRelationship() && testAttribute.getInherited()) {
                remaining.add(testAttribute);
            }
        }
        return remaining;
    }

    public List<TestAttribute> getTypeSpecificRelationships() {
        List<TestAttribute> remaining = new ArrayList<>();
        for (TestAttribute testAttribute : getTestAttributes()) {
            if (testAttribute.getRelationship() && !testAttribute.getInherited()) {
                remaining.add(testAttribute);
            }
        }
        return remaining;
    }

    public List<TestAttribute> getInheritedRelationships() {
        List<TestAttribute> remaining = new ArrayList<>();
        for (TestAttribute testAttribute : getTestAttributes()) {
            if (testAttribute.getRelationship() && testAttribute.getInherited()) {
                remaining.add(testAttribute);
            }
        }
        return remaining;
    }
}
