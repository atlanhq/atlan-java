/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for MicroStrategy assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MicroStrategyReport.class, name = MicroStrategyReport.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyProject.class, name = MicroStrategyProject.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyMetric.class, name = MicroStrategyMetric.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyCube.class, name = MicroStrategyCube.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyDossier.class, name = MicroStrategyDossier.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyFact.class, name = MicroStrategyFact.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyDocument.class, name = MicroStrategyDocument.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyAttribute.class, name = MicroStrategyAttribute.TYPE_NAME),
    @JsonSubTypes.Type(value = MicroStrategyVisualization.class, name = MicroStrategyVisualization.TYPE_NAME),
})
@Slf4j
@SuppressWarnings("cast")
public abstract class MicroStrategy extends Asset implements IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "MicroStrategy";

    /** Date when the asset was certified in MicroStrategy. */
    @Attribute
    Long microStrategyCertifiedAt;

    /** User who certified the asset in MicroStrategy. */
    @Attribute
    String microStrategyCertifiedBy;

    /** Simple names of the related MicroStrategy cubes. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** Unique names of the related MicroStrategy cubes. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** Whether the asset is certified in MicroStrategy (true) or not (false). */
    @Attribute
    Boolean microStrategyIsCertified;

    /** Location of the asset within MicroStrategy. */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Simple name of the related MicroStrategy project. */
    @Attribute
    String microStrategyProjectName;

    /** Unique name of the related MicroStrategy project. */
    @Attribute
    String microStrategyProjectQualifiedName;

    /** Simple names of the related MicroStrategy reports. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportNames;

    /** Unique names of the related MicroStrategy reports. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportQualifiedNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
