/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.serde.AuditDetailDeserializer;
import com.atlan.serde.AuditDetailSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * An interface used to describe the detailed information within an audit entry.
 * In practice this can be either details about a Classification or about an Entity.
 * (You should be able to type-check through instanceof and then explicitly cast
 * to either of these more detailed objects.)
 */
@JsonSerialize(using = AuditDetailSerializer.class)
@JsonDeserialize(using = AuditDetailDeserializer.class)
public interface AuditDetail {

    /** Returns the type of the instance details in the audit entry. */
    String getTypeName();
}
