/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.AtlanTypeCategory;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an Atlan tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
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
        return creator(displayName, color, false);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition, using the default tag icon.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param color the color to use for the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, AtlanTagColor color, boolean sourceSynced) {
        return setupSourceSynced(
                AtlanTagDef.builder()
                        .name(displayName)
                        .displayName(displayName)
                        .options(AtlanTagOptions.of(color, sourceSynced)),
                sourceSynced);
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
        return creator(displayName, icon, color, false);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param icon the built-in icon to use for the Atlan tag
     * @param color the color to use for the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     */
    public static AtlanTagDefBuilder<?, ?> creator(
            String displayName, AtlanIcon icon, AtlanTagColor color, boolean sourceSynced) {
        return setupSourceSynced(
                AtlanTagDef.builder()
                        .name(displayName)
                        .displayName(displayName)
                        .options(AtlanTagOptions.withIcon(icon, color, sourceSynced)),
                sourceSynced);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param client connectivity to the Atlan tenant in which the tag is intended to be created
     * @param displayName the human-readable name for the Atlan tag
     * @param url URL to an image to use for the Atlan tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     * @throws AtlanException on any issues uploading the image from the provided URL
     */
    public static AtlanTagDefBuilder<?, ?> creator(AtlanClient client, String displayName, String url)
            throws AtlanException {
        return creator(client, displayName, url, false);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param client connectivity to the Atlan tenant in which the tag is intended to be created
     * @param displayName the human-readable name for the Atlan tag
     * @param url URL to an image to use for the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     * @throws AtlanException on any issues uploading the image from the provided URL
     */
    public static AtlanTagDefBuilder<?, ?> creator(
            AtlanClient client, String displayName, String url, boolean sourceSynced) throws AtlanException {
        return setupSourceSynced(
                AtlanTagDef.builder()
                        .name(displayName)
                        .displayName(displayName)
                        .options(AtlanTagOptions.withImage(client, url, sourceSynced)),
                sourceSynced);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param options to use when creating the Atlan tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     * @throws AtlanException on any issues uploading the image from the provided URL
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, AtlanTagOptions options) throws AtlanException {
        return AtlanTagDef.builder().name(displayName).displayName(displayName).options(options);
    }

    /**
     * Builds the minimal object necessary to create an Atlan tag definition.
     *
     * @param displayName the human-readable name for the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the minimal request necessary to create the Atlan tag typedef, as a builder
     * @throws AtlanException on any issues uploading the image from the provided URL
     */
    public static AtlanTagDefBuilder<?, ?> creator(String displayName, boolean sourceSynced) throws AtlanException {
        return setupSourceSynced(AtlanTagDef.builder().name(displayName).displayName(displayName), sourceSynced);
    }

    /**
     * Configure the builder for the tag to support source-synced attributes.
     *
     * @param builder for the tag definition
     * @param sourceSynced if true, add the necessary structure for source-synced tags, otherwise do nothing
     * @return the tag builder, modified to handle source-synced tags
     */
    public static AtlanTagDefBuilder<?, ?> setupSourceSynced(AtlanTagDefBuilder<?, ?> builder, boolean sourceSynced) {
        if (sourceSynced) {
            builder.attributeDef(AttributeDef.builder()
                    .typeName("array<SourceTagAttachment>")
                    .isOptional(true)
                    .cardinality(AtlanCustomAttributeCardinality.SET)
                    .valuesMinCount(0L)
                    .valuesMaxCount(2147483647L)
                    .isUnique(false)
                    .isIndexable(false)
                    .includeInNotification(false)
                    .skipScrubbing(false)
                    .searchWeight(-1L)
                    .displayName("sourceTagAttachment")
                    .isDefaultValueNull(false)
                    .build());
        }
        return builder;
    }

    /**
     * Create this Atlan tag definition in Atlan.
     *
     * @param client connectivity to an Atlan tenant
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public synchronized AtlanTagDef create(AtlanClient client) throws AtlanException {
        TypeDefResponse response = client.typeDefs.create(this);
        if (response != null && !response.getAtlanTagDefs().isEmpty()) {
            return response.getAtlanTagDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) an Atlan tag by its human-readable name. This operation is irreversible.
     * If there are any existing Atlan tag instances, this operation will fail.
     *
     * @param client connectivity to the Atlan tenant from which the Atlan tag should be purged
     * @param displayName human-readable name of the Atlan tag
     * @param client connectivity to an Atlan tenant
     * @throws AtlanException on any error during the API invocation
     */
    public static synchronized void purge(AtlanClient client, String displayName) throws AtlanException {
        String internalName = client.getAtlanTagCache().getSidForName(displayName);
        client.typeDefs.purge(internalName);
    }
}
