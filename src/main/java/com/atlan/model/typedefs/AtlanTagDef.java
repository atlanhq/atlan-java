/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.cache.AtlanTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.AtlanTypeCategory;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an Atlan tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlanTagDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for Atlan tag typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.ATLAN_TAG;

    /** Options that describe the Atlan tag. */
    AtlanTagOptions options;

    /** Unused. */
    List<String> superTypes;

    /** List of the types of entities that the Atlan tag can be applied to. */
    List<String> entityTypes;

    /** Unused. */
    List<String> subTypes;

    /** TBC */
    @Builder.Default
    Boolean skipDisplayNameUniquenessCheck = false;

    /**
     * Builds the minimal object necessary to create an Atlan tag definition, using the default tag icon.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param color the color to use for the Atlan tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, AtlanTagColor color) {
        return AtlanTagDef.builder().name(displayName).displayName(displayName).options(AtlanTagOptions.of(color));
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param icon the built-in icon to use for the Atlan tag
     * @param color the color to use for the Atlan tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, AtlanIcon icon, AtlanTagColor color) {
        return AtlanTagDef.builder()
                .name(displayName)
                .displayName(displayName)
                .options(AtlanTagOptions.withIcon(icon, color));
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param url URL to an image to use for the Atlan tag
     * @param color the color to use for the Atlan tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     * @throws AtlanException on any issues uploading the image from the provided URL
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, String url, AtlanTagColor color)
            throws AtlanException {
        return AtlanTagDef.builder()
                .name(displayName)
                .displayName(displayName)
                .options(AtlanTagOptions.withImage(url, color));
    }

    /**
     * Create this Atlan tag definition in Atlan.
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public AtlanTagDef create() throws AtlanException {
        TypeDefResponse response = TypeDefsEndpoint.createTypeDef(this);
        if (response != null && !response.getAtlanTagDefs().isEmpty()) {
            AtlanTagCache.refreshCache();
            return response.getAtlanTagDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) an Atlan tag by its human-readable name. This operation is irreversible.
     * If there are any existing Atlan tag instances, this operation will fail.
     *
     * @param displayName human-readable name of the Atlan tag
     * @throws AtlanException on any error during the API invocation
     */
    public static void purge(String displayName) throws AtlanException {
        String internalName = AtlanTagCache.getIdForName(displayName);
        TypeDefsEndpoint.purgeTypeDef(internalName);
        AtlanTagCache.refreshCache();
    }
}
