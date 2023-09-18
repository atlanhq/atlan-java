/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.serde.SetToStringSerializer;
import com.atlan.serde.StringToSetDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.processing.Generated;

/**
 * Options that can be set on each attribute within a type definition.
 */
@Generated(value="${generatorName}")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AttributeDefOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private static final Set<String> defaultTypes = Set.of(
<#list assetTypes as assetType>
            ${assetType}.TYPE_NAME<#sep>,</#sep>
</#list>
        );

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @return the attribute options
     */
    public static AttributeDefOptions of(AtlanCustomAttributePrimitiveType type, String optionsName) {
        AttributeDefOptionsBuilder<?, ?> builder = AttributeDefOptions.builder().primitiveType(type);
        switch (type) {
            case USERS:
            case GROUPS:
            case URL:
            case SQL:
                builder.customType(type.getValue());
                break;
            case OPTIONS:
                builder.isEnum(true).enumType(optionsName);
                break;
            default:
                // do nothing
                break;
        }
        return builder.build();
    }

    /** Optional description of the attribute. */
    String description;

    /** Set of entities on which this attribute can be applied. */
    @Builder.Default
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableEntityTypes = Set.of("Asset");

    /** Set of entities on which this attribute should appear. */
    @Builder.Default
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> customApplicableEntityTypes = defaultTypes;

    /** Whether the attribute should be searchable (true) or not (false). */
    @Builder.Default
    Boolean allowSearch = false;

    /** Maximum length allowed for a string value. */
    @Builder.Default
    String maxStrLength = "100000000";

    /** Whether this attribute should appear in the filterable facets of discovery (true) or not (false). */
    @Builder.Default
    Boolean allowFiltering = true;

    /** Whether this attribute can have multiple values (true) or only a single value (false). */
    @Builder.Default
    Boolean multiValueSelect = false;

    /** Whether users will see this attribute in the overview tab of the sidebar (true) or not (false). */
    @Builder.Default
    Boolean showInOverview = false;

    /** Whether the attribute is deprecated ("true") or not (null or "false"). */
    String isDeprecated;

    /** Primitive type of the attribute. */
    AtlanCustomAttributePrimitiveType primitiveType;

    /** Whether the attribute is an enumeration (true) or not (false). */
    @Builder.Default
    Boolean isEnum = false;

    /** Name of the enumeration (options), when the attribute is an enumeration. */
    String enumType;

    /** Used for Atlan-specific types like {@code users}, {@code groups}, {@code url}, and {@code SQL}. */
    String customType;

    /** If true for a date attribute, then time-level precision is also available in the UI (otherwise only date-level) */
    Boolean hasTimePrecision;

    /** Whether the attribute has been deleted (true) or is still active (false). */
    Boolean isArchived;

    /** When the attribute was deleted. */
    Long archivedAt;

    /** User who deleted the attribute. */
    String archivedBy;

    /** TBC */
    String isSoftReference;

    /** TBC */
    String isAppendOnPartialUpdate;

    /**
     * Append the provided set of attribute definition options onto this attribute definition.
     * Note that certain options are ignored by the append, specifically: primitiveType, isEnum,
     * enumType, customType, multiValueSelect, isArchived, archivedAt, archivedBy. These should only be set
     * by the appropriate initial setup {@link #of(AtlanCustomAttributePrimitiveType, String)} or archival
     * of an attribute definition.
     *
     * @param options to append to this set of attribute definition options
     * @return the combined set of attribute definition options
     */
    public AttributeDefOptions append(AttributeDefOptions options) {
        if (options == null) {
            return this;
        }
        if (options.description != null) {
            this.description = options.description;
        }
        if (options.applicableEntityTypes != null) {
            this.applicableEntityTypes = options.applicableEntityTypes;
        }
        if (options.customApplicableEntityTypes != null) {
            this.customApplicableEntityTypes = options.customApplicableEntityTypes;
        }
        if (options.allowSearch != null) {
            this.allowSearch = options.allowSearch;
        }
        if (options.maxStrLength != null) {
            this.maxStrLength = options.maxStrLength;
        }
        if (options.allowFiltering != null) {
            this.allowFiltering = options.allowFiltering;
        }
        if (options.showInOverview != null) {
            this.showInOverview = options.showInOverview;
        }
        if (options.isDeprecated != null) {
            this.isDeprecated = options.isDeprecated;
        }
        if (options.hasTimePrecision != null) {
            this.hasTimePrecision = options.hasTimePrecision;
        }
        return this;
    }
}
