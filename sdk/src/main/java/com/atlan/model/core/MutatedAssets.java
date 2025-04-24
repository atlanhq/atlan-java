/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import com.atlan.model.assets.Asset;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class MutatedAssets extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Assets that were created. */
    @JsonProperty("CREATE")
    List<Asset> CREATE;

    /** Assets that were updated. */
    @JsonProperty("UPDATE")
    List<Asset> UPDATE;

    /** Assets that were partially updated. */
    @JsonProperty("PARTIAL_UPDATE")
    List<Asset> PARTIAL_UPDATE;

    /** Assets that were deleted. */
    @JsonProperty("DELETE")
    List<Asset> DELETE;
}
