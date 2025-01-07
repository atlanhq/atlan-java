/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * An interface used to describe the detailed information within an audit entry.
 * In practice this can be either details about an Atlan tag or about an Entity.
 * (You should be able to type-check through instanceof and then explicitly cast
 * to either of these more detailed objects.)
 */
public interface AuditDetail {

    /** Returns the type of the instance details in the audit entry. */
    String getTypeName();

    /**
     * Returns the raw JsonNode exposed by the Jackson library. This can be used to access properties
     * that are not directly exposed by Atlan's Java library.
     *
     * <p>Note: You should always prefer using the standard property accessors whenever possible.
     * Because this method exposes Jackson's underlying API, it is not considered fully stable. Atlan's
     * Java library might move off Jackson in the future and this method would be removed or change
     * significantly.</p>
     *
     * @return The raw JsonNode.
     */
    @JsonIgnore
    JsonNode getRawJsonObject();

    /**
     * Sets the raw response from the API. This is used to expose properties that are not
     * directly exposed by Atlan's Java library.
     *
     * @param rawJsonObject the raw JSON from the API response
     */
    @JsonIgnore
    void setRawJsonObject(JsonNode rawJsonObject);
}
