/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about Preset-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PresetChart.class, name = PresetChart.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetDataset.class, name = PresetDataset.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetDashboard.class, name = PresetDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetWorkspace.class, name = PresetWorkspace.TYPE_NAME),
})
public abstract class Preset extends BI {

    public static final String TYPE_NAME = "Preset";

    /** ID of the Preset asset's workspace. */
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
