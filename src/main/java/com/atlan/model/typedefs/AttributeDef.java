/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.cache.EnumCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the structure of a single attribute for a type definition in Atlan.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AttributeDef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Instantiate an attribute definition from the provided parameters.
     *
     * @param displayName human-readable name of the attribute
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @param multiValued true if multiple values are allowed for the attribute, otherwise false
     * @return the attribute definition
     * @throws AtlanException if there is any API error trying to construct the attribute (usually due to a non-existent enumeration)
     */
    public static AttributeDef of(
            String displayName, AtlanCustomAttributePrimitiveType type, String optionsName, boolean multiValued)
            throws AtlanException {
        AttributeDefBuilder<?, ?> builder =
                AttributeDef.builder().name(displayName).displayName(displayName);
        String baseType;
        boolean addEnumValues = false;
        switch (type) {
            case OPTIONS:
                baseType = optionsName;
                addEnumValues = true;
                break;
            case USERS:
            case GROUPS:
            case URL:
            case SQL:
                baseType = AtlanCustomAttributePrimitiveType.STRING.getValue();
                break;
            default:
                baseType = type.getValue();
                break;
        }
        if (multiValued) {
            builder = builder.typeName("array<" + baseType + ">")
                    .options(AttributeDefOptions.of(type, optionsName).toBuilder()
                            .multiValueSelect(true)
                            .build());
        } else {
            builder = builder.typeName(baseType).options(AttributeDefOptions.of(type, optionsName));
        }
        if (addEnumValues) {
            builder = builder.enumValues(EnumCache.getByName(optionsName).getValidValues());
        }
        return builder.build();
    }

    /** Internal hashed-string name for the attribute. */
    String name;

    /** Human-readable name of the attribute. */
    String displayName;

    /** Explanation of the attribute. */
    String description;

    /**
     * Type of the attribute.
     * <ul>
     *   <li>This can either be a primitive Atlan type or the name of a custom metadata enumeration (options).</li>
     *   <li>The primitive Atlan types' values are defined in the {@link AtlanCustomAttributePrimitiveType} enumeration.</li>
     *   <li>Note: there are a number of custom types there as well ({@code users}, {@code groups}, {@code url}, and {@code SQL}). The {@code typeName} for all of these custom types is {@code STRING}, and the more detailed type only appears in the {@link #options}.</li>
     *   <li>For fields that can be multivalued, use {@code array<type>} for the typeName.</li>
     * </ul>
     */
    String typeName;

    /** Specifies an (optional) default value for the attribute. */
    String defaultValue;

    /** Indicates whether the attribute is mandatory (false) or optional (true). */
    @Builder.Default
    Boolean isOptional = true;

    /** Specifies whether the attribute is single or multivalued. */
    @Builder.Default
    AtlanCustomAttributeCardinality cardinality = AtlanCustomAttributeCardinality.SINGLE;

    /** Specifies the minimum number of values the attribute can have. */
    @Builder.Default
    Long valuesMinCount = 0L;

    /** Specifies the maximum number of values the attribute can have. */
    @Builder.Default
    Long valuesMaxCount = 1L;

    /** Specifies whether the attribute must have unique values (true) or not (false). */
    @Builder.Default
    Boolean isUnique = false;

    /** Specifies whether the attribute can be searched (true) or not (false). */
    @Builder.Default
    Boolean isIndexable = true;

    /** Whether changes to this attribute's value generate an event (true) or not (false). */
    @Builder.Default
    Boolean includeInNotification = false;

    /** TBC */
    final Boolean skipScrubbing;

    /** TBC */
    final Long searchWeight;

    /** TBC */
    final String indexType;

    /** Options for the attribute. */
    AttributeDefOptions options;

    /** Whether the attribute is being newly created (true) or not (false). */
    @Builder.Default
    Boolean isNew = true;

    /** List of values for an enumeration. */
    @Singular
    List<String> enumValues;

    /** TBC */
    final List<Constraint> constraints;

    /** TBC */
    final Map<String, String> indexTypeESConfig;

    /** TBC */
    final Map<String, Map<String, String>> indexTypeESFields;

    /** TBC */
    final Map<String, List<String>> autoUpdateAttributes;
}
