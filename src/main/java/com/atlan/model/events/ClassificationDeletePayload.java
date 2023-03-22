/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.assets.Asset;
import com.atlan.model.core.Classification;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ClassificationDeletePayload extends AtlanEventPayload {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CLASSIFICATION_DELETE";

    /** Fixed type for asset-related events. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String type = "ENTITY_NOTIFICATION_V2";

    /** Fixed operation for classification deletion payloads. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String operationType = TYPE_NAME;

    /**
     * Details of the asset that was updated.
     * Note that the details of the removed classification will not be present in this object, but
     * only in the mutatedDetails.
     * @see #mutatedDetails
     */
    @JsonProperty("entity")
    Asset asset;

    /** Classifications that were removed from the asset by this event. */
    Set<Classification> mutatedDetails;
}
