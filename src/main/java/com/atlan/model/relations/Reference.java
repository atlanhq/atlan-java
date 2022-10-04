/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanStatus;
import java.util.Comparator;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Reference extends AtlanObject implements Comparable<Reference> {
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

    /**
     * Quickly create a new reference to another asset, by its GUID.
     *
     * @param typeName type of the asset to reference
     * @param guid GUID of the asset to reference
     * @return a reference to another asset
     */
    public static GuidReference to(String typeName, String guid) {
        return GuidReference.of(typeName, guid);
    }

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
     *
     * @param typeName type of the asset to reference
     * @param qualifiedName of the asset to reference
     * @return a reference to another asset
     */
    public static QualifiedNameReference by(String typeName, String qualifiedName) {
        return QualifiedNameReference.of(typeName, qualifiedName);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Reference o) {
        return referenceComparator.compare(this, o);
    }
}
