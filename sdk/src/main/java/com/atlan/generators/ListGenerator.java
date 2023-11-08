/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.util.List;
import lombok.Getter;

@Getter
public class ListGenerator {

    private final List<String> items;
    private final String generatorName;

    public ListGenerator(List<String> items, String generatorName) {
        this.items = items;
        this.generatorName = generatorName;
    }
}
