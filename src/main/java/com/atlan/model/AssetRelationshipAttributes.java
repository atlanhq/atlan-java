/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.core.RelationshipAttributes;
import com.atlan.model.relations.Reference;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AssetRelationshipAttributes extends RelationshipAttributes {

    /** Resources that are linked to this asset. */
    @Singular
    List<Reference> links;

    /** Readme that is linked to this asset. */
    Reference readme;

    /** Terms that are linked to this asset. */
    @Singular
    List<Reference> meanings;

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof AssetRelationshipAttributes;
    }
}
