/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.nr;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.enums.AtlanStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("cast")
public class Ref extends AtlanObject implements Comparable<Ref> {
    private static final long serialVersionUID = 2L;

    private static final ObjectMapper mapper = new ObjectMapper();

    // Sort references in a set based first on their relationshipGuid (if any),
    // then by their guid (if any), then by their qualifiedName (if any),
    // and finally by their awsArn — at least one of these must exist if this
    // is any kind of valid reference. (And there cannot be duplicate combinations
    // of these properties to be a valid set of references: they would be duplicate
    // references.)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<Ref> referenceComparator = Comparator.comparing(
            Ref::getRelationshipGuid, stringComparator)
        .thenComparing(Ref::getGuid, stringComparator);

    public enum SaveSemantic {
        REPLACE,
        APPEND,
        REMOVE,
    }

    /**
     * Quickly create a new reference to another asset, by its GUID.
     *
     * @param typeName type of the asset to reference
     * @param guid GUID of the asset to reference
     * @return a reference to another asset
     */
    public static Ref to(String typeName, String guid) {
        return Ref.builder().typeName(typeName).guid(guid).build();
    }

    /**
     * Validate that the required parameters are present to carry out an operation.
     *
     * @param typeName type of asset being validated
     * @param parameters mapping of required parameters, keyed by parameter name with the value of that parameter
     * @throws InvalidRequestException if any of the parameters have a null or empty value
     */
    protected static void validateRequired(String typeName, Map<String, String> parameters)
        throws InvalidRequestException {
        validate(ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, typeName, parameters);
    }

    /**
     * Validate that the required parameters are present to set up a relationship.
     *
     * @param typeName type of asset to which the relationship is being created
     * @param parameters mapping of required parameters, keyed by parameter name with the value of that parameter
     * @throws InvalidRequestException if any of the parameters have a null or empty value
     */
    protected static void validateRelationship(String typeName, Map<String, String> parameters)
        throws InvalidRequestException {
        validate(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, typeName, parameters);
    }

    private static void validate(ErrorCode code, String typeName, Map<String, String> parameters)
        throws InvalidRequestException {
        if (parameters != null && !parameters.isEmpty()) {
            List<String> missing = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    missing.add(entry.getKey());
                }
            }
            if (!missing.isEmpty()) {
                throw new InvalidRequestException(code, typeName, String.join(",", missing));
            }
        }
    }

    /** Semantic for how this relationship should be saved, if used in an asset request on which .save() is called. */
    @JsonIgnore
    @Builder.Default
    transient SaveSemantic semantic = SaveSemantic.REPLACE;

    /** Internal tracking of whether this represents a complete view of an object, or not. */
    @JsonIgnore
    @Builder.Default
    transient boolean completeObject = false;

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    @JsonIgnore
    public boolean isComplete() {
        return completeObject;
    }

    /**
     * Sets the internal tracking of this object to indicate it is a complete representation of an entity.
     */
    @JsonIgnore
    protected void setCompleteObject() {
        this.completeObject = true;
    }

    /** Name of the type that defines the entity. */
    @Getter
    String typeName;

    /** Globally-unique identifier for the entity. */
    @Getter
    String guid;

    /** Human-readable name of the entity. */
    @Getter
    String displayText;

    /** Status of the entity (if this is a related entity). */
    @Getter
    String entityStatus;

    /** Type of the relationship (if this is a related entity). */
    @Getter
    String relationshipType;

    /** Unique identifier of the relationship (when this is a related entity). */
    @Getter
    String relationshipGuid;

    /** Status of the relationship (when this is a related entity). */
    @Getter
    AtlanStatus relationshipStatus;

    /** Direct serde of attributes. */
    @JsonProperty("attributes")
    protected Map<String, Object> attributes;

    /** Direct serde of relationship attributes. */
    @JsonProperty("relationshipAttributes")
    protected Map<String, Object> relationshipAttributes;

    /** Direct serde of unique attributes. */
    @JsonProperty("uniqueAttributes")
    protected Map<String, Object> uniqueAttributes;

    /**
     * Attribute(s) that handle custom information direct from source systems. Not to be
     * confused with custom metadata attributes, which are user-defined and user-managed, and
     * are instead found in {@code customMetadataSets}.
     */
    @Getter
    @Singular
    Map<String, String> customAttributes;

    @JsonIgnore
    protected <T> T getAttribute(String name, Class<T> type) {
        Object value = getAttribute(name);
        if (value == null) return null;
        return type.cast(value);
    }

    @JsonIgnore
    protected <T> T getAttribute(String name, TypeReference<T> type) {
        Object value = getAttribute(name);
        if (value == null) return null;
        return mapper.convertValue(value, type);
    }

    @JsonIgnore
    public <E extends Enum<E>> E getEnumAttribute(String name, Class<E> enumType) {
        Object value = getAttribute(name);
        if (value == null) return null;
        if (enumType.isInstance(value)) {
            // If the stored value is already an enum instance of the correct type, return it directly
            return enumType.cast(value);
        }
        if (value instanceof String) {
            // TODO: not this simple, given the enum ID doesn't match (string) value...
            return Enum.valueOf(enumType, (String) value);
        }
        throw new IllegalArgumentException("Property " + name + " cannot be cast to enum type " + enumType.getName());
    }

    @JsonIgnore
    private Object getAttribute(String name) {
        Object value = null;
        if (relationshipAttributes != null) value = relationshipAttributes.get(name);
        if (value == null && attributes != null) value = attributes.get(name);
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Ref o) {
        return referenceComparator.compare(this, o);
    }
}
