/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanTypeCategory;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for all type definitions (typedefs) in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "category")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AtlanTagDef.class, name = "CLASSIFICATION"),
    @JsonSubTypes.Type(value = EnumDef.class, name = "ENUM"),
    @JsonSubTypes.Type(value = CustomMetadataDef.class, name = "BUSINESS_METADATA"),
    @JsonSubTypes.Type(value = EntityDef.class, name = "ENTITY"),
    @JsonSubTypes.Type(value = RelationshipDef.class, name = "RELATIONSHIP"),
    @JsonSubTypes.Type(value = StructDef.class, name = "STRUCT"),
})
@ToString(callSuper = true)
@SuppressWarnings("serial")
public abstract class TypeDef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of the type definition itself. */
    transient AtlanTypeCategory category;

    /** Unique identifier for the type definition. */
    String guid;

    /** User who created the type definition. */
    String createdBy;

    /** User who last updated the type definition. */
    String updatedBy;

    /** Time (epoch) in milliseconds when the type definition was created. */
    Long createTime;

    /** Time (epoch) in milliseconds when the type definition was last updated. */
    Long updateTime;

    /** TBC */
    Long version;

    /** Internal hashed-string name for the type definition. */
    String name;

    /** Explanation of the type definition. */
    String description;

    /** TBC */
    String serviceType;

    /** TBC */
    String typeVersion;

    /** List of attributes defined within the type definition. */
    @Singular
    List<AttributeDef> attributeDefs;

    /** Human-readable name of the type definition. */
    String displayName;

    public abstract static class TypeDefBuilder<C extends TypeDef, B extends TypeDefBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
