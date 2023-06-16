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
 * Object store assets
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = S3.class, name = S3.TYPE_NAME),
    @JsonSubTypes.Type(value = ADLS.class, name = ADLS.TYPE_NAME),
    @JsonSubTypes.Type(value = GCS.class, name = GCS.TYPE_NAME),
})
@Slf4j
public abstract class ObjectStore extends Asset implements IObjectStore, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "ObjectStore";

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
