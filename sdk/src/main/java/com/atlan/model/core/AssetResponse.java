/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.assets.Asset;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    Object referredEntities;

    /** The retrieved entity. */
    @JsonProperty("entity")
    Asset asset;
}
