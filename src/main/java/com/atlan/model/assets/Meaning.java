/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a term related to an asset.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Meaning extends AtlanObject {

    /** Unique identifier (GUID) of the related term. */
    String termGuid;

    /** Unique identifier (GUID) of the relationship itself. */
    String relationGuid;

    /** Human-readable display name of the related term. */
    String displayText;

    /** Unused. */
    Integer confidence;
}
