/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about Catalog-related assets. Only types that inherit from Catalog can participate in lineage.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Catalog extends Asset {

    public static final String TYPE_NAME = "Catalog";

    /** Processes that use this object as input. */
    @Attribute
    @Singular
    List<Process> inputToProcesses;

    /** Processes that produce this object as output. */
    @Attribute
    @Singular
    List<Process> outputFromProcesses;
}
