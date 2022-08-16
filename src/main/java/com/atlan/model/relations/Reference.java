/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import com.atlan.model.enums.AtlanStatus;
import com.atlan.net.AtlanObject;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Reference extends AtlanObject {

    /**
     * Quickly create a new reference to another asset, by its GUID.
     * @param typeName type of the asset to reference
     * @param guid GUID of the asset to reference
     * @return a reference to another asset
     */
    public static Reference to(String typeName, String guid) {
        return Reference.builder().typeName(typeName).guid(guid).build();
    }

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
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
    UniqueAttributes uniqueAttributes;
}
