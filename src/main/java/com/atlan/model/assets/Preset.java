/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about Preset-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Preset extends Asset {

    public static final String TYPE_NAME = "Preset";

    /**
     * ID of the Preset asset's workspace.
     */
    @Attribute
    Long presetWorkspaceId;

    /** qualifiedName of the Preset asset's workspace. */
    @Attribute
    String presetWorkspaceQualifiedName;

    /** ID of the Preset asset's collection. */
    @Attribute
    Long presetDashboardId;

    /** qualifiedName of the Preset asset's collection. */
    @Attribute
    String presetDashboardQualifiedName;
}
