/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryX extends AssetX {

    public static final String TYPE_NAME = "AtlasGlossary";

    /** Fixed typeName for glossaries. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Terms within this glossary. */
    @Singular
    @Attribute List<Reference> terms;

    /** Categories within this glossary. */
    @Singular
    @Attribute List<Reference> categories;

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Glossary;
    }

    /**
     * Builds the minimal request necessary to create a glossary.
     *
     * @param name of the glossary
     * @return the minimal request necessary to create the glossary
     */
    public static GlossaryX createRequest(String name) {
        return GlossaryX.builder()
            .qualifiedName(name)
            .name(name)
            .build();
    }

    /**
     * Builds the minimal request necessary to update a glossary.
     *
     * @param guid unique identifier of the glossary
     * @param name of the glossary
     * @return the minimal request necessary to update the glossary
     */
    public static GlossaryX updateRequest(String guid, String name) {
        return GlossaryX.builder()
            .guid(guid)
            .qualifiedName(name)
            .name(name)
            .build();
    }
}
