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
 * Base class for ThoughtSpot assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ThoughtspotLiveboard.class, name = ThoughtspotLiveboard.TYPE_NAME),
    @JsonSubTypes.Type(value = ThoughtspotDashlet.class, name = ThoughtspotDashlet.TYPE_NAME),
    @JsonSubTypes.Type(value = ThoughtspotAnswer.class, name = ThoughtspotAnswer.TYPE_NAME),
})
@Slf4j
public abstract class Thoughtspot extends Asset implements IThoughtspot, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Thoughtspot";

    /** TBC */
    @Attribute
    String thoughtspotChartType;

    /** TBC */
    @Attribute
    String thoughtspotQuestionText;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
