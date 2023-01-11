/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LookerLook.class, name = LookerLook.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerDashboard.class, name = LookerDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerFolder.class, name = LookerFolder.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerTile.class, name = LookerTile.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerModel.class, name = LookerModel.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerExplore.class, name = LookerExplore.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerProject.class, name = LookerProject.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerQuery.class, name = LookerQuery.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerField.class, name = LookerField.TYPE_NAME),
    @JsonSubTypes.Type(value = LookerView.class, name = LookerView.TYPE_NAME),
})
public abstract class Looker extends BI {

    public static final String TYPE_NAME = "Looker";
}
