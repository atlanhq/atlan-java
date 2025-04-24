/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetDocGenerator extends AssetTestGenerator {

    public static final String DIRECTORY = "generate" + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "markdown";

    private final Set<String> superTypes;

    @SuppressWarnings("this-escape")
    public AssetDocGenerator(AssetGenerator asset, GeneratorConfig cfg) {
        super(asset, cfg);
        this.superTypes = cache.getAllSuperTypesForType(getOriginalName());
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
