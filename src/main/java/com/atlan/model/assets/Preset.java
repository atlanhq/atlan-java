/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Preset assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PresetChart.class, name = PresetChart.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetDataset.class, name = PresetDataset.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetDashboard.class, name = PresetDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = PresetWorkspace.class, name = PresetWorkspace.TYPE_NAME),
})
@Slf4j
public abstract class Preset extends Asset implements IPreset, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Preset";

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** ID of the Preset asset's collection. */
    @Attribute
    Long presetDashboardId;

    /** qualifiedName of the Preset asset's collection. */
    @Attribute
    String presetDashboardQualifiedName;

    /** ID of the Preset asset's workspace. */
    @Attribute
    Long presetWorkspaceId;

    /** qualifiedName of the Preset asset's workspace. */
    @Attribute
    String presetWorkspaceQualifiedName;
}
