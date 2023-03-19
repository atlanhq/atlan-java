/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.serde.CustomMetadataAuditDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Capture the attributes and values for custom metadata as tracked through the audit log.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = CustomMetadataAuditDeserializer.class)
public class CustomMetadataAttributesAuditDetail extends CustomMetadataAttributes implements AuditDetail {

    /** Unique name of the custom metadata set (structure). */
    String typeName;
}
