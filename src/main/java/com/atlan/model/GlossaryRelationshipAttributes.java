/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

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
public class GlossaryRelationshipAttributes extends AssetRelationshipAttributes {
    /** Terms within this glossary. */
    @Singular
    List<Reference> terms;

    /** Categories within this glossary. */
    @Singular
    List<Reference> categories;

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof GlossaryRelationshipAttributes;
    }
}
