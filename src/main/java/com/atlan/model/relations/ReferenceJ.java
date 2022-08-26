/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import com.atlan.model.enums.AtlanStatus;
import com.atlan.net.AtlanObjectJ;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

// TODO: Consider making this object the root of the entire inheritance hierarchy (above Entity)
//  - then we can actually pull back fully-typed objects via search (?)

@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReferenceJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    /**
     * Quickly create a new reference to another asset, by its GUID.
     *
     * @param typeName type of the asset to reference
     * @param guid GUID of the asset to reference
     * @return a reference to another asset
     */
    public static GuidReferenceJ to(String typeName, String guid) {
        return GuidReferenceJ.of(typeName, guid);
    }

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
     *
     * @param typeName type of the asset to reference
     * @param qualifiedName of the asset to reference
     * @return a reference to another asset
     */
    public static QualifiedNameReferenceJ by(String typeName, String qualifiedName) {
        return QualifiedNameReferenceJ.of(typeName, qualifiedName);
    }

    /**
     * Unique identifier of the related entity. If the uniqueAttributes are not provided, this must be
     * provided.
     */
    String guid;

    /** Type of the related entity. */
    String typeName;

    /** Status of the related entity. */
    String entityStatus;

    /** Human-readable name of the related entity. */
    String displayText;

    /** Type of the relationship itself. */
    String relationshipType;

    /** Unique identifier of the relationship itself. */
    String relationshipGuid;

    /** Status of the relationship itself. */
    AtlanStatus relationshipStatus;

    /** Unused. */
    Map<String, Object> relationshipAttributes;

    /**
     * Attribute(s) that uniquely identify the related entity. If the guid is not provided, these must
     * be provided.
     */
    UniqueAttributesJ uniqueAttributes;

    /**
     * Attributes of the referenced entity.
     * Note that these will only be populated when the reference is being returned as part of a search query;
     * in all other circumstances these will be null.
     */
    Map<String, Object> attributes;
}
