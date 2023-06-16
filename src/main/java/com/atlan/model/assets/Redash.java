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
 * Base class for Redash assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RedashDashboard.class, name = RedashDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = RedashQuery.class, name = RedashQuery.TYPE_NAME),
    @JsonSubTypes.Type(value = RedashVisualization.class, name = RedashVisualization.TYPE_NAME),
})
@Slf4j
public abstract class Redash extends Asset implements IRedash, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Redash";

    /** Whether the asset is published in Redash (true) or not (false). */
    @Attribute
    Boolean redashIsPublished;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
