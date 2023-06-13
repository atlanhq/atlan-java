/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import java.util.List;
import lombok.Getter;

@Getter
public class ListGenerator {

    private final List<String> items;

    public ListGenerator(List<String> items) {
        this.items = items;
    }
}
