/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTermAttributes extends AssetAttributes {
    @Override
    protected boolean canEqual(Object other) {
        return other instanceof GlossaryTermAttributes;
    }
}
