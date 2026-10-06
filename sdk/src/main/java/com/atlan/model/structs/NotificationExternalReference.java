/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.NotificationSurface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * One place outside Atlan where a notification was delivered.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class NotificationExternalReference extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "NotificationExternalReference";

    /** Fixed typeName for NotificationExternalReference. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Which external surface this reference is for. */
    NotificationSurface notificationExternalReferenceSurface;

    /** Link to the delivered message, for example a Slack thread permalink or a Teams message link. */
    String notificationExternalReferenceRefUrl;

    /** When the notification was delivered to this surface. */
    Long notificationExternalReferenceDeliveredAt;

    /** Anything else the surface needs to address the delivered message later, such as the identifiers required to edit it in place. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> notificationExternalReferenceExtraAttributes;

    /**
     * Quickly create a new NotificationExternalReference.
     * @param notificationExternalReferenceSurface Which external surface this reference is for.
     * @param notificationExternalReferenceRefUrl Link to the delivered message, for example a Slack thread permalink or a Teams message link.
     * @param notificationExternalReferenceDeliveredAt When the notification was delivered to this surface.
     * @param notificationExternalReferenceExtraAttributes Anything else the surface needs to address the delivered message later, such as the identifiers required to edit it in place.
     * @return a NotificationExternalReference with the provided information
     */
    public static NotificationExternalReference of(
            NotificationSurface notificationExternalReferenceSurface,
            String notificationExternalReferenceRefUrl,
            Long notificationExternalReferenceDeliveredAt,
            Map<String, String> notificationExternalReferenceExtraAttributes) {
        return NotificationExternalReference.builder()
                .notificationExternalReferenceSurface(notificationExternalReferenceSurface)
                .notificationExternalReferenceRefUrl(notificationExternalReferenceRefUrl)
                .notificationExternalReferenceDeliveredAt(notificationExternalReferenceDeliveredAt)
                .notificationExternalReferenceExtraAttributes(notificationExternalReferenceExtraAttributes)
                .build();
    }

    public abstract static class NotificationExternalReferenceBuilder<
                    C extends NotificationExternalReference, B extends NotificationExternalReferenceBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
