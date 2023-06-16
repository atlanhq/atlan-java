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
 * Base class for Metabase assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MetabaseQuestion.class, name = MetabaseQuestion.TYPE_NAME),
    @JsonSubTypes.Type(value = MetabaseCollection.class, name = MetabaseCollection.TYPE_NAME),
    @JsonSubTypes.Type(value = MetabaseDashboard.class, name = MetabaseDashboard.TYPE_NAME),
})
@Slf4j
public abstract class Metabase extends Asset implements IMetabase, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Metabase";

    /** TBC */
    @Attribute
    String metabaseCollectionName;

    /** TBC */
    @Attribute
    String metabaseCollectionQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
