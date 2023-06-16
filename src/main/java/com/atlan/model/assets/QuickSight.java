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
 * Base class for QuickSight assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = QuickSightFolder.class, name = QuickSightFolder.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightDashboardVisual.class, name = QuickSightDashboardVisual.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightAnalysisVisual.class, name = QuickSightAnalysisVisual.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightDatasetField.class, name = QuickSightDatasetField.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightAnalysis.class, name = QuickSightAnalysis.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightDashboard.class, name = QuickSightDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSightDataset.class, name = QuickSightDataset.TYPE_NAME),
})
@Slf4j
public abstract class QuickSight extends Asset implements IQuickSight, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "QuickSight";

    /** TBC */
    @Attribute
    String quickSightId;

    /** TBC */
    @Attribute
    String quickSightSheetId;

    /** TBC */
    @Attribute
    String quickSightSheetName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
