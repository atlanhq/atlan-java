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
    @JsonSubTypes.Type(value = Metabase.class, name = Metabase.TYPE_NAME),
    @JsonSubTypes.Type(value = QuickSight.class, name = QuickSight.TYPE_NAME),
    @JsonSubTypes.Type(value = Thoughtspot.class, name = Thoughtspot.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBI.class, name = PowerBI.TYPE_NAME),
    @JsonSubTypes.Type(value = Preset.class, name = Preset.TYPE_NAME),
    @JsonSubTypes.Type(value = Sigma.class, name = Sigma.TYPE_NAME),
    @JsonSubTypes.Type(value = Mode.class, name = Mode.TYPE_NAME),
    @JsonSubTypes.Type(value = Qlik.class, name = Qlik.TYPE_NAME),
    @JsonSubTypes.Type(value = Tableau.class, name = Tableau.TYPE_NAME),
    @JsonSubTypes.Type(value = Looker.class, name = Looker.TYPE_NAME),
})
@Slf4j
public abstract class BI extends Catalog {

    public static final String TYPE_NAME = "BI";
}
