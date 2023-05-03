/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RedashDashboard.class, name = RedashDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = RedashQuery.class, name = RedashQuery.TYPE_NAME),
    @JsonSubTypes.Type(value = RedashVisualization.class, name = RedashVisualization.TYPE_NAME),
})
@Slf4j
public abstract class Redash extends BI {

    public static final String TYPE_NAME = "Redash";

    /** TBC */
    @Attribute
    Boolean redashIsPublished;
}
