/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for API assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = APISpec.class, name = APISpec.TYPE_NAME),
    @JsonSubTypes.Type(value = APIPath.class, name = APIPath.TYPE_NAME),
})
@Slf4j
@SuppressWarnings("cast")
public abstract class API extends Asset implements IAPI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "API";

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** TBC */
    @Attribute
    Boolean apiIsAuthOptional;

    /** TBC */
    @Attribute
    String apiSpecName;

    /** TBC */
    @Attribute
    String apiSpecQualifiedName;

    /** TBC */
    @Attribute
    String apiSpecType;

    /** TBC */
    @Attribute
    String apiSpecVersion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
