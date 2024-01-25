/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AuditDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Comparator;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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

    /** Attributes specific to the relationship (unused). */
    Map<String, Object> relationshipAttributes;

    /**
     * Attribute(s) that uniquely identify the entity (when this is a related entity).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes uniqueAttributes;

    /** {@inheritDoc} */
    @Override
    public int compareTo(Reference o) {
        return referenceComparator.compare(this, o);
    }
}
