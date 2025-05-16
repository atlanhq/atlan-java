/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.serde.CustomMetadataAuditDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Capture the attributes and values for custom metadata as tracked through the audit log.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = CustomMetadataAuditDeserializer.class)
@ToString(callSuper = true)
public class CustomMetadataAttributesAuditDetail extends CustomMetadataAttributes implements AuditDetail {
    private static final long serialVersionUID = 2L;

    /** Unique name of the custom metadata set (structure). */
    String typeName;

    /**
     * Indicates whether this custom metadata audit entry is empty,
     * typically true when the custom metadata has since been deleted from Atlan.
     */
    Boolean empty;

    public abstract static class CustomMetadataAttributesAuditDetailBuilder<
                    C extends CustomMetadataAttributesAuditDetail,
                    B extends CustomMetadataAttributesAuditDetailBuilder<C, B>>
            extends CustomMetadataAttributes.CustomMetadataAttributesBuilder<C, B> {}
}
