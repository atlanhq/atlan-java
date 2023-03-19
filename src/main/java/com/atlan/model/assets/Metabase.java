/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for Metabase assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MetabaseQuestion.class, name = MetabaseQuestion.TYPE_NAME),
    @JsonSubTypes.Type(value = MetabaseCollection.class, name = MetabaseCollection.TYPE_NAME),
    @JsonSubTypes.Type(value = MetabaseDashboard.class, name = MetabaseDashboard.TYPE_NAME),
})
public abstract class Metabase extends BI {

    public static final String TYPE_NAME = "Metabase";

    /** TBC */
    @Attribute
    String metabaseCollectionName;

    /** TBC */
    @Attribute
    String metabaseCollectionQualifiedName;
}
