/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DbtColumnProcess.class, name = DbtColumnProcess.TYPE_NAME),
    @JsonSubTypes.Type(value = ColumnProcess.class, name = ColumnProcess.TYPE_NAME),
})
public abstract class AbstractColumnProcess extends AbstractProcess {

    public static final String TYPE_NAME = "ColumnProcess";

    /** TBC */
    @Attribute
    LineageProcess process;
}
