/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanClassificationColor;
import com.atlan.model.enums.AtlanTypeCategory;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of a classification.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ClassificationDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for classification typedefs. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.CLASSIFICATION;

    /** Options that describe the classification. */
    ClassificationOptions options;

    /** Unused. */
    List<String> superTypes;

    /** List of the types of entities that the classification can be applied to. */
    List<String> entityTypes;

    /** Unused. */
    List<String> subTypes;

    /** TBC */
    @Builder.Default
    Boolean skipDisplayNameUniquenessCheck = false;

    /**
     * Builds the minimal object necessary to create a classification definition.
     *
     * @param displayName the human-readable name for the classification
     * @param color the color to use for the classification
     * @return the minimal request necessary to create the classification typedef, as a builder
     */
    public static ClassificationDefBuilder<?, ?> creator(String displayName, AtlanClassificationColor color) {
        return ClassificationDef.builder()
                .name(displayName)
                .displayName(displayName)
                .options(ClassificationOptions.of(color));
    }

    /**
     * Create this classification definition in Atlan.
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public ClassificationDef create() throws AtlanException {
        TypeDefResponse response = TypeDefsEndpoint.createTypeDef(this);
        if (response != null && !response.getClassificationDefs().isEmpty()) {
            return response.getClassificationDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) a classification by its human-readable name. This operation is irreversible.
     * If there are any existing classification instances, this operation will fail.
     *
     * @param displayName human-readable name of the classification
     * @throws AtlanException on any error during the API invocation
     */
    public static void purge(String displayName) throws AtlanException {
        String internalName = ClassificationCache.getIdForName(displayName);
        TypeDefsEndpoint.purgeTypeDef(internalName);
    }
}
