/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.core.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Asset extends Entity {

    /** Attributes that can exist across all assets in Atlan. */
    @Getter(onMethod_ = {@Override})
    transient AssetAttributes attributes;

    /** Relationships that can exist across all assets in Atlan. */
    @Getter(onMethod_ = {@Override})
    transient AssetRelationshipAttributes relationshipAttributes;

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Asset;
    }
}
