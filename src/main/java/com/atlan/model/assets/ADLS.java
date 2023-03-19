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
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ADLSAccount.class, name = ADLSAccount.TYPE_NAME),
    @JsonSubTypes.Type(value = ADLSContainer.class, name = ADLSContainer.TYPE_NAME),
    @JsonSubTypes.Type(value = ADLSObject.class, name = ADLSObject.TYPE_NAME),
})
public abstract class ADLS extends Azure {

    public static final String TYPE_NAME = "ADLS";

    /** TBC */
    @Attribute
    String adlsAccountQualifiedName;
}
