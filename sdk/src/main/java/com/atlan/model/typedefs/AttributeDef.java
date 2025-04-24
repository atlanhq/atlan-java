/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanAttributeType;
import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the structure of a single attribute for a type definition in Atlan.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AttributeDef extends AtlanObject implements Comparable<AttributeDef> {
    private static final long serialVersionUID = 2L;

    // Sort attribute definitions in a set based purely on their name (two attributes
    // in the same set with the same name should be a conflict / duplicate)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<AttributeDef> attributeDefComparator =
            Comparator.comparing(AttributeDef::getName, stringComparator);

    /**
     * Instantiate an attribute definition from the provided parameters.
     *
     * @param client connectivity to the Atlan tenant on which this attribute is intended to be created
     * @param displayName human-readable name of the attribute
     * @param type primitive type of the attribute (non-enum)
     * @param multiValued true if multiple values are allowed for the attribute, otherwise false
     * @return the attribute definition
     * @throws AtlanException if there is any API error trying to construct the attribute (usually due to a non-existent enumeration)
     */
    public static AttributeDef of(
            AtlanClient client, String displayName, AtlanCustomAttributePrimitiveType type, boolean multiValued)
            throws AtlanException {
        return of(client, displayName, type, null, multiValued);
    }

    /**
     * Instantiate an attribute definition from the provided parameters.
     *
     * @param client connectivity to the Atlan tenant on which this attribute is intended to be created
     * @param displayName human-readable name of the attribute
     * @param type primitive type of the attribute (non-enum)
     * @param multiValued true if multiple values are allowed for the attribute, otherwise false
     * @param otherOptions other options to set on the attribute
     * @return the attribute definition
     * @throws AtlanException if there is any API error trying to construct the attribute (usually due to a non-existent enumeration)
     */
    public static AttributeDef of(
            AtlanClient client,
            String displayName,
            AtlanCustomAttributePrimitiveType type,
            boolean multiValued,
            AttributeDefOptions otherOptions)
            throws AtlanException {
        return of(client, displayName, type, null, multiValued, otherOptions);
    }

    /**
     * Instantiate an attribute definition from the provided parameters.
     *
     * @param client connectivity to the Atlan tenant on which this attribute is intended to be created
     * @param displayName human-readable name of the attribute
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @param multiValued true if multiple values are allowed for the attribute, otherwise false
     * @return the attribute definition
     * @throws AtlanException if there is any API error trying to construct the attribute (usually due to a non-existent enumeration)
     */
    public static AttributeDef of(
            AtlanClient client,
            String displayName,
            AtlanCustomAttributePrimitiveType type,
            String optionsName,
            boolean multiValued)
            throws AtlanException {
        return of(client, displayName, type, optionsName, multiValued, null);
    }

    /**
     * Instantiate an attribute definition from the provided parameters.
     *
     * @param client connectivity to the Atlan tenant on which this attribute is intended to be created
     * @param displayName human-readable name of the attribute
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @param multiValued true if multiple values are allowed for the attribute, otherwise false
     * @param otherOptions other options to set on the attribute
     * @return the attribute definition
     * @throws AtlanException if there is any API error trying to construct the attribute (usually due to a non-existent enumeration)
     */
    public static AttributeDef of(
            AtlanClient client,
            String displayName,
            AtlanCustomAttributePrimitiveType type,
            String optionsName,
            boolean multiValued,
            AttributeDefOptions otherOptions)
            throws AtlanException {
        AttributeDefBuilder<?, ?> builder = AttributeDef.builder().displayName(displayName);
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
            builder.typeName("array<" + baseType + ">")
                    .cardinality(AtlanCustomAttributeCardinality.SET)
                    .options(AttributeDefOptions.of(client, type, optionsName, otherOptions).toBuilder()
                            .multiValueSelect(true)
                            .build());
        } else {
            builder.typeName(baseType).options(AttributeDefOptions.of(client, type, optionsName, otherOptions));
        }
        if (addEnumValues) {
            builder.enumValues(client.getEnumCache().getByName(optionsName).getValidValues());
        }
        return builder.build();
    }

    /**
     * Build up an attribute definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the attribute
     * @param type primitive type of the attribute (non-enum, non-struct)
     * @return a builder for an attribute definition
     */
    public static AttributeDefBuilder<?, ?> creator(String name, AtlanAttributeType type) {
        return creator(name, type, null, AtlanCustomAttributeCardinality.SINGLE);
    }

    /**
     * Build up an attribute definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the attribute
     * @param type type of the attribute
     * @param relatedObjectType name of the enumeration or struct, if the attribute type is an enumeration or struct (can be null otherwise)
     * @param cardinality whether the attribute is single or multivalued
     * @return a builder for an attribute definition
     */
    public static AttributeDefBuilder<?, ?> creator(
            String name,
            AtlanAttributeType type,
            String relatedObjectType,
            AtlanCustomAttributeCardinality cardinality) {
        String typeName;
        switch (type) {
            case ENUM:
            case STRUCT:
                typeName = relatedObjectType;
                break;
            default:
                typeName = type.getValue();
                break;
        }
        if (cardinality != AtlanCustomAttributeCardinality.SINGLE) {
            typeName = "array<" + typeName + ">";
        }
        return AttributeDef.builder().name(name).typeName(typeName).cardinality(cardinality);
    }

    /** Internal hashed-string name for the attribute. */
    @Builder.Default
    @JsonInclude(JsonInclude.Include.ALWAYS)
    String name = "";

    /** Human-readable name of the attribute. */
    String displayName;

    /** Explanation of the attribute. */
    @Builder.Default
    @JsonInclude(JsonInclude.Include.ALWAYS)
    String description = "";

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

    /** Indicates whether the attribute has a default value of being empty (true) or not (false). */
    Boolean isDefaultValueNull;

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
    Boolean isIndexable = false;

    /** Whether changes to this attribute's value generate an event (true) or not (false). */
    @Builder.Default
    Boolean includeInNotification = true;

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

    /**
     * Whether this attribute is archived (true) or active (false).
     * @return boolean
     */
    @JsonIgnore
    public boolean isArchived() {
        return options != null && options.getIsArchived() != null && options.getIsArchived();
    }

    /**
     * Determine the basic type of this attribute (irrespective of whether it is multivalued or not).
     * For example, the basic type of {@code array<string>} is just {@code string}.
     *
     * @return the basic type of this attribute
     */
    @JsonIgnore
    public String getBasicType() {
        return getBasicType(getTypeName());
    }

    /**
     * Determine the container of this attribute, if any.
     * For example, the container of {@code array<string>} is a list or sorted set.
     *
     * @return the container of this attribute, if any, or null if it is a single-valued attribute
     */
    @JsonIgnore
    public String getContainerType() {
        return getContainerType(getTypeName());
    }

    /**
     * Determine the basic type of the attribute (irrespective of whether it is multivalued or not).
     * For example, the basic type of {@code array<string>} is just {@code string}.
     *
     * @param type typeName of the attribute
     * @return the basic type of this attribute
     */
    public static String getBasicType(String type) {
        String baseType = type;
        if (type.contains("<")) {
            if (type.startsWith("array<")) {
                if (type.startsWith("array<map<")) {
                    baseType = getEmbeddedType(type.substring("array<".length(), type.length() - 1));
                } else {
                    baseType = getEmbeddedType(type);
                }
            } else if (type.startsWith("map<")) {
                baseType = getEmbeddedType(type);
            }
        }
        return baseType;
    }

    /**
     * Determine the container of the attribute, if any.
     * For example, the container of {@code array<string>} is a list or sorted set.
     *
     * @param type typeName of the attribute
     * @return the container of this attribute, if any, or null if it is a single-valued attribute
     */
    public static String getContainerType(String type) {
        String container = null;
        if (type.contains("<")) {
            if (type.startsWith("array<")) {
                if (type.startsWith("array<map<")) {
                    container = "List<Map<";
                } else {
                    container = "SortedSet<";
                }
            } else if (type.startsWith("map<")) {
                container = "Map<";
            }
        }
        return container;
    }

    /**
     * Determine the primitive type of the attribute when it's values are contained in an
     * array or map.
     *
     * @param attrType data type of the attribute
     * @return the primitive contained type of the attribute's values
     */
    private static String getEmbeddedType(String attrType) {
        return attrType.substring(attrType.indexOf("<") + 1, attrType.indexOf(">"));
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(AttributeDef o) {
        return attributeDefComparator.compare(this, o);
    }

    public abstract static class AttributeDefBuilder<
                    C extends AttributeDef, B extends AttributeDef.AttributeDefBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {

        /**
         * Mark this attribute definition as archived. Note that this will only do so if
         * the attribute is already defined (i.e. has some options). Otherwise, this operation does
         * nothing to the attribute definition.
         *
         * @param by name of the user who is archiving the attribute definition
         * @return the builder for archiving this attribute definition
         */
        public B archive(String by) {
            if (options != null) {
                long removalEpoch = Instant.now().toEpochMilli();
                options = options.toBuilder()
                        .isArchived(true)
                        .archivedBy(by)
                        .archivedAt(removalEpoch)
                        .build();
                displayName = displayName + "-archived-" + removalEpoch;
            }
            return self();
        }

        /**
         * Configure this attribute definition to allow multiple values. Note that you MUST
         * first have defined a type for the attribute definition, or this will likely not work
         * as expected.
         *
         * @param multiValued true if multiple values are allowed for the attribute, otherwise false
         * @return the builder configured for the multiple or singular values for this attribute definition
         */
        public B multiValued(boolean multiValued) {
            if (options != null) {
                options = options.toBuilder().multiValueSelect(multiValued).build();
            } else {
                options = AttributeDefOptions.builder()
                        .multiValueSelect(multiValued)
                        .build();
            }
            if (multiValued) {
                if (typeName != null && !typeName.startsWith("array<")) {
                    typeName("array<" + typeName + ">");
                }
                cardinality(AtlanCustomAttributeCardinality.SET);
            } else {
                if (typeName != null && typeName.startsWith("array<")) {
                    typeName(typeName.substring("array<".length(), typeName.length() - 1));
                }
                cardinality(AtlanCustomAttributeCardinality.SINGLE);
            }
            return self();
        }
    }
}
