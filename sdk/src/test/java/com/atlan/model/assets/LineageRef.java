/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Capture minimal information about an asset for lineage reference purposes.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class LineageRef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique name of the asset being referenced. */
    String qualifiedName;

    /** Simple name of the asset being referenced. */
    String name;

    /** UUID of the asset being referenced. */
    String guid;
}
