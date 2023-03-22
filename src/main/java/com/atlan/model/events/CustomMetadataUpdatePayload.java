/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.assets.Asset;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.serde.CustomMetadataMapDeserializer;
import com.atlan.serde.CustomMetadataMapSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomMetadataUpdatePayload extends AtlanEventPayload {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BUSINESS_ATTRIBUTE_UPDATE";

    /** Fixed type for asset-related events. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String type = "ENTITY_NOTIFICATION_V2";

    /** Fixed operation for custom metadata update payloads. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String operationType = TYPE_NAME;

    /**
     * Details of the asset that was updated.
     * Note that the details of the custom metadata are NOT included in this object,
     * but only in the mutatedDetails.
     * @see #mutatedDetails
     */
    @JsonProperty("entity")
    Asset asset;

    /**
     * Map of custom metadata attributes and values defined on the asset. The map is keyed by the human-readable
     * name of the custom metadata set, and the values are a further mapping from human-readable attribute name
     * to the value for that attribute as provided when updating this asset.
     */
    @JsonDeserialize(using = CustomMetadataMapDeserializer.class)
    @JsonSerialize(using = CustomMetadataMapSerializer.class)
    Map<String, CustomMetadataAttributes> mutatedDetails;
}
