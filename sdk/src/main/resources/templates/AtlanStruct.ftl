/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import com.atlan.serde.StructDeserializer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Generated;

/**
 * Base class for all structs.
 */
@Generated(value="${generatorName}")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = StructDeserializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "typeName")
@JsonSubTypes({
<#list items as className>
    @JsonSubTypes.Type(value = ${className}.class, name = ${className}.TYPE_NAME),
</#list>
})
@Slf4j
public abstract class AtlanStruct extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the type that defines the struct. */
    String typeName;

    public abstract static class AtlanStructBuilder<C extends AtlanStruct, B extends AtlanStructBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
