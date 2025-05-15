/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanTypeCategory;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of custom metadata.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomMetadataDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for custom metadata typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.CUSTOM_METADATA;

    /** Options for the custom metadata. */
    CustomMetadataOptions options;

    /**
     * Builds the minimal object necessary to create a custom metadata definition.
     * Note: without any enrichment, this will create a custom metadata set with no attributes. This is valid,
     * but probably not useful for anything!
     *
     * @param displayName the human-readable name for the custom metadata set
     * @return the minimal request necessary to create the custom metadata typedef, as a builder
     */
    public static CustomMetadataDefBuilder<?, ?> creator(String displayName) {
        return CustomMetadataDef.builder().name(displayName).displayName(displayName);
    }

    /**
     * Create this custom metadata definition in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to create the custom metadata
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public synchronized CustomMetadataDef create(AtlanClient client) throws AtlanException {
        TypeDefResponse response = client.typeDefs.create(this);
        if (response != null && !response.getCustomMetadataDefs().isEmpty()) {
            return response.getCustomMetadataDefs().get(0);
        }
        return null;
    }

    /**
     * Update this custom metadata definition in Atlan.
     * Note: there are many restrictions on what you can / should update, so this should really be treated
     * as an internal method. (This will also force a refresh of the custom metadata cache.)
     *
     * @param client connectivity to the Atlan tenant on which to update the custom metadata
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API communication issues
     */
    public CustomMetadataDef update(AtlanClient client) throws AtlanException {
        TypeDefResponse response = client.typeDefs.update(this);
        if (response != null && !response.getCustomMetadataDefs().isEmpty()) {
            return response.getCustomMetadataDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) a custom metadata definition by its human-readable name. This operation is irreversible.
     * If there are any existing uses of the custom metadata on an asset, this operation will fail.
     *
     * @param client connectivity to the Atlan tenant from which to purge the custom metadata
     * @param displayName human-readable name of the custom metadata definition
     * @throws AtlanException on any error during the API invocation
     */
    public static synchronized void purge(AtlanClient client, String displayName) throws AtlanException {
        String internalName = client.getCustomMetadataCache().getSidForName(displayName);
        client.typeDefs.purge(internalName);
    }

    public abstract static class CustomMetadataDefBuilder<C extends CustomMetadataDef, B extends CustomMetadataDefBuilder<C, B>>
        extends TypeDef.TypeDefBuilder<C, B> {}
}
