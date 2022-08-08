/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a glossary in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Glossary extends Asset {

    public static final String TYPE_NAME = "AtlasGlossary";

    /** Fixed typeName for glossaries. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    private final String shortDescription = "";

    private final String longDescription = "";
    private final String language = "";
    private final String usage = "";
    private final Map<String, String> additionalAttributes = Collections.emptyMap();

    /** Terms within this glossary. */
    @Singular
    @Attribute
    List<Reference> terms;

    /** Categories within this glossary. */
    @Singular
    @Attribute
    List<Reference> categories;

    /**
     * Builds the minimal object necessary for creating a glossary.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling additional
     * methods to add metadata followed by {@link GlossaryBuilder#build()}.
     *
     * @param name of the glossary
     * @return the minimal object necessary to create the glossary
     */
    public static Glossary toCreate(String name) {
        return Glossary.builder().qualifiedName(name).name(name).build();
    }

    /**
     * Builds the minimal object necessary to update a glossary.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling additional
     * methods to add metadata followed by {@link GlossaryBuilder#build()}.
     *
     * @param guid unique identifier of the glossary
     * @param name of the glossary
     * @return the minimal object necessary to update the glossary
     */
    public static Glossary toUpdate(String guid, String name) {
        return Glossary.builder().guid(guid).qualifiedName(name).name(name).build();
    }
}
