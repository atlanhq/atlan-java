/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AuditDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class Reference extends AtlanObject implements Comparable<Reference>, AuditDetail {
    private static final long serialVersionUID = 2L;

    // Sort references in a set based first on their relationshipGuid (if any),
    // then by their guid (if any), then by their qualifiedName (if any),
    // and finally by their awsArn — at least one of these must exist if this
    // is any kind of valid reference. (And there cannot be duplicate combinations
    // of these properties to be a valid set of references: they would be duplicate
    // references.)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<UniqueAttributes> uniqueAttrsComparator = Comparator.comparing(
                    UniqueAttributes::getQualifiedName, stringComparator)
            .thenComparing(UniqueAttributes::getAwsArn, stringComparator);
    private static final Comparator<Reference> referenceComparator = Comparator.comparing(
                    Reference::getRelationshipGuid, stringComparator)
            .thenComparing(Reference::getGuid, stringComparator)
            .thenComparing(Reference::getUniqueAttributes, Comparator.nullsFirst(uniqueAttrsComparator));

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
    public static Reference to(String typeName, String guid) {
        return Reference.builder().typeName(typeName).guid(guid).build();
    }

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
     *
     * @param typeName type of the asset to reference
     * @param qualifiedName of the asset to reference
     * @return a reference to another asset
     */
    public static Reference by(String typeName, String qualifiedName) {
        return Reference.builder()
                .typeName(typeName)
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
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

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     *
     * @return true if it is a valid GUID reference, false otherwise
     */
    @JsonIgnore
    public boolean isValidReferenceByGuid() {
        // Careful: because typeName is overridden (for default setting) in
        // derived classes, we must use the getter here and not the member directly
        return getTypeName() != null
                && !getTypeName().isEmpty()
                && getGuid() != null
                && !getGuid().isEmpty();
    }

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     *
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    @JsonIgnore
    public boolean isValidReferenceByQualifiedName() {
        // Careful: because typeName is overridden (for default setting) in
        // derived classes, we must use the getter here and not the member directly
        return getTypeName() != null
                && !getTypeName().isEmpty()
                && getUniqueAttributes() != null
                && getUniqueAttributes().getQualifiedName() != null
                && !getUniqueAttributes().getQualifiedName().isEmpty();
    }

    /**
     * Indicates whether this object can be used as a reference (relationship).
     *
     * @return true if it is a valid reference, false otherwise
     */
    @JsonIgnore
    public boolean isValidReference() {
        return isValidReferenceByGuid() || isValidReferenceByQualifiedName();
    }

    /** Name of the type that defines the entity. */
    String typeName;

    /** Unique identifier for the record in the search index (for search results). */
    String docId;

    /** Globally-unique identifier for the entity. */
    String guid;

    /** Human-readable name of the entity. */
    String displayText;

    /** Status of the entity (if this is a related entity). */
    String entityStatus;

    /** Type of the relationship (if this is a related entity). */
    String relationshipType;

    /** Unique identifier of the relationship (when this is a related entity). */
    String relationshipGuid;

    /** Status of the relationship (when this is a related entity). */
    AtlanStatus relationshipStatus;

    /** Attributes specific to the relationship. */
    RelationshipAttributes relationshipAttributes;

    /**
     * Attribute(s) that uniquely identify the entity (when this is a related entity).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes uniqueAttributes;

    /** Arbitrary textual labels for the asset. */
    @Singular
    List<String> labels;

    /**
     * Attribute(s) that handle custom information direct from source systems. Not to be
     * confused with custom metadata attributes, which are user-defined and user-managed, and
     * are instead found in {@code customMetadataSets}.
     */
    @Singular("customAttribute")
    Map<String, String> customAttributes;

    /** Relationships that were added to this asset (part of audit logging ONLY). */
    final Reference addedRelationshipAttributes;

    /** Relationships that were removed from this asset (part of audit logging ONLY). */
    final Reference removedRelationshipAttributes;

    /** {@inheritDoc} */
    @Override
    public int compareTo(Reference o) {
        if (o == null) return -1;
        return referenceComparator.compare(this, o);
    }

    public abstract static class ReferenceBuilder<C extends Reference, B extends ReferenceBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
