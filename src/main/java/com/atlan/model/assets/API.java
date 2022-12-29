/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for API assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = APISpec.class, name = APISpec.TYPE_NAME),
    @JsonSubTypes.Type(value = APIPath.class, name = APIPath.TYPE_NAME),
})
@SuppressWarnings("cast")
public abstract class API extends Catalog {

    public static final String TYPE_NAME = "API";

    /** TBC */
    @Attribute
    String apiSpecType;

    /** TBC */
    @Attribute
    String apiSpecVersion;

    /** TBC */
    @Attribute
    String apiSpecName;

    /** TBC */
    @Attribute
    String apiSpecQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** TBC */
    @Attribute
    Boolean apiIsAuthOptional;
}
