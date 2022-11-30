/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.CustomMetadataAttributes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Capture the attributes and values for custom metadata as tracked through the audit log.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomMetadataAttributesAuditDetail extends CustomMetadataAttributes implements AuditDetail {

    /** Unique name of the custom metadata set (structure). */
    String typeName;
}
