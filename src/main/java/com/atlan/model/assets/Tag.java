/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.SourceTagAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all Tag assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SnowflakeTag.class, name = SnowflakeTag.TYPE_NAME),
})
@Slf4j
public abstract class Tag extends Catalog {

    public static final String TYPE_NAME = "Tag";

    /** Unique identifier of the tag in the source system. */
    @Attribute
    String tagId;

    /** Attributes associated with the tag in the source system. */
    @Attribute
    @Singular
    List<SourceTagAttribute> tagAttributes;

    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    @Attribute
    @Singular
    SortedSet<String> tagAllowedValues;

    /** Name of the classification in Atlan that is mapped to this tag. */
    @Attribute
    @JsonProperty("mappedClassificationName")
    String mappedAtlanTagName;
}
